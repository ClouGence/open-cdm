package com.clougence.adapter.db2;

public enum Db2ForeignKeyRule {

    NoAction("A"),
    Cascade("C"),
    SetNull("N"),
    Restrict("R"),
    SetDefault("D");

    private final String typeName;

    Db2ForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static Db2ForeignKeyRule valueOfCode(String code) {
        for (Db2ForeignKeyRule foreignKeyRule : Db2ForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }

        throw new UnsupportedOperationException("Unsupported Db2 foreignKeyRule " + code);
    }
}
