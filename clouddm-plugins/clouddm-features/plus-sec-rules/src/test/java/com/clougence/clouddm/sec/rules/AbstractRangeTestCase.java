package com.clougence.clouddm.sec.rules;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.CollectionUtils;

public class AbstractRangeTestCase {

    protected MySecDomainResolveSpi     resolveSpi     = new MySecDomainResolveSpi();
    protected DataSourceType            dataSourceType = DataSourceType.MySQL;
    protected final Map<String, Object> ctx            = CollectionUtils.asMap(//
            SessionSpi.PARAMS_DEFAULT_DB, "test_db",//
            SessionSpi.PARAMS_DEFAULT_SCHEMA, "test_schema");

    protected List<RuleDomain> configDsAndEnv(long envId, long dsId, List<RuleDomain> domainList) {
        for (RuleDomain domain : domainList) {
            domain.setEnvId(envId);
            domain.setDsId(dsId);
        }
        return domainList;
    }
}
