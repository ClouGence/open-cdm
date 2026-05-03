package com.clougence.adapter.oracle;

/**
 * Oracle 外建约束的及联更新策略
 * @version : 2021-05-08
 * @author 赵永春 (zyc@hasor.net)
 */
public enum OracleForeignKeyRule {

    NoAction("NO ACTION"),
    Cascade("CASCADE"),
    SetNull("SET NULL");

    private final String typeName;

    OracleForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static OracleForeignKeyRule valueOfCode(String code) {
        for (OracleForeignKeyRule foreignKeyRule : OracleForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported oracle foreignKeyRule " + code);
    }
}
