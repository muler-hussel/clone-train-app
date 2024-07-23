package org.clone.business.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DailyTrainStationQueryAllReq {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "【Date】 cannot be null")
    private Date date;

    @NotBlank(message = "【train code】 cannot be blank")
    private String trainCode;

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
