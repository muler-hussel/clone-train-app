package org.clone.business.domain;

import java.math.BigDecimal;
import java.util.Date;

public class DailyTrainTicket {
    private Long id;

    private Date date;

    private String trainCode;

    private String start;

    private String startPinyin;

    private Date startTime;

    private Integer startIndex;

    private String end;

    private String endPinyin;

    private Date endTime;

    private Integer endIndex;

    private Integer fc;

    private BigDecimal fcPrice;

    private Integer sc;

    private BigDecimal scPrice;

    private Integer ss;

    private BigDecimal ssPrice;

    private Integer hs;

    private BigDecimal hsPrice;

    private Date createTime;

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