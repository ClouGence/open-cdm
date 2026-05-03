package com.clougence.schema.editor.builder.actions;

import java.util.List;

import lombok.Getter;

/**
 * @author mode 2021/6/8 19:56
 */
@Getter
public class Action {

    private final List<String> sqlString;
    private final String       catalog;
    private final String       schema;
    private final String       table;

    public Action(List<String> sqlString, String catalog, String schema, String table){
        this.sqlString = sqlString;
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    public final WarnLevel getWarnLevel() { return WarnLevel.Normal; }
}
