package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConfigDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4AdministratorTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4AdministratorTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void set_global_1() {
        String sql1 = "SET force_index_by_date = 1;";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.ConfigKey &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1.substring(0, sql1.length() - 1));
            assert splitScripts.get(0).getType() == SecQueryType.CONFIG_WRITE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConfigDomain domain = (RdbConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("force_index_by_date");
        }
    }

    @Test
    public void set_global_2() {
        String sql1 = "SET force_index_by_date = 1, bb = 32;";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.ConfigKey &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1.substring(0, sql1.length() - 1));
            assert splitScripts.get(0).getType() == SecQueryType.CONFIG_WRITE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        assert domainList.size() == 2;
        {
            RdbConfigDomain domain = (RdbConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("force_index_by_date");
        }
        {
            RdbConfigDomain domain = (RdbConfigDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("bb");
        }
    }

}
