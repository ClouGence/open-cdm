package com.clougence.adapter.sqlserver;

import lombok.Getter;

@Getter
public enum SqlServerConstraintType {

    PrimaryKey("PK", "PRIMARY KEY"),
    Unique("UQ", "UNIQUE"),
    ForeignKey("F", "FOREIGN KEY"),;

    private final String typeName;

    private final String fullName;

    SqlServerConstraintType(String typeName, String fullName){
        this.typeName = typeName;
        this.fullName = fullName;
    }

    public static SqlServerConstraintType valueOfCode(String code) {
        for (SqlServerConstraintType constraintType : SqlServerConstraintType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }

    public static SqlServerConstraintType valueOfFullCode(String code) {
        for (SqlServerConstraintType constraintType : SqlServerConstraintType.values()) {
            if (constraintType.fullName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
