package com.clougence.clouddm.ds.rules.rdb.using_mongo;

import com.clougence.sql.mongodb.security.MongoSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public abstract class MongoAbstractRuleTest extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MongoSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MongoDB;
    }
}
