package org.clone.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.clone.business.domain.*;
import org.clone.common.response.PageRes;
import org.clone.common.util.SnowUtil;
import org.clone.business.mapper.DailyTrainSeatMapper;
import org.clone.business.req.DailyTrainSeatQueryReq;
import org.clone.business.req.DailyTrainSeatSaveReq;
import org.clone.business.resp.DailyTrainSeatQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainSeatService.class);

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private TrainSeatService trainSeatService;

    @Resource
    private TrainStationService trainStationService;

    public void save(DailyTrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        if (ObjectUtil.isNull(dailyTrainSeat.getId())) {
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.updateByPrimaryKey(dailyTrainSeat);
        }
    }

    public PageRes<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQueryReq req) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();

        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        LOG.info("Pages: {}", req.getPage());
        LOG.info("Total: {}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);

        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeatList);
        LOG.info("Rows: {}", pageInfo.getTotal());
        LOG.info("Pages: {}", pageInfo.getPages());

        List<DailyTrainSeatQueryResp> list = BeanUtil.copyToList(dailyTrainSeatList, DailyTrainSeatQueryResp.class);

        PageRes<DailyTrainSeatQueryResp> pageResp = new PageRes<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void genDaily(Date date, String trainCode) {
        LOG.info("Generate seat data of {} on {}", trainCode, DateUtil.formatDate(date));

        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainSeatMapper.deleteByExample(dailyTrainSeatExample);

        List<TrainStation> stationList = trainStationService.selectByTrainCode(trainCode);
        String sell = StrUtil.fillBefore("", '0', stationList.size() - 1);

        List<TrainSeat> trainSeats = trainSeatService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(trainSeats)) {
            LOG.info("No data");
            return;
        }
        for (TrainSeat trainSeat : trainSeats) {
            //生成该车次数据
            DateTime now = DateTime.now();
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId()); //每天生成的同一批车次id不能相同
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setSelling(sell);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        }
    }
    public int countSeat(Date date, String trainCode) {return countSeat(date, trainCode, null);}
    public int countSeat(Date date, String trainCode, String seatType) {
        DailyTrainSeatExample example = new DailyTrainSeatExample();
        DailyTrainSeatExample.Criteria criteria = example.createCriteria();
        criteria.andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        if (StrUtil.isNotBlank(seatType)) {
            criteria.andSeatTypeEqualTo(seatType);
        }
        long count = dailyTrainSeatMapper.countByExample(example);
        if (count == 0L) {
            return -1;
        }
        return (int) count;
    }

    public List<DailyTrainSeat> selectByCarriage(Date date, String trainCode, Integer carriageIndex) {
        DailyTrainSeatExample example = new DailyTrainSeatExample();
        example.setOrderByClause("carriage_seat_index asc");
        example.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode).andCarriageIndexEqualTo(carriageIndex);
        return dailyTrainSeatMapper.selectByExample(example);
    }
}
