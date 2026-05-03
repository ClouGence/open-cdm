package com.clougence.adapter.polar.pormy;

/**
 * MySQL 外建约束的及联更新策略
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBMyForeignKeyRule {

    NoAction("NO ACTION"),
    Restrict("RESTRICT"),
    Cascade("CASCADE"),
    SetNull("SET NULL"),
    SetDefault("SET DEFAULT");

    private final String typeName;

    PolarDBMyForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDBMyForeignKeyRule valueOfCode(String code) {
        for (PolarDBMyForeignKeyRule foreignKeyRule : PolarDBMyForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported mysql foreignKeyRule " + code);
    }
}
