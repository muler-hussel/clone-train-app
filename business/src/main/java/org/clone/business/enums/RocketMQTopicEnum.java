package org.clone.business.enums;

public enum RocketMQTopicEnum {
    CONFIRM_ORDER("CONFIRM_ORDER", "confirm orders in queue");

    private String code;
    private String desc;

    @Override
    public String toString() {
        return "RocketMQTopicEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    RocketMQTopicEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
