package com.clougence.clouddm.ds.secdomain.family.mongo;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.mongodb.analysis.MongoResAnalysisSpi;
import com.clougence.clouddm.ds.mongodb.analysis.MongoSecDomainResolveSpi;
import com.clougence.clouddm.ds.mongodb.analysis.MongoSplitAnalysisSpi;
import com.clougence.clouddm.ds.mongodb.analysis.secrules.MongoCmdDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MongoSecDomainTest extends MongoSecDomainTestSupport {

    public MongoSecDomainTest(){
        this.analysisSpi = new MongoResAnalysisSpi(new TestMetaServiceImpl());
        this.resolveSpi = new MongoSecDomainResolveSpi(new TestMetaServiceImpl());
        this.splitAnalysisSpi = new MongoSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MongoDB;
    }

    @Test
    public void commands_1() {
        String sql = "db.aa.find();db.aa.drop();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/aa/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 2;
        {
            assert splitScripts.get(0).getScript().equals("db.aa.find()");
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }
        {
            assert splitScripts.get(1).getScript().equals("db.aa.drop()");
            assert splitScripts.get(1).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getSchema() == null && domain.getFunc().equals("find");
        }
        {
            MongoCmdDomain domain = (MongoCmdDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.DROP_TABLE && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getSchema() == null && domain.getFunc().equals("drop");
        }
    }
}
