package com.clougence.adapter.tidb;

/**
 * TiDB ForeignKey Rule
 * @version : 2021-09-27
 * @author mode
 */
public enum TiDBForeignKeyRule {

    NoAction("NO ACTION"),
    Restrict("RESTRICT"),
    Cascade("CASCADE"),
    SetNull("SET NULL"),
    SetDefault("SET DEFAULT");

    private final String typeName;

    TiDBForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static TiDBForeignKeyRule valueOfCode(String code) {
        for (TiDBForeignKeyRule foreignKeyRule : TiDBForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported tidb foreignKeyRule " + code);
    }
}
