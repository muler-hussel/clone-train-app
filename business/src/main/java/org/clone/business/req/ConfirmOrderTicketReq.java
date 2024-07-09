package org.clone.business.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConfirmOrderTicketReq {

    @NotNull(message = "[passengerId] cannot be null")
    private Long passengerId;

    @NotBlank(message = "[passengerType] cannot be empty")
    private String passengerType;

    @NotBlank(message = "[passengerName] cannot be empty")
    private String passengerName;

    @NotBlank(message = "[SeatTypeCode] cannot be empty")
    private String SeatTypeCode;

    private String seat;

    @Override
    public String toString() {
        return "ConfirmOrderTicketReq{" +
                "passengerId=" + passengerId +
                ", passengerType='" + passengerType + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", SeatTypeCode='" + SeatTypeCode + '\'' +
                ", seat='" + seat + '\'' +
                '}';
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getSeatTypeCode() {
        return SeatTypeCode;
    }

    public void setSeatTypeCode(String seatTypeCode) {
        SeatTypeCode = seatTypeCode;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
