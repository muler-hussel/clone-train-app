package org.clone.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.clone.business.domain.DailyTrain;
import org.clone.business.domain.DailyTrainTicket;
import org.clone.business.domain.DailyTrainTicketExample;
import org.clone.business.domain.TrainStation;
import org.clone.business.enums.SeatTypeEnum;
import org.clone.business.enums.TrainTypeEnum;
import org.clone.business.mapper.DailyTrainTicketMapper;
import org.clone.business.req.DailyTrainTicketQueryReq;
import org.clone.business.req.DailyTrainTicketSaveReq;
import org.clone.business.resp.DailyTrainTicketQueryResp;
import org.clone.common.response.PageRes;
import org.clone.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class DailyTrainTicketService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Resource
    private TrainStationService trainStationService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    public void save(DailyTrainTicketSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKey(dailyTrainTicket);
        }
    }

    @CachePut(value = "DailyTrainTicketService.queryList") //查完将结果放入缓存。强制刷新缓存
    public PageRes<DailyTrainTicketQueryResp> queryList2(DailyTrainTicketQueryReq req) {
        return queryList(req);
    }


    //@Cacheable(value = "DailyTrainTicketService.queryList")
    public PageRes<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();

        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }
        if (ObjectUtil.isNotEmpty(req.getStart())) {
            criteria.andStartEqualTo(req.getStart());
        }
        if (ObjectUtil.isNotEmpty(req.getEnd())) {
            criteria.andEndEqualTo(req.getEnd());
        }

        LOG.info("Pages: {}", req.getPage());
        LOG.info("Total: {}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);

        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTicketList);
        LOG.info("Rows: {}", pageInfo.getTotal());
        LOG.info("Pages: {}", pageInfo.getPages());

        List<DailyTrainTicketQueryResp> list = BeanUtil.copyToList(dailyTrainTicketList, DailyTrainTicketQueryResp.class);

        PageRes<DailyTrainTicketQueryResp> pageResp = new PageRes<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void genDaily(DailyTrain dailyTrain, Date date, String trainCode) {
        LOG.info("Generate ticket data of {} on {}", trainCode, DateUtil.formatDate(date));

        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);

        //查询途径的车站信息

        List<TrainStation> stationList = trainStationService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(stationList)) {
            LOG.info("No data.");
            return;
        }

        DateTime now = DateTime.now();
        for (int i = 0; i < stationList.size(); i++) {
            TrainStation trainStationStart = stationList.get(i);
            BigDecimal sumMileage = BigDecimal.ZERO;
            for (int j = i + 1; j < stationList.size(); j++) {
                TrainStation trainStationEnd = stationList.get(j);
                sumMileage = sumMileage.add(trainStationEnd.getMileage());
                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(trainStationStart.getName());
                dailyTrainTicket.setStartTime(trainStationStart.getOutTime());
                dailyTrainTicket.setStartIndex(trainStationStart.getIndex());
                dailyTrainTicket.setStartPinyin(trainStationStart.getNamePinyin());
                dailyTrainTicket.setEnd(trainStationEnd.getName());
                dailyTrainTicket.setEndTime(trainStationEnd.getInTime());
                dailyTrainTicket.setEndIndex(trainStationEnd.getIndex());
                dailyTrainTicket.setEndPinyin(trainStationEnd.getNamePinyin());
                int fc = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.FC.getCode());
                int sc = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.SC.getCode());
                int ss = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.SS.getCode());
                int hs = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.HS.getCode());
                //票价= 里程 * 座位单价 * 车次类型系数
                String type = dailyTrain.getType();
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, type);
                BigDecimal fcPrice = sumMileage.multiply(SeatTypeEnum.FC.getPrice())
                        .multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal scPrice = sumMileage.multiply(SeatTypeEnum.SC.getPrice())
                        .multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ssPrice = sumMileage.multiply(SeatTypeEnum.SS.getPrice())
                        .multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal hsPrice = sumMileage.multiply(SeatTypeEnum.HS.getPrice())
                        .multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                dailyTrainTicket.setFc(fc);
                dailyTrainTicket.setFcPrice(fcPrice);
                dailyTrainTicket.setSc(sc);
                dailyTrainTicket.setScPrice(scPrice);
                dailyTrainTicket.setSs(ss);
                dailyTrainTicket.setSsPrice(ssPrice);
                dailyTrainTicket.setHs(hs);
                dailyTrainTicket.setHsPrice(hsPrice);
                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }
    }

    public DailyTrainTicket selectByUnique(Date date, String trainCode, String departure, String destination) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andStartEqualTo(departure)
                .andEndEqualTo(destination);
        List<DailyTrainTicket> list = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
