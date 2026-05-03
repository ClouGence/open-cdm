package com.clougence.schema.umi.special.rdb;

public enum RdbParamMode {

    IN("IN"),
    OUT("OUT"),
    INOUT("INOUT"),
    // for pg;
    VARIADIC("VARIADIC"),
    // for DB2
    ResultBeforeCasting("R"),
    ResultAfterCasting("C"),
    AggregationStateVar("S"),;

    private final String modeName;

    RdbParamMode(String modeName){
        this.modeName = modeName;
    }

    public String getModeName() { return this.modeName; }

    public static RdbParamMode valueOfCode(String code) {
        for (RdbParamMode paramMode : RdbParamMode.values()) {
            if (paramMode.modeName.equals(code)) {
                return paramMode;
            }
        }
        return null;
    }
}
