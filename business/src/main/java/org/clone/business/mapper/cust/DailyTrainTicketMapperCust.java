package org.clone.business.mapper.cust;

import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface DailyTrainTicketMapperCust {

    void updateCountBySelling (Date date, String trainCode, String seatTypeCode,
                               Integer minStartIndex, Integer maxStartIndex,
                               Integer minEndIndex, Integer maxEndIndex);
}
