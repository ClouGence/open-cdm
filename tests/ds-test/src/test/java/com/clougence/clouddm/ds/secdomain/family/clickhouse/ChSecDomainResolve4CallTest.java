package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4CallTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4CallTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void selectFunction() {
        String sql = "select test_function();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

    @Test
    public void selectTimeFunc() {
        String sql = "select DATE '1994-01-01' + INTERVAL '1' YEAR;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/DATE/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/INTERVAL/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("DATE") &&//
                   domain.getArgs().size() == 1 && domain.getArgs().get(0).equals("1994-01-01");
        }
    }

}
