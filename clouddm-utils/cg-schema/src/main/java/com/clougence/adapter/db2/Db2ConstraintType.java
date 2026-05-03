package com.clougence.adapter.db2;

public enum Db2ConstraintType {

    PrimaryKey("PRIMARY KEY"),
    Unique("Unique"),
    ForeignKey("ForeignKey"),
    Check("Check"),
    FuncDependency("Functional dependency");

    private final String typeName;

    Db2ConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static Db2ConstraintType valueOfCode(String code) {
        for (Db2ConstraintType constraintType : Db2ConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
