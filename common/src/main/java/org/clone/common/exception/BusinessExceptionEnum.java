package org.clone.common.exception;

public enum BusinessExceptionEnum {

    MEMBER_MOBILE_EXIST("Phone is registered"),
    MEMBER_MOBILE_NOT_EXIST("Phone does not exist"),
    MEMBER_MOBILE_CODE_ERROR("Code is error"),

    BUSINESS_STATION_NAME_UNIQUE_ERROR("Station already exists"),
    BUSINESS_TRAIN_CODE_UNIQUE_ERROR("Train code already exists"),
    BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR("Station name already exists"),
    BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR("Station index already exists"),
    BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR("Carriage already exists"),

    CONFIRM_ORDER_TICKET_COUNT_ERROR("Not enough ticket left"),
    CONFIRM_ORDER_EXCEPTION("Server is busy, please try a moment later"),
    CONFIRM_ORDER_FLOW_EXCEPTION("Please try a moment later"),
    CONFIRM_ORDER_SK_TOKEN_FAIL("Please try 5 minutes later"),
    CONFIRM_ORDER_LOCK_FAIL("Try later");

    private String desc;

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
