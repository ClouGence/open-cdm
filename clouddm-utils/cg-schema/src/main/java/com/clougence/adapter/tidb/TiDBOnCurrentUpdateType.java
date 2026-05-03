package com.clougence.adapter.tidb;

/**
 * TiDB default on xxxx
 * @version : 2021-09-27
 * @author mode
 */
public enum TiDBOnCurrentUpdateType {

    CurrentTimestamp("current_timestamp"),
    CurrentDate("current_date"),
    CurrentTime("current_time");

    private final String typeName;

    TiDBOnCurrentUpdateType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static TiDBOnCurrentUpdateType valueOfCode(String code) {
        for (TiDBOnCurrentUpdateType currentType : TiDBOnCurrentUpdateType.values()) {
            if (currentType.typeName.equalsIgnoreCase(code)) {
                return currentType;
            }
        }
        return null;
    }
}
