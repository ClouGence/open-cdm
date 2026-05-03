package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbProcedureDomain extends RuleDomain {

    private String catalog;
    private String schema;
    private String name;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, catalog);
        map.put(TargetType.Schema, schema);
        map.put(TargetType.Procedure, name);
        return Collections.singletonList(map);
    }
}
