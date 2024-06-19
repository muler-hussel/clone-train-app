package org.clone.exception;

public enum BusinessExceptionEnum {

    MEMBER_MOBILE_EXIST("Phone is registered");
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
