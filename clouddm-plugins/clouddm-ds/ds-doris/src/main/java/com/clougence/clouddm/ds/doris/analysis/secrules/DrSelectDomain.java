package com.clougence.clouddm.ds.doris.analysis.secrules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.detectrule.lang.reflect.RuleIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DrSelectDomain extends RdbSelectDomain {

    private boolean hasLimit;

    @RuleIgnore
    private boolean valuesSelect;

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
