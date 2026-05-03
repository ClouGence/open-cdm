package com.clougence.adapter.polar.porpg;

/**
 * Postgres "match_option"
 * @version : 2021-05-17
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PolarDBPgForeignMatchOption {

    None("NONE"),
    Full("FULL"),
    Partial("PARTIAL"),;

    private final String typeName;

    PolarDBPgForeignMatchOption(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDBPgForeignMatchOption valueOfCode(String code) {
        for (PolarDBPgForeignMatchOption foreignKeyRule : PolarDBPgForeignMatchOption.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported postgres foreignMatchOption " + code);
    }
}
