package com.clougence.adapter.ob.obfororacle;

/**
 * @version : 2021-05-08
 * @author 赵永春 (zyc@hasor.net)
 */
public enum ObForOracleForeignKeyRule {

    NoAction("NO ACTION"),
    Cascade("CASCADE"),
    SetNull("SET NULL");

    private final String typeName;

    ObForOracleForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static ObForOracleForeignKeyRule valueOfCode(String code) {
        for (ObForOracleForeignKeyRule foreignKeyRule : ObForOracleForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported oracle foreignKeyRule " + code);
    }
}
