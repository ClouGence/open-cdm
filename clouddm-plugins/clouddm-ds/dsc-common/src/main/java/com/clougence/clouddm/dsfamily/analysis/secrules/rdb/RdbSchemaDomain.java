package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class RdbSchemaDomain extends RuleDomain {

    private String catalog;
    private String schema;

    // for rename
    private String newName;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        return Collections.singletonList(map);
    }
}
