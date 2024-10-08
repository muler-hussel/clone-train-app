package org.clone.business.req;

import org.clone.common.request.PageReq;

public class TrainCarriageQueryReq extends PageReq {

    private String trainCode;

    @Override
    public String toString() {
        return "TrainCarriageQueryReq{" +
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
