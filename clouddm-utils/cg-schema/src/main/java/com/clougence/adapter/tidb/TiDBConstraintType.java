package com.clougence.adapter.tidb;

/**
 * TiDB Constraint Type
 * @version : 2021-09-27
 * @author mode
 */
public enum TiDBConstraintType {

    /** 主键 */
    PrimaryKey("PRIMARY KEY"),
    /** 唯一 */
    Unique("UNIQUE"),
    /** 外建 */
    ForeignKey("FOREIGN KEY"),;

    private final String typeName;

    TiDBConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static TiDBConstraintType valueOfCode(String code) {
        for (TiDBConstraintType constraintType : TiDBConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
