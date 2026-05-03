package com.clougence.schema.editor.domain;

public enum EConstraintType {

    Check("Check"),
    Unique("Unique");

    private final String typeName;

    EConstraintType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static EConstraintType valueOfCode(String code) {
        for (EConstraintType type : EConstraintType.values()) {
            if (type.typeName.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
