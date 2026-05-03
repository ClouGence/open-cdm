package com.clougence.clouddm.ds.clickhouse.analysis.secrules;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ChSelectDomain extends RdbSelectDomain {

    private boolean hasLimit;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        List<Map<TargetType, String>> res = new ArrayList<>();
        if (getChildren() != null) {
            for (RuleDomain child : getChildren()) {
                res.addAll(child.resolveResource());
            }
        }

        return res;
    }
}
