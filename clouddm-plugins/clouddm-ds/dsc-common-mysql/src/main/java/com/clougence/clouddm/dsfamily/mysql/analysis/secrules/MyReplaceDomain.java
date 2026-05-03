package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import java.util.*;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyReplaceDomain extends RuleDomain {

    private String       catalog;
    private String       schema;
    private String       table;

    // insert
    private boolean      fromSelect;
    private boolean      hasSubQuery;
    private boolean      hasNullValue;
    private List<String> columns    = new ArrayList<>();

    //update
    private List<String> setColumns = new ArrayList<>();
    private boolean      selectInSet;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, this.table);
        return Collections.singletonList(map);
    }
}
