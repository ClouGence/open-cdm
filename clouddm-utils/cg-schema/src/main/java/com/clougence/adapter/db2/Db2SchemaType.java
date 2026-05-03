package com.clougence.adapter.db2;

public enum Db2SchemaType {

    USER("U"),
    SYSTEM("S");

    private final String typeName;

    Db2SchemaType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static Db2SchemaType valueOfCode(String code) {
        for (Db2SchemaType tableType : Db2SchemaType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code.trim())) {
                return tableType;
            }
        }
        return null;
    }
}
