package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

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
public class RdbIndexDomain extends RuleDomain {

    private String       tableCatalog;
    private String       tableSchema;
    private String       tableName;

    private String       type;
    private String       catalog;
    private String       schema;
    private String       name;
    private String       comment;

    private String       newName;

    private List<String> columns;

    //    @Override
    //    public String resolveName(TargetType targetType) {
    //        switch (targetType) {
    //            case Catalog:
    //                return this.tableCatalog;
    //            case Schema:
    //                return this.tableSchema;
    //            //case Index:
    //            //    return this.name;
    //            default:
    //                return "";
    //        }
    //    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        List<Map<TargetType, String>> res = new ArrayList<>();
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.tableCatalog);
        map.put(TargetType.Schema, this.tableSchema);
        map.put(TargetType.Table, this.tableName);
        res.add(map);
        return res;
    }
}
