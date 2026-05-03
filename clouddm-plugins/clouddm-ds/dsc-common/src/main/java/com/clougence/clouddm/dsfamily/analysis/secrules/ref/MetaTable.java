package com.clougence.clouddm.dsfamily.analysis.secrules.ref;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * defined for full migration. Every full task processor process one table unit
 *
 * @author wanshao create time is 2020/1/31
 **/
@Getter
@Setter
public class MetaTable {

    private String           catalog;
    private String           schema;
    private String           table;

    private boolean          real;

    private String           alias;
    private List<MetaColumn> columns;

    public MetaTable(){
        this(null, null, null, null);
    }

    public MetaTable(String catalog, String schema, String table){
        this(catalog, schema, table, table);
    }

    public MetaTable(String catalog, String schema, String table, String alias){
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
        this.alias = alias;
        this.real = true;
        this.columns = new ArrayList<>();
    }

    //    public void addSelectAll() {
    //        if (StringUtils.isNotBlank(table)) {
    //            this.columns.add(MetaColumn.buildAll(this.catalog, this.schema, this.table));
    //        } else if (StringUtils.isNotBlank(alias)) {
    //            this.columns.add(MetaColumn.buildAll(this.catalog, this.schema, this.alias));
    //        } else {
    //            throw new UnsupportedOperationException("table or alias is null.");
    //        }
    //    }
    //
    //    public void addSelect(String colName, String asName) {
    //        if (StringUtils.isNotBlank(table)) {
    //            this.columns.add(MetaColumn.buildCol(this.catalog, this.schema, this.table, colName, asName));
    //        } else if (StringUtils.isNotBlank(table)) {
    //            this.columns.add(MetaColumn.buildCol(this.catalog, this.schema, this.alias, colName, asName));
    //        } else {
    //            throw new UnsupportedOperationException("table or alias is null.");
    //        }
    //    }
}
