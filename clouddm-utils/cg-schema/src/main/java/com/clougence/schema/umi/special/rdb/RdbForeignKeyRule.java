package com.clougence.schema.umi.special.rdb;

import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/5/21 19:56
 */
public enum RdbForeignKeyRule {

    NoAction("NO ACTION"),
    Restrict("RESTRICT"),
    SetDefault("SET DEFAULT"),
    Cascade("CASCADE"),
    SetNull("SET NULL");

    private final String typeName;

    RdbForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static RdbForeignKeyRule valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (RdbForeignKeyRule foreignKeyRule : RdbForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equals(code)) {
                return foreignKeyRule;
            }
        }
        return null;
    }
}
