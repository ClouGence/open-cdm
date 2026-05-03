package com.clougence.adapter.gauss;


public enum GaussDBForeignKeyRule {

    NoAction("NO ACTION"),
    Restrict("RESTRICT"),
    SetDefault("SET DEFAULT"),
    Cascade("CASCADE"),
    SetNull("SET NULL");

    private final String typeName;

    GaussDBForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static GaussDBForeignKeyRule valueOfCode(String code) {
        for (GaussDBForeignKeyRule foreignKeyRule : GaussDBForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported postgres foreignKeyRule " + code);
    }
}
