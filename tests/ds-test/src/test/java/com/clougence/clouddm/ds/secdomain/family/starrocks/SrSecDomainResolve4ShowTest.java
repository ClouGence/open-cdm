package com.clougence.clouddm.ds.secdomain.family.starrocks;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.starrocks.analysis.SrResAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSecDomainResolveSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbResourceDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class SrSecDomainResolve4ShowTest extends SrSecDomainTestSupport {

    public SrSecDomainResolve4ShowTest(){
        this.analysisSpi = new SrResAnalysisSpi(null);
        this.resolveSpi = new SrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.StarRocks;
    }

    @Test
    public void createTable_1() {
        String sql = "SHOW ALTER TABLE COLUMN";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Column &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showAlter2() {
        String sql = "SHOW ALTER TABLE COLUMN from tt";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Column &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/tt/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("tt") && domain.getName() == null;
        }
    }

    @Test
    public void showAlter3() {
        String sql = "SHOW ALTER TABLE ROLLUP from tt.cc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/tt/cc/");
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
            assert domain.getCatalog().equals("tt") && domain.getSchema().equals("cc") && domain.getName() == null;
        }
    }

    @Test
    public void showAlter4() {
        String sql = "SHOW ALTER MATERIALIZED VIEW from tt.cc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Materialized &&//
                   resList.get(0).toDsResPath().getResPath().equals("/tt/cc/");
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
            assert domain.getCatalog().equals("tt") && domain.getSchema().equals("cc") && domain.getName() == null;
        }
    }

    @Test
    public void showCreateTable() {
        String sql = "SHOW create table a";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/a/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("a");
        }
    }

    @Test
    public void showCreateView() {
        String sql = "SHOW create view schema1.a";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/a/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("a");
        }
    }

    @Test
    public void showDelete() {
        String sql = "SHOW delete";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showDelete2() {
        String sql = "SHOW delete from schema1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName() == null;
        }
    }

    @Test
    public void showFullColumns() {
        String sql = "SHOW full columns from table1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("table1");
        }
    }

    @Test
    public void showFullColumns1() {
        String sql = "SHOW full columns from schema1.table1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/table1/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("table1");
        }
    }

    @Test
    public void showIndex() {
        String sql = "SHOW index from table1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("table1");
        }
    }

    @Test
    public void showIndex1() {
        String sql = "SHOW index from schema1.table1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/table1/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("table1");
        }
    }

    @Test
    public void showTables() {
        String sql = "SHOW tables";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showTables1() {
        String sql = "SHOW tables from schema1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName() == null;
        }
    }

    @Test
    public void showFunctions() {
        String sql = "SHOW functions";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null && domain.getTarget() == TargetType.Function;
        }
    }

    @Test
    public void showFunctions1() {
        String sql = "SHOW functions from schema1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName() == null;
        }
    }

    @Test
    public void showDictionary() {
        String sql = "SHOW DICTIONARY";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showFrontends() {
        String sql = "SHOW FRONTENDS";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showComputeNodes() {
        String sql = "SHOW COMPUTE NODES";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showAnalyzeJob() {
        String sql = "SHOW ANALYZE JOB";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showBroker() {
        String sql = "SHOW BROKER";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showRunningQueries() {
        String sql = "SHOW RUNNING QUERIES";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showProcessList() {
        String sql = "SHOW  PROCESSLIST";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Unknown &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showTables2() {
        String sql = "SHOW tables from catalog1.schema1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/catalog1/schema1/");
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
            assert domain.getCatalog().equals("catalog1") && domain.getSchema().equals("schema1") && domain.getName() == null;
        }
    }

    @Test
    public void showCatalogs() {
        String sql = "show catalogs";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showGrants() {
        String sql = "show grants";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showUsers() {
        String sql = "show users";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

    @Test
    public void showRoles() {
        String sql = "show roles";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.UserOrRole &&//
                   resList.get(0).toDsResPath().getResPath().equals("/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName() == null;
        }
    }

}
