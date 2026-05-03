package com.clougence.clouddm.ds.redis.analysis.secrules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisKeyDomain extends RedisCmdDomain {

    private String     key;
    private AccessType accessType;

    public RedisKeyDomain(String command, String commandType){
        super(command, commandType);
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        List<Map<TargetType, String>> res = new ArrayList<>();
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Schema, this.getSchema());
        map.put(TargetType.Key, this.key);
        res.add(map);
        return res;
    }
}
