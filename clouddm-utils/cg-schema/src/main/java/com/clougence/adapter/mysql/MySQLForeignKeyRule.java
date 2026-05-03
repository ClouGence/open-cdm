package com.clougence.adapter.mysql;

/**
 * MySQL 外建约束的及联更新策略
 * @version : 2021-03-30
 * @author 赵永春 (zyc@hasor.net)
 */
public enum MySQLForeignKeyRule {

    NoAction("NO ACTION"),
    Restrict("RESTRICT"),
    Cascade("CASCADE"),
    SetNull("SET NULL"),
    SetDefault("SET DEFAULT");

    private final String typeName;

    MySQLForeignKeyRule(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static MySQLForeignKeyRule valueOfCode(String code) {
        for (MySQLForeignKeyRule foreignKeyRule : MySQLForeignKeyRule.values()) {
            if (foreignKeyRule.typeName.equalsIgnoreCase(code)) {
                return foreignKeyRule;
            }
        }
        throw new UnsupportedOperationException("Unsupported mysql foreignKeyRule " + code);
    }
}
