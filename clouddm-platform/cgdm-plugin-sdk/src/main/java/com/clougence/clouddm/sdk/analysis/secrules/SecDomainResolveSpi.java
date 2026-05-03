package com.clougence.clouddm.sdk.analysis.secrules;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface SecDomainResolveSpi extends Spi {

    List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo);
}
