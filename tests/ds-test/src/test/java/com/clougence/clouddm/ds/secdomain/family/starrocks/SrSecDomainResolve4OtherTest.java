package com.clougence.clouddm.ds.secdomain.family.starrocks;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.starrocks.analysis.SrResAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSecDomainResolveSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbFunctionDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class SrSecDomainResolve4OtherTest extends SrSecDomainTestSupport {

    public SrSecDomainResolve4OtherTest(){
        this.analysisSpi = new SrResAnalysisSpi(null);
        this.resolveSpi = new SrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.StarRocks;
    }

    @Test
    public void createView1() {
        String sql = "Create view `ss` as select * from testdb;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_VIEW && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("ss");

        }
    }

    @Test
    public void alterView1() {
        String sql = "ALTER VIEW ss\n" + "(\n" + "    c1 COMMENT \"column 1\",\n" + "    c2 COMMENT \"column 2\",\n" + "    c3 COMMENT \"column 3\"\n" + ")\n"
                     + "AS SELECT k1, k2, SUM(v1) \n" + "FROM example_table\n" + "GROUP BY k1, k2;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_VIEW && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("ss");
        }
    }

    @Test
    public void alterMView1() {
        String sql = "ALTER MATERIALIZED VIEW ss SET (\"session.new_planner_optimize_timeout\" = \"30000\");";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_VIEW && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("ss") && domain.isMaterialized();
        }
    }

    @Test
    public void dropView() {
        String sql = "drop view ss";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_VIEW && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("ss");

        }
    }

    @Test
    public void createView2() {
        String sql = "Create view a.`ss` as select * from testdb;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/a/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_VIEW && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("a") && domain.getView().equals("ss");
        }
    }

    @Test
    public void createMView1() {
        String sql = "Create materialized view ss as select * from testdb;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_VIEW && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("ss");
            assert domain.isMaterialized();

        }
    }

    @Test
    public void dropMView1() {
        String sql = "drop materialized view ss;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_VIEW && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("ss");
            assert domain.isMaterialized();
        }
    }

    @Test
    public void createMView2() {
        String sql = "Create materialized view a.ss as select * from testdb;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/a/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_VIEW && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("a") && domain.getView().equals("ss");
            assert domain.isMaterialized();
        }
    }

    @Test
    public void createFunction() {
        String sql = "CREATE FUNCTION `ttt`(INT) RETURNS INT PROPERTIES (\n" + "    \"file\"=\"file:///path/to/java-udf-demo-jar-with-dependencies.jar\",\n"
                     + "    \"symbol\"=\"org.apache.doris.udf.AddOne\",\n" + "    \"always_nullable\"=\"true\",\n" + "    \"type\"=\"JAVA_UDF\"\n" + ");";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/ttt/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_FUNCTION;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbFunctionDomain domain = (RdbFunctionDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_FUNCTION && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("ttt");
        }
    }

    @Test
    public void createFunction2() {
        String sql = "CREATE FUNCTION schema1.ttt(INT) RETURNS INT PROPERTIES (\n" + "    \"file\"=\"file:///path/to/java-udf-demo-jar-with-dependencies.jar\",\n"
                     + "    \"symbol\"=\"org.apache.doris.udf.AddOne\",\n" + "    \"always_nullable\"=\"true\",\n" + "    \"type\"=\"JAVA_UDF\"\n" + ");";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/ttt/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_FUNCTION;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbFunctionDomain domain = (RdbFunctionDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_FUNCTION && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("ttt");
        }
    }

    @Test
    public void dropFunction2() {
        String sql = "drop FUNCTION schema1.ttt()";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/ttt/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_FUNCTION;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbFunctionDomain domain = (RdbFunctionDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_FUNCTION && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("ttt");
        }
    }
}
