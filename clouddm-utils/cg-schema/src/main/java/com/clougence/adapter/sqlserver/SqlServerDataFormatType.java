package com.clougence.adapter.sqlserver;

public enum SqlServerDataFormatType {

    YMD("YMD"),
    MYD("MYD"),
    MDY("MDY"),
    YDM("YDM"),
    DYM("DYM"),
    DMY("DMY"),;

    private final String typeName;

    SqlServerDataFormatType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static SqlServerDataFormatType valueOfCode(String code) {
        for (SqlServerDataFormatType constraintType : SqlServerDataFormatType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
