package com.clougence.schema.umi.special.rdb;

/**
 * @author mode 2021/5/21 19:56
 */
public enum RdbIndexType {

    Unique("Unique"),
    Normal("Normal"),
    Other("Other"),;

    private final String typeName;

    RdbIndexType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static RdbIndexType valueOfCode(String code) {
        for (RdbIndexType umiIndexType : RdbIndexType.values()) {
            if (umiIndexType.typeName.equals(code)) {
                return umiIndexType;
            }
        }
        return null;
    }
}
