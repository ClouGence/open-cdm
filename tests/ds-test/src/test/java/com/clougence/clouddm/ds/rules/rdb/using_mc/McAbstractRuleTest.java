package com.clougence.clouddm.ds.rules.rdb.using_mc;

import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainResolveSpi;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class McAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new McSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MaxCompute;
    }

    protected  ContextInfo contextInfo() {
        McConfig dataSourceConfig = new McConfig();
        dataSourceConfig.setSchemaStyle(true);
        return ContextInfo.builder().deepParser(false).dataSourceConfig(dataSourceConfig).build();
    }
}
