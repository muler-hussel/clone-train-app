package org.clone.member.req;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketSaveReq {

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    @NotNull(message = "【memberId】cannot be null")
    private Long memberId;

    /**
     * 
     */
    @NotNull(message = "【passengerId】cannot be null")
    private Long passengerId;

    /**
     * 
     */
    private String passengerName;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【trainDate】cannot be null")
    private Date trainDate;

    /**
     * 
     */
    @NotBlank(message = "【trainCode】cannot be blank")
    private String trainCode;

    /**
     * 
     */
    @NotNull(message = "【carriageIndex】cannot be null")
    private Integer carriageIndex;

    /**
     * 
     */
    @NotBlank(message = "【seatRow】cannot be blank")
    private String seatRow;

    /**
     * 
     */
    @NotBlank(message = "【seatCol】cannot be blank")
    private String seatCol;

    /**
     * 
     */
    @NotBlank(message = "【departure】cannot be blank")
    private String departure;

    /**
     * 
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【departureTime】cannot be null")
    private Date departureTime;

    /**
     * 
     */
    @NotBlank(message = "【destination】cannot be blank")
    private String destination;

    /**
     * 
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【arrivalTime】cannot be null")
    private Date arrivalTime;

    /**
     * 
     */
    @NotBlank(message = "【seatType】cannot be blank")
    private String seatType;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Date getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(Date trainDate) {
        this.trainDate = trainDate;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public Integer getCarriageIndex() {
        return carriageIndex;
    }

    public void setCarriageIndex(Integer carriageIndex) {
        this.carriageIndex = carriageIndex;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public String getSeatCol() {
        return seatCol;
    }

    public void setSeatCol(String seatCol) {
        this.seatCol = seatCol;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", memberId=").append(memberId);
        sb.append(", passengerId=").append(passengerId);
        sb.append(", passengerName=").append(passengerName);
        sb.append(", trainDate=").append(trainDate);
        sb.append(", trainCode=").append(trainCode);
        sb.append(", carriageIndex=").append(carriageIndex);
        sb.append(", seatRow=").append(seatRow);
        sb.append(", seatCol=").append(seatCol);
        sb.append(", departure=").append(departure);
        sb.append(", departureTime=").append(departureTime);
        sb.append(", destination=").append(destination);
        sb.append(", arrivalTime=").append(arrivalTime);
        sb.append(", seatType=").append(seatType);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}
