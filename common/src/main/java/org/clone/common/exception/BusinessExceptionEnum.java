package org.clone.common.exception;

public enum BusinessExceptionEnum {

    MEMBER_MOBILE_EXIST("Phone is registered"),
    MEMBER_MOBILE_NOT_EXIST("Phone does not exist"),
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
