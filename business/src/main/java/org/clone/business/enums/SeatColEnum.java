package org.clone.business.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum SeatColEnum {

    FC_A("A", "A", "1"),
    FC_C("C", "C", "1"),
    FC_D("D", "D", "1"),
    FC_F("F", "F", "1"),
    SC_A("A", "A", "2"),
    SC_B("B", "B", "2"),
    SC_C("C", "C", "2"),
    SC_D("D", "D", "2"),
    SC_F("F", "F", "2"),
    HS_U("Upper", "Upper", "4"),
    HS_M("Middle", "Middle", "4"),
    HS_D("Down", "Down", "4"),
    SS_U("Upper", "Upper", "3"),
    SS_D("Down", "Down", "3");

    private String code;

    private String desc;

    /**
     * 对应SeatTypeEnum.code
     */
    private String type;

    SeatColEnum(String code, String desc, String type) {
        this.code = code;
        this.desc = desc;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 根据车箱的座位类型，筛选出所有的列，比如车箱类型是一等座，则筛选出columnList={ACDF}
     */
    public static List<SeatColEnum> getColsByType(String seatType) {
        List<SeatColEnum> colList = new ArrayList<>();
        EnumSet<SeatColEnum> seatColEnums = EnumSet.allOf(SeatColEnum.class);
        for (SeatColEnum anEnum : seatColEnums) {
            if (seatType.equals(anEnum.getType())) {
                colList.add(anEnum);
            }
        }
        return colList;
    }

}
