package com.clougence.clouddm.ds.secdomain.family.mc.supportschema;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.maxcompute.analysis.McResAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainResolveSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbResourceDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

// https://dev.mysql.com/doc/refman/8.4/en/show.html
public class McSecDomainResolve4ShowTest extends McSecDomainTestSupport {

    public McSecDomainResolve4ShowTest(){
        this.analysisSpi = new McResAnalysisSpi(null);
        this.resolveSpi = new McSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new McSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MaxCompute;
    }

    @Test
    public void showCreateTable1() {
        String sql = "show create table aa";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/test_schema/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa");
        }
    }

    @Test
    public void showRoles() {
        String sql = "show roles";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/") && resObject.getType() == TargetType.UserOrRole;
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
            assert domain.getTarget() == TargetType.UserOrRole && domain.getName() == null;
        }
    }

    @Test
    public void showCreateTable2() {
        String sql = "show create table bb.aa";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/bb/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa") && domain.getSchema().equals("bb");
        }
    }

    @Test
    public void descTable1() {
        String sql = "desc aa";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/test_schema/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa");
        }
    }

    @Test
    public void DescTable2() {
        String sql = "describe bb.aa";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/bb/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa") && domain.getSchema().equals("bb");
        }
    }

    @Test
    public void showColumnsStatistic() {
        String sql = "show statistic bb.aa columns";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/bb/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa") && domain.getSchema().equals("bb");
        }
    }

    @Test
    public void showColumnsStatistic2() {
        String sql = "show statistic aa columns(aa,bb)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/test_schema/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa") && domain.getSchema() == null;
        }
    }

    @Test
    public void showTablePartition1() {
        String sql = "show partitions bb.aa";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/bb/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa") && domain.getSchema().equals("bb");
        }
    }

    @Test
    public void showTableParitions() {
        String sql = "show partitions aa";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/test_schema/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa") && domain.getSchema() == null;
        }
    }

    @Test
    public void showHistoryTable() {
        String sql = "show history for table aa";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/test_schema/aa/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName().equals("aa") && domain.getSchema() == null;
        }
    }

    @Test
    public void showHistory() {
        String sql = "show history for tables";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            ResObject resObject = resList.get(0);
            assert resObject.toDsResPath().getResPath().equals("/test_db/test_schema/") && resObject.getType() == TargetType.Table;
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
            assert domain.getTarget() == TargetType.Table && domain.getName() == null && domain.getSchema() == null;
        }
    }

}
