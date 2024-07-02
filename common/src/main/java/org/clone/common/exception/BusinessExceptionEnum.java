package org.clone.common.exception;

public enum BusinessExceptionEnum {

    MEMBER_MOBILE_EXIST("Phone is registered"),
    MEMBER_MOBILE_NOT_EXIST("Phone does not exist"),
    MEMBER_STATION_NAME_UNIQUE_ERROR("Station already exists"),
    MEMBER_TRAIN_CODE_UNIQUE_ERROR("Train code already exists"),
    MEMBER_TRAIN_STATION_NAME_UNIQUE_ERROR("Station name already exists"),
    MEMBER_TRAIN_STATION_INDEX_UNIQUE_ERROR("Station index already exists"),
    MEMBER_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR("Carriage already exists"),
    MEMBER_MOBILE_CODE_ERROR("Code is error");
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
