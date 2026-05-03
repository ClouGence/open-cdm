package com.clougence.clouddm.ds.hana.analysis;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class HanaSecDomainResolveSpi implements SecDomainResolveSpi {

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo) {
        return Collections.emptyList();
    }
}
