package com.clougence.adapter.polar.pormy;

/**
 * MySQL default on xxxx
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBMyOnCurrentUpdateType {

    CurrentTimestamp("current_timestamp"),
    CurrentDate("current_date"),
    CurrentTime("current_time");

    private final String typeName;

    PolarDBMyOnCurrentUpdateType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDBMyOnCurrentUpdateType valueOfCode(String code) {
        for (PolarDBMyOnCurrentUpdateType currentType : PolarDBMyOnCurrentUpdateType.values()) {
            if (currentType.typeName.equalsIgnoreCase(code)) {
                return currentType;
            }
        }
        return null;
    }
}
