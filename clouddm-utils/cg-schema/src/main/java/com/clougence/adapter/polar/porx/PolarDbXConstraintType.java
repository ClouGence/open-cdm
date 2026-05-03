package com.clougence.adapter.polar.porx;

public enum PolarDbXConstraintType {

    PrimaryKey("PRIMARY KEY"),

    Unique("UNIQUE");

    private final String typeName;

    PolarDbXConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDbXConstraintType valueOfCode(String code) {
        for (PolarDbXConstraintType constraintType : PolarDbXConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
