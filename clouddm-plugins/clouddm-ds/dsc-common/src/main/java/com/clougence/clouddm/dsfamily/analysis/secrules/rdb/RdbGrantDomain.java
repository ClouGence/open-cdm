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
public class RdbGrantDomain extends RuleDomain implements RdbConfigNames {

    private String  name;

    private boolean withGrantOption;

    @Override
    public void configName(String catalog, String schema, String name) {
        this.name = name;
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.UserOrRole, this.name);
        return Collections.singletonList(map);
    }
}
