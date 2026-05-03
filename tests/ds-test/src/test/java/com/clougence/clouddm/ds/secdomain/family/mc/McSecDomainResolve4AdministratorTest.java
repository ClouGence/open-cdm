package com.clougence.clouddm.ds.secdomain.family.mc;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.maxcompute.analysis.McResAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainResolveSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mc.notsupportschema.McSecDomainTestSupport;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConfigDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyConfigDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class McSecDomainResolve4AdministratorTest extends McSecDomainTestSupport {

    public McSecDomainResolve4AdministratorTest(){
        this.analysisSpi = new McResAnalysisSpi(null);
        this.resolveSpi = new McSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new McSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MaxCompute;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void set_1() {
        String sql1 = "set odps.namespace.schema=true";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.ConfigKey &&//
                   resList.get(0).toDsResPath().getResPath().equals("/max_binlog_cache_size/");
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
    public void set_2() {
        String sql1 = "set odps.sql.reshuffle.dynamicpt=true";

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
            RdbConfigDomain domain = (RdbConfigDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CONFIG_WRITE && domain.getAuditKind() == SecQueryKind.OTHER;
            assert domain.getConfigKey().equals("odps.sql.reshuffle.dynamicpt");
        }
    }
}
