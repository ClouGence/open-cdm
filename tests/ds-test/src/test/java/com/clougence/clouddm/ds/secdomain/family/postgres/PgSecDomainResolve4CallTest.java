package com.clougence.clouddm.ds.secdomain.family.postgres;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgSecDomainResolve4CallTest extends PgSecDomainTestSupport {

    public PgSecDomainResolve4CallTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PostgreSQL;
    }

    @Test
    public void execFunction() {
        String sql = "select test_function()";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function");
            assert domain.isEmptyArg() && domain.isFunc() && domain.getArgs().isEmpty();
        }
    }



    @Test
    public void execFunction2() {
        String sql = "select test.test_function()";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getCallName().equals("test_function");
            assert domain.isEmptyArg() && domain.isFunc() && domain.getArgs().isEmpty();
        }
    }

    @Test
    public void execFunction3() {
        String sql = "select test_catalog.test.test_function()";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_function");
            assert domain.isEmptyArg() && domain.isFunc() && domain.getArgs().isEmpty();
        }
    }

    @Test
    public void execFunction4() {
        String sql = "select \"test_catalog\".\"test\".\"test_function\"()";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_function");
            assert domain.isEmptyArg() && domain.isFunc() && domain.getArgs().isEmpty();
        }
    }

    @Test
    public void execFunctionWithArgs() {
        String sql = "select test_function(1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 1 && domain.getArgs().get(0).equals("1");
        }
    }

    @Test
    public void execFunctionWithArgs2() {
        String sql = "select test_function(1,2)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 2 && domain.getArgs().get(0).equals("1");
            assert domain.getArgs().get(1).equals("2");
        }
    }

    @Test
    public void execFunctionWithFunctionArg() {
        String sql = "select test_function(test_function2())";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test_function/");
            assert resList.get(1).getType() == TargetType.Function && //
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test_function2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 1 && domain.getArgs().get(0).equals("test_function2()");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function2");
            assert domain.isEmptyArg() && domain.isFunc() && domain.getArgs().isEmpty();
        }
    }

    @Test
    public void execFunctionWithFunctionArg2() {
        String sql = "select test_function(1,test_function2(2,3))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test_function/");
            assert resList.get(1).getType() == TargetType.Function && //
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test_function2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 2 && domain.getArgs().contains("test_function2(2,3)");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function2");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 2;
            assert domain.getArgs().contains("2") && domain.getArgs().contains("3");
        }
    }

    @Test
    public void execFunctionWithFunctionArg3() {
        String sql = "select test_function(1,test.test_function2(2,3))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test_function/");
            assert resList.get(1).getType() == TargetType.Function && //
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/test_function2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 2 && domain.getArgs().contains("test.test_function2(2,3)");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getCallName().equals("test_function2");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 2;
            assert domain.getArgs().contains("2") && domain.getArgs().contains("3");
        }
    }

    @Test
    public void execFunctionWithFunctionArg4() {
        String sql = "SELECT test_function(1,test_catalog.test.test_function2(2,3))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test_function/");
            assert resList.get(1).getType() == TargetType.Function && //
                   resList.get(1).toDsResPath().getResPath().equals("/test_catalog/test/test_function2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 2 && domain.getArgs().contains("test_catalog.test.test_function2(2,3)");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_function2");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 2;
            assert domain.getArgs().contains("2") && domain.getArgs().contains("3");
        }
    }

    @Test
    public void execProcedure() {
        String sql = "call test_procedure()";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test_procedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_procedure");
            assert domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().isEmpty();
        }

    }

    @Test
    public void execProcedure2() {
        String sql = "call test.test_procedure()";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/test_procedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getCallName().equals("test_procedure");
            assert domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().isEmpty();
        }
    }

    @Test
    public void execProcedure3() {
        String sql = "call test_catalog.test.test_procedure()";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_procedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_procedure");
            assert domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().isEmpty();
        }
    }

    @Test
    public void execProcedureWithArgs() {
        String sql = "call test_catalog.test.test_procedure(1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_procedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_procedure");
            assert !domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().size() == 1 && domain.getArgs().contains("1");
        }
    }

    @Test
    public void execProcedureWithArgs2() {
        String sql = "call test_catalog.test.test_procedure(1,2)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_procedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_procedure");
            assert !domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().size() == 2 && domain.getArgs().contains("1") && domain.getArgs().contains("2");
        }
    }

    @Test
    public void execProcedureWithFunctionArgs() {
        String sql = "call \"test_catalog\".test.test_procedure(1,test_function(3))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_procedure/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_procedure");
            assert !domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().size() == 2 && domain.getArgs().contains("1") && domain.getArgs().contains("test_function(3)");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 1 && domain.getArgs().contains("3");
        }
    }

    @Test
    public void execProcedureWithFunctionArgs2() {
        String sql = "call \"test_catalog\".test.test_procedure(1,test.test_function(3))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_procedure/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_procedure");
            assert !domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().size() == 2 && domain.getArgs().contains("1") && domain.getArgs().contains("test.test_function(3)");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 1 && domain.getArgs().contains("3");
        }
    }

    @Test
    public void execProcedureWithFunctionArgs3() {
        String sql = "call test_catalog.test.test_procedure(1,test_catalog.test.test_function(3))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_procedure/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_catalog/test/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_procedure");
            assert !domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().size() == 2 && domain.getArgs().contains("1")
                   && domain.getArgs().contains("test_catalog.test.test_function(3)");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 1 && domain.getArgs().contains("3");
        }
    }

    @Test
    public void execProcedureWithFunctionArgs4() {
        String sql = "call test_catalog.test.test_procedure(1,test_catalog.test.test_function(3),(select id from test_catalog.test.table1))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_catalog/test/test_procedure/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_catalog/test/test_function/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_catalog/test/table1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_procedure");
            assert !domain.isEmptyArg() && !domain.isFunc() && domain.getArgs().size() == 3 && domain.getArgs().contains("1")
                   && domain.getArgs().contains("test_catalog.test.test_function(3)") && domain.getArgs().contains("(select id from test_catalog.test.table1)");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog().equals("test_catalog") && domain.getSchema().equals("test") && domain.getCallName().equals("test_function");
            assert !domain.isEmptyArg() && domain.isFunc() && domain.getArgs().size() == 1 && domain.getArgs().contains("3");
        }
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(2);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.SUB_CALL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().equals(Collections.singletonList("id")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

}
