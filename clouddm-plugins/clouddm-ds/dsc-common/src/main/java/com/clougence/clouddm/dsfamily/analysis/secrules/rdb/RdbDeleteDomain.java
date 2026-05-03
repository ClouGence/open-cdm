package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RdbDeleteDomain extends RdbWhereDomain implements RdbConfigNames {

    private String  catalog;
    private String  schema;
    private String  table;

    private boolean multiDelete;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        List<Map<TargetType, String>> res = new ArrayList<>();
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, this.table);
        res.add(map);
        return res;
    }

    @Override
    public void configName(String catalog, String schema, String name) {
        this.catalog = catalog;
        this.schema = schema;
        this.table = name;
    }
}
