package com.clougence.adapter.hana;

public enum HanaForeignKeyRule {

    NoAction("NO ACTION"),
    Restrict("RESTRICT"),
    Cascade("CASCADE"),
    SetNull("SET NULL"),
    SetDefault("SET DEFAULT");

    private final String typeName;

    HanaForeignKeyRule(String typeName) {
        this.typeName = typeName;
    }
    public static HanaForeignKeyRule valueOfCode(String code) {
        for (HanaForeignKeyRule foreignKeyRule : HanaForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported mysql foreignKeyRule " + code);
    }

    public String getTypeName() {
        return typeName;
    }
}
