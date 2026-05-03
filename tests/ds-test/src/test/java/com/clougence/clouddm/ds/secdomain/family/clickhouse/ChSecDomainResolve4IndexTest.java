package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4IndexTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4IndexTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void addIndex() {
        String sql = "alter table test add INDEX test_index id TYPE minmax GRANULARITY 4";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getName().equals("test_index");
            assert domain.getType().equals("index");
        }
    }

    @Test
    public void dropIndex() {
        String sql = "alter table test drop index test_index";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_INDEX && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getName().equals("test_index");
            assert domain.getTableName().equals("test");
        }
    }

    @Test
    public void alterDropIndex() {
        String sql = "alter table test DROP INDEX idx_test";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_INDEX && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getName().equals("idx_test");
            assert domain.getTableName().equals("test");
        }
    }

}
