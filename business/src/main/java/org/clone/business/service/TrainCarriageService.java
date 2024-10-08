package org.clone.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.clone.business.domain.TrainCarriage;
import org.clone.business.domain.TrainCarriageExample;
import org.clone.business.enums.SeatColEnum;
import org.clone.business.mapper.TrainCarriageMapper;
import org.clone.business.req.TrainCarriageQueryReq;
import org.clone.business.req.TrainCarriageSaveReq;
import org.clone.business.resp.TrainCarriageQueryResp;
import org.clone.common.exception.BusinessException;
import org.clone.common.response.PageRes;
import org.clone.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.clone.common.exception.BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR;

@Service
public class TrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainCarriageService.class);

    @Resource
    private TrainCarriageMapper trainCarriageMapper;

    public void save(TrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        //自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());

        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        if (ObjectUtil.isNull(trainCarriage.getId())) {

            TrainCarriage trainCarriageDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainCarriageDB)) {
                throw new BusinessException(BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }

            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
        } else {
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateByPrimaryKey(trainCarriage);
        }
    }

    public PageRes<TrainCarriageQueryResp> queryList(TrainCarriageQueryReq req) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("train_code asc, `index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        LOG.info("Pages: {}", req.getPage());
        LOG.info("Total: {}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainCarriage> trainCarriageList = trainCarriageMapper.selectByExample(trainCarriageExample);

        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriageList);
        LOG.info("Rows: {}", pageInfo.getTotal());
        LOG.info("Pages: {}", pageInfo.getPages());

        List<TrainCarriageQueryResp> list = BeanUtil.copyToList(trainCarriageList, TrainCarriageQueryResp.class);

        PageRes<TrainCarriageQueryResp> pageResp = new PageRes<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        trainCarriageMapper.deleteByPrimaryKey(id);
    }

    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("train_code asc, `index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        return trainCarriageMapper.selectByExample(trainCarriageExample);
    }

    private TrainCarriage selectByUnique(String trainCode, Integer idx) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.createCriteria().andTrainCodeEqualTo(trainCode).andIndexEqualTo(idx);
        List<TrainCarriage> carriageList = trainCarriageMapper.selectByExample(trainCarriageExample);
        if (CollUtil.isNotEmpty(carriageList)) {
            return carriageList.get(0);
        } else {
            return null;
        }
    }
}
