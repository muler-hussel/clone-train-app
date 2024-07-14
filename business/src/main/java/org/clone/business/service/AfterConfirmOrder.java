package org.clone.business.service;

import jakarta.annotation.Resource;
import org.clone.business.domain.ConfirmOrder;
import org.clone.business.domain.DailyTrainSeat;
import org.clone.business.domain.DailyTrainTicket;
import org.clone.business.enums.ConfirmOrderStatusEnum;
import org.clone.business.feign.MemberFeign;
import org.clone.business.mapper.ConfirmOrderMapper;
import org.clone.business.mapper.DailyTrainSeatMapper;
import org.clone.business.mapper.cust.DailyTrainTicketMapperCust;
import org.clone.business.req.ConfirmOrderTicketReq;
import org.clone.common.context.LoginMemberContext;
import org.clone.common.request.MemberTicketReq;
import org.clone.common.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
//同一个类方法间的调用，事务不生效。必须新建一个类
public class AfterConfirmOrder {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private DailyTrainTicketMapperCust dailyTrainTicketMapperCust;

    @Resource
    private MemberFeign memberFeign;

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    // 选中座位后事务处理：
    // 座位表修改售卖情况sell；
    // 余票详情表修改余票；
    // 为会员增加购票记录
    // 更新确认订单为成功
    //@Transactional
    //@GlobalTransactional
    public void afterConfirm(DailyTrainTicket dailyTrainTicket,
                             List<DailyTrainSeat> finalSeatList,
                             List<ConfirmOrderTicketReq> tickets,
                             ConfirmOrder confirmOrder) throws Exception {
        //LOG.info("seata ID: {}", RootContext.getXID());
        for (int j = 0; j < finalSeatList.size(); j++) {
            DailyTrainSeat dailyTrainSeat = finalSeatList.get(j);
            DailyTrainSeat seatForUpdate = new DailyTrainSeat();
            seatForUpdate.setId(dailyTrainSeat.getId());
            seatForUpdate.setSelling(dailyTrainSeat.getSelling());
            seatForUpdate.setUpdateTime(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatForUpdate);

            //计算影响库存。包括没卖出过票的，和本次购买区间有交集的区间
            //原售： 001000001
            //购买： 000011100
            //新售： 001011101
            //影响： XXX11111X
            Integer startIndex = dailyTrainTicket.getStartIndex();
            Integer endIndex = dailyTrainTicket.getEndIndex();
            char[] chars = seatForUpdate.getSelling().toCharArray();
            Integer maxStartIndex = endIndex - 1;
            Integer minEndIndex = startIndex + 1;
            Integer minStartIndex = 0;
            for (int i = startIndex - 1; i >= 0; i--) {
                char aChar = chars[i];
                if (aChar == '1') {
                    minStartIndex = i + 1;
                    break;
                }
            }
            Integer maxEndIndex = seatForUpdate.getSelling().length();
            for (int i = endIndex; i < seatForUpdate.getSelling().length(); i++) {
                if (chars[i] == '1') {
                    maxEndIndex = i;
                    break;
                }
            }
            LOG.info("stations:" + minStartIndex + maxStartIndex + minEndIndex + maxEndIndex);
            dailyTrainTicketMapperCust.updateCountBySelling(dailyTrainSeat.getDate(),
                    dailyTrainSeat.getTrainCode(),
                    dailyTrainSeat.getSeatType(),
                    minStartIndex, maxStartIndex,
                    minEndIndex, maxEndIndex);
            //调用会员服务接口，为会员增加车票
            MemberTicketReq memberTicketReq = new MemberTicketReq();
            memberTicketReq.setMemberId(LoginMemberContext.getId());
            memberTicketReq.setPassengerId(tickets.get(j).getPassengerId());
            memberTicketReq.setPassengerName(tickets.get(j).getPassengerName());
            memberTicketReq.setTrainDate(dailyTrainTicket.getDate());
            memberTicketReq.setTrainCode(dailyTrainTicket.getTrainCode());
            memberTicketReq.setCarriageIndex(dailyTrainSeat.getCarriageIndex());
            memberTicketReq.setSeatRow(dailyTrainSeat.getRow());
            memberTicketReq.setSeatCol(dailyTrainSeat.getCol());
            memberTicketReq.setDeparture(dailyTrainTicket.getStart());
            memberTicketReq.setDepartureTime(dailyTrainTicket.getStartTime());
            memberTicketReq.setDestination(dailyTrainTicket.getEnd());
            memberTicketReq.setArrivalTime(dailyTrainTicket.getEndTime());
            memberTicketReq.setSeatType(dailyTrainSeat.getSeatType());
            CommonResponse<Object> commonResponse = memberFeign.save(memberTicketReq);

            //更新订单状态为成功
            ConfirmOrder confirmOrderForUpdate = new ConfirmOrder();
            confirmOrderForUpdate.setId(confirmOrder.getId());
            confirmOrderForUpdate.setUpdateTime(new Date());
            confirmOrderForUpdate.setStatus(ConfirmOrderStatusEnum.SUCCESS.getCode());
            confirmOrderMapper.updateByPrimaryKeySelective(confirmOrderForUpdate);

             //模拟调用方出现异常
             //Thread.sleep(10000);
//             if (1 == 1) {
//                 throw new Exception("测试异常");
//             }
        }
    }

}
