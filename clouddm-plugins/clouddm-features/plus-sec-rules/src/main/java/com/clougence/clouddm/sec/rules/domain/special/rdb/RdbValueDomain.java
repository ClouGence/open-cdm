package com.clougence.clouddm.sec.rules.domain.special.rdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
@Setter
public class RdbValueDomain extends RuleDomain {

    private String       catalog;
    private String       schema;
    private String       table;
    private String       column;
    private String       dbType;
    private int          index;

    private String       senAlgorithm;
    private boolean      hasRange;

    private String       value;
    private boolean      itIsNull;

    private List<String> allColumns = new ArrayList<>();

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        List<Map<TargetType, String>> res = new ArrayList<>();
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, this.table);
        map.put(TargetType.Column, this.column);
        res.add(map);
        return res;
    }
}
