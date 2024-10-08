package org.clone.business.req;

import org.clone.common.request.PageReq;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DailyTrainCarriageQueryReq extends PageReq {

    private String trainCode;

    @Override
    public String toString() {
        return "DailyTrainCarriageQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                ", date=" + date +
                "} " + super.toString();
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

}
