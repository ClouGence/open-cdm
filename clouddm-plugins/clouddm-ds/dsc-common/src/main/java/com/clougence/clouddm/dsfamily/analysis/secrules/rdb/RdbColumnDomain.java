package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RdbColumnDomain extends RuleDomain {

    private String  catalog;
    private String  schema;
    private String  table;
    private String  column;
    private String  comment;

    private String  typeName;
    private String  typeDesc;
    private boolean nullable;
    private boolean primary;
    private boolean unique;
    private boolean foreign;
    private boolean index;

    private String  length;
    private String  defaultValue;

    // for column rename
    private String  newName;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        List<Map<TargetType, String>> res = new ArrayList<>();
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, this.table);
        map.put(TargetType.Column, this.column);
        res.add(map);
        return res;
    }
}
