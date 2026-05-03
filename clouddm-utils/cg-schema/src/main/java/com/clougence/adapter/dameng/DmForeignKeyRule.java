package com.clougence.adapter.dameng;

/**
 * 达梦 外建约束的及联更新策略
 * @version : 2021-05-08
 * @author 赵永春 (zyc@hasor.net)
 */
public enum DmForeignKeyRule {

    NoAction("NO ACTION"),
    Cascade("CASCADE"),
    SetNull("SET NULL");

    private final String typeName;

    DmForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DmForeignKeyRule valueOfCode(String code) {
        for (DmForeignKeyRule foreignKeyRule : DmForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported oracle foreignKeyRule " + code);
    }
}
