package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbInsertDomain extends RdbQueryDomain implements RdbConfigNames {

    private String                    catalog;
    private String                    schema;
    private String                    table;
    private List<String>              columns;

    private boolean                   onlyValues;
    private boolean                   multipleValues;
    private RdbInsertConflictStrategy conflict;// (a) DO UPDATE SET;

    // insert from select
    private boolean                   fromSelect;

    private boolean                   hasSubQuery;
    private boolean                   hasNullValue;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, this.table);
        return Collections.singletonList(map);
    }

    @Override
    public void configName(String catalog, String schema, String name) {
        this.catalog = catalog;
        this.schema = schema;
        this.table = name;
    }
}
