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
public class RdbUserDomain extends RuleDomain implements RdbConfigNames {

    private String  user;
    private String  password;
    private String  comment;

    private String  newName;

    //
    private boolean ifExists;

    @Override
    public void configName(String catalog, String schema, String name) {
        this.user = name;
    }


    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.UserOrRole, this.user);
        return Collections.singletonList(map);
    }
}
