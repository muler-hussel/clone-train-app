package org.clone.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.clone.business.domain.TrainStation;
import org.clone.business.domain.TrainStationExample;
import org.clone.business.mapper.TrainStationMapper;
import org.clone.business.req.TrainStationQueryReq;
import org.clone.business.req.TrainStationSaveReq;
import org.clone.business.resp.TrainStationQueryResp;
import org.clone.common.exception.BusinessException;
import org.clone.common.response.PageRes;
import org.clone.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.clone.common.exception.BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR;
import static org.clone.common.exception.BusinessExceptionEnum.BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR;

@Service
public class TrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainStationService.class);

    @Resource
    private TrainStationMapper trainStationMapper;

    public void save(TrainStationSaveReq req) {
        DateTime now = DateTime.now();
        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        if (ObjectUtil.isNull(trainStation.getId())) {

            TrainStation trainStationDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
                throw new BusinessException(BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }
            trainStationDB = selectByUnique(req.getTrainCode(), req.getName());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
                throw new BusinessException(BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR);
            }
            
            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
        } else {
            trainStation.setUpdateTime(now);
            trainStationMapper.updateByPrimaryKey(trainStation);
        }
    }

    public PageRes<TrainStationQueryResp> queryList(TrainStationQueryReq req) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("train_code asc, `index` asc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        LOG.info("Pages: {}", req.getPage());
        LOG.info("Total: {}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);

        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStationList);
        LOG.info("Rows: {}", pageInfo.getTotal());
        LOG.info("Pages: {}", pageInfo.getPages());

        List<TrainStationQueryResp> list = BeanUtil.copyToList(trainStationList, TrainStationQueryResp.class);

        PageRes<TrainStationQueryResp> pageResp = new PageRes<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        trainStationMapper.deleteByPrimaryKey(id);
    }

    private TrainStation selectByUnique(String trainCode, Integer idx) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode).andIndexEqualTo(idx);
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    private TrainStation selectByUnique(String trainCode, String name) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode).andNameEqualTo(name);
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<TrainStation> selectByTrainCode(String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("`index` asc");
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode);
        return trainStationMapper.selectByExample(trainStationExample);
    }
}
