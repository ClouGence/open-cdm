package com.clougence.adapter.postgre;

/**
 * PostgresSQL 约束类型
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PostgresConstraintType {

    /** 主键 */
    PrimaryKey("PRIMARY KEY"),
    /** 唯一 */
    Unique("UNIQUE"),
    /** 外建 */
    ForeignKey("FOREIGN KEY"),
    //    /** 检查 */
    //    Check("CHECK"),
    ;

    private final String typeName;

    PostgresConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PostgresConstraintType valueOfCode(String code) {
        for (PostgresConstraintType constraintType : PostgresConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        throw new UnsupportedOperationException("Unsupported postgres constraintType " + code);
    }
}
