package com.clougence.clouddm.ds.mongodb.analysis.secrules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoCollectionDomain extends MongoCmdDomain {

    private String collection;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        List<Map<TargetType, String>> res = new ArrayList<>();
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Schema, this.getSchema());
        map.put(TargetType.Table, this.collection);
        res.add(map);
        return res;
    }
}
