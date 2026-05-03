package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RdbRevokeDomain extends RuleDomain implements RdbConfigNames {

    private String name;

    @Override
    public void configName(String catalog, String schema, String name) {
        this.name = name;
    }

//    @Override
//    public String resolveName(TargetType targetType) {
//        return this.name;
//    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.UserOrRole, this.name);
        return Collections.singletonList(map);
    }
}
