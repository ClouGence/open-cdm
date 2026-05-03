package com.clougence.clouddm.ds.redis.analysis.secrules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisCmdDomain extends RuleDomain {

    private String schema;
    private String command;
    private String commandType;

    public RedisCmdDomain(String command, String commandType){
        this.command = command;
        this.commandType = commandType;
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        List<Map<TargetType, String>> res = new ArrayList<>();
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Schema, this.schema);
        res.add(map);
        return res;
    }
}
