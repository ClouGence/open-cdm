package com.clougence.clouddm.ds.secdomain.family.doris;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.doris.analysis.DrResAnalysisSpi;
import com.clougence.clouddm.ds.doris.analysis.DrSecDomainResolveSpi;
import com.clougence.clouddm.ds.doris.analysis.DrSplitAnalysisSpi;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrConfigDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrSecDomainResolve4AdministratorTest extends DrSecDomainTestSupport {

    public DrSecDomainResolve4AdministratorTest(){
        this.analysisSpi = new DrResAnalysisSpi(null);
        this.resolveSpi = new DrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new DrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Doris;
    }

    @Test
    public void set_global_1() {
        String sql1 = "SET GLOBAL max_binlog_cache_size = 1024 * 1024 * 1024;";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.ConfigKey &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.CONFIG_WRITE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        assert domainList.size() == 1;
        {
            DrConfigDomain domain = (DrConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("max_binlog_cache_size");
        }
    }

    @Test
    public void set_global_2() {
        String sql1 = "set @@max_binlog_cache_size = 1024;";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.ConfigKey &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.CONFIG_WRITE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        assert domainList.size() == 1;
        {
            DrConfigDomain domain = (DrConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("max_binlog_cache_size");
        }
    }

    @Test
    public void set_local_1() {
        String sql1 = "SET local max_binlog_cache_size = 1024 * 1024 * 1024;";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.ConfigKey &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.CONFIG_WRITE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        assert domainList.size() == 1;
        {
            DrConfigDomain domain = (DrConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("max_binlog_cache_size");
        }
    }

    @Test
    public void set_local_2() {
        String sql1 = "set @max_binlog_cache_size = 1024;";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.ConfigKey &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.CONFIG_WRITE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        assert domainList.size() == 1;
        {
            DrConfigDomain domain = (DrConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("max_binlog_cache_size");
        }
    }

    @Test
    public void set_session_1() {
        String sql1 = "SET session max_binlog_cache_size = 1024 * 1024 * 1024;";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.ConfigKey &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.CONFIG_WRITE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        assert domainList.size() == 1;
        {
            DrConfigDomain domain = (DrConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("max_binlog_cache_size");
        }
    }
}
