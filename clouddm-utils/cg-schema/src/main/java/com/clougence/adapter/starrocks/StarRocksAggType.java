package com.clougence.adapter.starrocks;

/**
 * @author wanshao
 */
public enum StarRocksAggType {

    SUM("SUM"),
    MAX("MAX"),
    MIN("MIN"),
    REPLACE("REPLACE"),
    HLL_UNION("HLL_UNION"), //（仅用于HLL列，为HLL独有的聚合方式)。
    BITMAP_UNION("BITMAP_UNION"), //（仅用于 BITMAP 列，为 BITMAP 独有的聚合方式)。
    REPLACE_IF_NOT_NULL("REPLACE_IF_NOT_NULL"),;

    private final String typeName;

    StarRocksAggType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static StarRocksAggType valueOfCode(String code) {
        for (StarRocksAggType constraintType : StarRocksAggType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
