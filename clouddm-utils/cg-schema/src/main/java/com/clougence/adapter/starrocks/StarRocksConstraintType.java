package com.clougence.adapter.starrocks;

/**
 * @author wanshao
 */
public enum StarRocksConstraintType {

    /**
     * primary key
     */
    PrimaryKey("PRIMARY KEY"),
    /**
     * unique key
     */
    Unique("UNIQUE"),;

    private final String typeName;

    StarRocksConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static StarRocksConstraintType valueOfCode(String code) {
        for (StarRocksConstraintType constraintType : StarRocksConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
