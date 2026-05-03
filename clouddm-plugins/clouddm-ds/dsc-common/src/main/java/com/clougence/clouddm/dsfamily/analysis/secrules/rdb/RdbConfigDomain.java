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
public class RdbConfigDomain extends RuleDomain {

    private String configKey;

    public RdbConfigDomain(String configKey){
        this.configKey = configKey;
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.ConfigKey, configKey);
        return Collections.singletonList(map);
    }
}
