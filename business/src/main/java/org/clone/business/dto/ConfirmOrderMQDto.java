package org.clone.business.dto;

import java.util.Date;

public class ConfirmOrderMQDto {

    private String logId;

    private Date date;

    private String trainCode;

    @Override
    public String toString() {
        return "ConfirmOrderMQDto{" +
                "logId='" + logId + '\'' +
                ", date=" + date +
                ", trainCode='" + trainCode + '\'' +
                '}';
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }
}
