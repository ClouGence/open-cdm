package com.clougence.adapter.ob.obformysql;

/**
 * @author wanshao
 */
public enum ObForMySQLConstraintType {

    /**
     * primary key
     */
    PrimaryKey("PRIMARY KEY"),
    /**
     * unique key
     */
    Unique("UNIQUE"),;

    private final String typeName;

    ObForMySQLConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static ObForMySQLConstraintType valueOfCode(String code) {
        for (ObForMySQLConstraintType constraintType : ObForMySQLConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
