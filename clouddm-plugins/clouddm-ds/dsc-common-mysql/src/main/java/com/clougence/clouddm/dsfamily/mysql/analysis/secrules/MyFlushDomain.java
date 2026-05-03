package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MyFlushDomain extends RuleDomain {

    private MyFlushType flushType;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        return Collections.emptyList();
    }
}
