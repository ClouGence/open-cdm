package com.clougence.adapter.doris;

/**
 * @author wanshao
 */
public enum DorisConstraintType {

    /**
     * primary key
     */
    PrimaryKey("PRIMARY KEY"),
    /**
     * unique key
     */
    Unique("UNIQUE"),;

    private final String typeName;

    DorisConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DorisConstraintType valueOfCode(String code) {
        for (DorisConstraintType constraintType : DorisConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
