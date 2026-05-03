package com.clougence.clouddm.dsfamily.analysis.secrules.ref;

import lombok.Getter;
import lombok.Setter;

/**
 * defined for full migration. Every full task processor process one table unit
 *
 * @author wanshao create time is 2020/1/31
 **/
@Getter
@Setter
public class MetaColumn {

    private String  catalog;
    private String  schema;
    private String  table;
    private String  column;

    //private boolean real;
    //private boolean expression;
    private boolean all;

    private String  alias;

    public static MetaColumn buildAll(String catalog, String schema, String table) {
        MetaColumn col = new MetaColumn();
        col.catalog = catalog;
        col.schema = schema;
        col.table = table;
        col.column = null;
        col.all = true;
        return col;
    }

    public static MetaColumn buildCol(String catalog, String schema, String table, String colName, String asName) {
        MetaColumn col = new MetaColumn();
        col.catalog = catalog;
        col.schema = schema;
        col.table = table;
        col.column = colName;
        col.all = false;
        col.alias = asName;
        return col;
    }
}
