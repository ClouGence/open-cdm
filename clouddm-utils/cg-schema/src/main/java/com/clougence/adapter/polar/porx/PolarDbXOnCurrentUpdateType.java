package com.clougence.adapter.polar.porx;

public enum PolarDbXOnCurrentUpdateType {

    CurrentTimestamp("current_timestamp"),
    CurrentDate("current_date"),
    CurrentTime("current_time");

    private final String typeName;

    PolarDbXOnCurrentUpdateType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDbXOnCurrentUpdateType valueOfCode(String code) {
        for (PolarDbXOnCurrentUpdateType currentType : PolarDbXOnCurrentUpdateType.values()) {
            if (currentType.typeName.equalsIgnoreCase(code)) {
                return currentType;
            }
        }
        return null;
    }
}
