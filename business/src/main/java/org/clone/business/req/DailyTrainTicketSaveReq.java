package org.clone.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public class DailyTrainTicketSaveReq {

    /**
     * id
     */
    private Long id;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【date】cannot be null")
    private Date date;

    /**
     * 车次编号
     */
    @NotBlank(message = "【trainCode】cannot be blank")
    private String trainCode;

    /**
     * 出发站
     */
    @NotBlank(message = "【departure】cannot be blank")
    private String start;

    /**
     * 出发站拼音
     */
    @NotBlank(message = "【departurePinyin】cannot be blank")
    private String startPinyin;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【departureTime】cannot be null")
    private Date startTime;

    /**
     * 出发站序|本站是整个车次的第几站
     */
    @NotNull(message = "【departureIndex】cannot be null")
    private Integer startIndex;

    /**
     * 到达站
     */
    @NotBlank(message = "【arrival】cannot be blank")
    private String end;

    /**
     * 到达站拼音
     */
    @NotBlank(message = "【arrivalPinyin】cannot be blank")
    private String endPinyin;

    /**
     * 到站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【arrivalTime】cannot be null")
    private Date endTime;

    /**
     * 到站站序|本站是整个车次的第几站
     */
    @NotNull(message = "【arrivalIndex】cannot be null")
    private Integer endIndex;

    /**
     * 一等座余票
     */
    @NotNull(message = "【fc】cannot be null")
    private Integer fc;

    /**
     * 一等座票价
     */
    @NotNull(message = "【fcPrice】cannot be null")
    private BigDecimal fcPrice;

    /**
     * 二等座余票
     */
    @NotNull(message = "【sc】cannot be null")
    private Integer sc;

    /**
     * 二等座票价
     */
    @NotNull(message = "【scPrice】cannot be null")
    private BigDecimal scPrice;

    /**
     * 软卧余票
     */
    @NotNull(message = "【ss】cannot be null")
    private Integer ss;

    /**
     * 软卧票价
     */
    @NotNull(message = "【ssPrice】cannot be null")
    private BigDecimal ssPrice;

    /**
     * 硬卧余票
     */
    @NotNull(message = "【hs】cannot be null")
    private Integer hs;

    /**
     * 硬卧票价
     */
    @NotNull(message = "【hsPrice】cannot be null")
    private BigDecimal hsPrice;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStartPinyin() {
        return startPinyin;
    }

    public void setStartPinyin(String startPinyin) {
        this.startPinyin = startPinyin;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEndPinyin() {
        return endPinyin;
    }

    public void setEndPinyin(String endPinyin) {
        this.endPinyin = endPinyin;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Integer getFc() {
        return fc;
    }

    public void setFc(Integer fc) {
        this.fc = fc;
    }

    public BigDecimal getFcPrice() {
        return fcPrice;
    }

    public void setFcPrice(BigDecimal fcPrice) {
        this.fcPrice = fcPrice;
    }

    public Integer getSc() {
        return sc;
    }

    public void setSc(Integer sc) {
        this.sc = sc;
    }

    public BigDecimal getScPrice() {
        return scPrice;
    }

    public void setScPrice(BigDecimal scPrice) {
        this.scPrice = scPrice;
    }

    public Integer getSs() {
        return ss;
    }

    public void setSs(Integer ss) {
        this.ss = ss;
    }

    public BigDecimal getSsPrice() {
        return ssPrice;
    }

    public void setSsPrice(BigDecimal ssPrice) {
        this.ssPrice = ssPrice;
    }

    public Integer getHs() {
        return hs;
    }

    public void setHs(Integer hs) {
        this.hs = hs;
    }

    public BigDecimal getHsPrice() {
        return hsPrice;
    }

    public void setHsPrice(BigDecimal hsPrice) {
        this.hsPrice = hsPrice;
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
        sb.append(", date=").append(date);
        sb.append(", trainCode=").append(trainCode);
        sb.append(", start=").append(start);
        sb.append(", startPinyin=").append(startPinyin);
        sb.append(", startTime=").append(startTime);
        sb.append(", startIndex=").append(startIndex);
        sb.append(", end=").append(end);
        sb.append(", endPinyin=").append(endPinyin);
        sb.append(", endTime=").append(endTime);
        sb.append(", endIndex=").append(endIndex);
        sb.append(", fc=").append(fc);
        sb.append(", fcPrice=").append(fcPrice);
        sb.append(", sc=").append(sc);
        sb.append(", scPrice=").append(scPrice);
        sb.append(", ss=").append(ss);
        sb.append(", ssPrice=").append(ssPrice);
        sb.append(", hs=").append(hs);
        sb.append(", hsPrice=").append(hsPrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}
