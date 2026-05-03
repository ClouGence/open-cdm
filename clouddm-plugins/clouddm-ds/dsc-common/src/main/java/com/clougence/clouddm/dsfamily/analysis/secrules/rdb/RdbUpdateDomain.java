package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.*;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.detectrule.lang.reflect.RuleIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbUpdateDomain extends RdbWhereDomain implements RdbConfigNames {

    @Deprecated
    private String                 catalog;
    @Deprecated
    private String                 schema;
    @Deprecated
    private String                 table;

    private List<String>           setColumns;
    private boolean                selectInSet;

    private boolean                multiUpdate;

    private List<RdbJoinType>      joinTypes = new ArrayList<>();
    @RuleIgnore
    protected List<RdbTableDomain> tables    = new ArrayList<>();

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        HashMap<TargetType, String> map = new HashMap<>();
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
