package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class RdbCatalogDomain extends RuleDomain {

    private String catalog;

    // for rename
    private String newName;

    //    @Override
    //    public String resolveName(TargetType targetType) {
    //        switch (targetType) {
    //            case Catalog:
    //                return this.catalog;
    //            default:
    //                return "";
    //        }
    //    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        return Collections.singletonList(map);
    }
}
