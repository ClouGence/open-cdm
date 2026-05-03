package com.clougence.adapter.postgre;

/**
 * Postgres "match_option"
 * @version : 2021-05-17
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PostgresForeignMatchOption {

    None("NONE"),
    Full("FULL"),
    Partial("PARTIAL"),;

    private final String typeName;

    PostgresForeignMatchOption(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PostgresForeignMatchOption valueOfCode(String code) {
        for (PostgresForeignMatchOption foreignKeyRule : PostgresForeignMatchOption.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported postgres foreignMatchOption " + code);
    }
}
