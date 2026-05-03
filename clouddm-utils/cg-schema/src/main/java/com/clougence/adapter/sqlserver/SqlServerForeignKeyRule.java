package com.clougence.adapter.sqlserver;

public enum SqlServerForeignKeyRule {

    NoAction("0"),
    Cascade("1"),
    SetNull("2"),
    SetDefault("3");

    private final String typeName;

    SqlServerForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static SqlServerForeignKeyRule valueOfCode(String code) {
        for (SqlServerForeignKeyRule foreignKeyRule : SqlServerForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }

        throw new UnsupportedOperationException("Unsupported SqlServer foreignKeyRule " + code);
    }
}
