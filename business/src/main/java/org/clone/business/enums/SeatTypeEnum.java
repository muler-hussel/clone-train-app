package org.clone.business.enums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public enum SeatTypeEnum {

    FC("1", "First Class", new BigDecimal("0.4")),
    SC("2", "Second Class", new BigDecimal("0.3")),
    SS("3", "Soft Sleeper", new BigDecimal("0.6")),
    HS("4", "Hard Sleeper", new BigDecimal("0.5"));

    private String code;

    private String desc;

    /**
     * 基础票价 N元/公里，0.4即为0.4元/公里
     */
    private BigDecimal price;

    SeatTypeEnum(String code, String desc, BigDecimal price) {
        this.code = code;
        this.desc = desc;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static List<HashMap<String,String>> getEnumList() {
        List<HashMap<String, String>> list = new ArrayList<>();
        for (SeatTypeEnum anEnum : EnumSet.allOf(SeatTypeEnum.class)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code",anEnum.code);
            map.put("desc",anEnum.desc);
            list.add(map);
        }
        return list;
    }

    public static SeatTypeEnum getEnumByCode(String code) {
        for (SeatTypeEnum enums : SeatTypeEnum.values()) {
            if (enums.getCode().equalsIgnoreCase(code)) {
                return enums;
            }
        }
        return null;
    }
}
