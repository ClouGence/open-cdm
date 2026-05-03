package com.clougence.adapter.polar.porpg;

/**
 * Postgres 外建约束的及联更新策略
 * @version : 2021-05-17
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBPgForeignKeyRule {

    NoAction("NO ACTION"),
    Restrict("RESTRICT"),
    SetDefault("SET DEFAULT"),
    Cascade("CASCADE"),
    SetNull("SET NULL");

    private final String typeName;

    PolarDBPgForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDBPgForeignKeyRule valueOfCode(String code) {
        for (PolarDBPgForeignKeyRule foreignKeyRule : PolarDBPgForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported postgres foreignKeyRule " + code);
    }
}
