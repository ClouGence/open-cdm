package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McFlushDomain extends RuleDomain {

    private McFlushType flushType;

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        return Collections.emptyList();
    }
}
