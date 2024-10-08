package org.clone.business.req;

import org.clone.common.request.PageReq;

public class DailyTrainSeatQueryReq extends PageReq {

    private String trainCode;

    @Override
    public String toString() {
        return "DailyTrainSeatQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

}
