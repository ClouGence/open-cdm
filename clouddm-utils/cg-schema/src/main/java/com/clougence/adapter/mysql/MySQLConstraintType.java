package com.clougence.adapter.mysql;

/**
 * MySQL 约束类型
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum MySQLConstraintType {

    /** 主键 */
    PrimaryKey("PRIMARY KEY"),
    /** 唯一 */
    Unique("UNIQUE"),
    /** 外建 */
    ForeignKey("FOREIGN KEY"),;

    private final String typeName;

    MySQLConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static MySQLConstraintType valueOfCode(String code) {
        for (MySQLConstraintType constraintType : MySQLConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
