package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class RdbViewDomain extends RuleDomain {

    private String  catalog;
    private String  schema;
    private String  view;
    private String  comment;

    private boolean isMaterialized;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, this.view);
        return Collections.singletonList(map);
    }
}
