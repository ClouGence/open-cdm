package com.clougence.clouddm.ds.secdomain.family.mysql;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyConfigDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MySecDomainResolve4AdministratorTest extends MySecDomainTestSupport {

    public MySecDomainResolve4AdministratorTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
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
            MyConfigDomain domain = (MyConfigDomain) domainList.get(0);
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
            MyConfigDomain domain = (MyConfigDomain) domainList.get(0);
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
            MyConfigDomain domain = (MyConfigDomain) domainList.get(0);
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
            MyConfigDomain domain = (MyConfigDomain) domainList.get(0);
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
            MyConfigDomain domain = (MyConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("max_binlog_cache_size");
        }
    }
}
