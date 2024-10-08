package org.clone.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.clone.business.domain.Train;
import org.clone.business.domain.TrainExample;
import org.clone.business.mapper.TrainMapper;
import org.clone.business.req.TrainQueryReq;
import org.clone.business.req.TrainSaveReq;
import org.clone.business.resp.TrainQueryResp;
import org.clone.common.exception.BusinessException;
import org.clone.common.response.PageRes;
import org.clone.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.clone.common.exception.BusinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR;

@Service
public class TrainService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);

    @Resource
    private TrainMapper trainMapper;

    public void save(TrainSaveReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        if (ObjectUtil.isNull(train.getId())) {

            Train trainDB = selectByUnique(req.getCode());
            if (ObjectUtil.isNotEmpty(trainDB)) {
                throw new BusinessException(BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }
            
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }

    public PageRes<TrainQueryResp> queryList(TrainQueryReq req) {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("id desc");
        TrainExample.Criteria criteria = trainExample.createCriteria();

        LOG.info("Pages: {}", req.getPage());
        LOG.info("Total: {}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Train> trainList = trainMapper.selectByExample(trainExample);

        PageInfo<Train> pageInfo = new PageInfo<>(trainList);
        LOG.info("Rows: {}", pageInfo.getTotal());
        LOG.info("Pages: {}", pageInfo.getPages());

        List<TrainQueryResp> list = BeanUtil.copyToList(trainList, TrainQueryResp.class);

        PageRes<TrainQueryResp> pageResp = new PageRes<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    @Transactional //打开事务mybatis一级缓存的事务缓存（session）才生效
    public List<TrainQueryResp> queryAll() {
        List<Train> trainList = selectAll();
        return BeanUtil.copyToList(trainList, TrainQueryResp.class);
    }

    public List<Train> selectAll() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code asc");
        return trainMapper.selectByExample(trainExample);
    }

    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    private Train selectByUnique(String code) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria().andCodeEqualTo(code);
        List<Train> list = trainMapper.selectByExample(trainExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
