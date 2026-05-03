package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbResourceDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

// https://dev.mysql.com/doc/refman/8.4/en/show.html
public class ChSecDomainResolve4ShowTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4ShowTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void showBinaryLogStatus_1() {
        // todo can't parse
        //        List<RuleDomain> list = resolveSpi.resolveDomain(dataSourceType, "show binary log status");
        //        MyShowDomain domain = (MyShowDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
        //        assert domain.getCatalog()== null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
    }

    @Test
    public void showBinaryLogs_1() {
        String sql = "show create database test";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema && resList.get(0).toDsResPath().getResPath().equals("/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbResourceDomain domain = (RdbResourceDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getTarget() == TargetType.Schema && domain.getSchema().equals("test");
        }
    }

    @Test
    public void showCreateTable() {
        String sql = "show create table test";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table//
                   && resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Table && domain1.getName().equals("test");
        }
    }

    @Test
    public void showCreateTable2() {
        String sql = "show create table aa.test";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table//
                   && resList.get(0).toDsResPath().getResPath().equals("/aa/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Table && domain1.getName().equals("test") && domain1.getSchema().equals("aa");
        }
    }

    @Test
    public void showPrivileges_1() {
        String sql = "show privileges";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showMerges_1() {
        String sql = "show merges";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showEngines() {
        String sql = "show engines";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showFileSystemCaches() {
        String sql = "SHOW FILESYSTEM CACHES";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showCluster() {
        String sql = "SHOW cluster 'default'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showClusters() {
        String sql = "SHOW clusters";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showAccess() {
        String sql = "SHOW access";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showCreateQuota() {
        String sql = "SHOW create quota default";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showProfiles() {
        String sql = "SHOW profiles";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showUsers() {
        String sql = "SHOW users";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.UserOrRole;
        }
    }

    @Test
    public void showRoles() {
        String sql = "SHOW roles";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.UserOrRole;
        }
    }

    @Test
    public void showCurrentQuote() {
        String sql = "SHOW current Quota";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showQuotas() {
        String sql = "SHOW quotas";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Environment && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showFunctions() {
        String sql = "show functions";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function && resList.get(0).toDsResPath().getResPath().equals("/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            RdbResourceDomain domain1 = (RdbResourceDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getTarget() == TargetType.Function;
        }
    }

}
