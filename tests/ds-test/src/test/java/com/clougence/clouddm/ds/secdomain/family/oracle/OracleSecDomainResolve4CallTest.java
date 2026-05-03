package com.clougence.clouddm.ds.secdomain.family.oracle;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OracleSecDomainResolve4CallTest extends OraSecDomainTestSupport {

    public OracleSecDomainResolve4CallTest(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Oracle;
    }

    @Test
    public void callProcedure() {
        String sql = "call testProcedure();";

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

    @Test
    public void callProcedure_1() {
        String sql = "call \"testProcedure\"();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/testProcedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

    @Test
    public void callProcedure_2() {
        String sql = "call test1.testProcedure();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test1/testProcedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test1") && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

    @Test
    public void callProcedure_3() {
        String sql = "call \"test1\".\"testProcedure\"();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test1/testProcedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test1") && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

    @Test
    public void callProcedureWithArgs_1() {
        String sql = "call testProcedure(1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/testProcedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().size() == 1 && !domain.isEmptyArg();
            assert domain.getArgs().contains("1");
        }
    }

    @Test
    public void callProcedureWithArgs_2() {
        String sql = "call testProcedure(2,'cq');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/testProcedure/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().size() == 2 && !domain.isEmptyArg();
            assert domain.getArgs().contains("cq") && domain.getArgs().contains("2");
        }
    }

    @Test
    public void callProcedureWithArgs_3() {
        String sql = "call testProcedure(2,'cq',test());";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/testProcedure/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().size() == 3 && !domain.isEmptyArg();
            assert domain.getArgs().contains("cq") && domain.getArgs().contains("2") && domain.getArgs().contains("test()");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

    @Test
    public void callProcedureWithArgs_3_1() {
        String sql = "call testProcedure(2,'cq',test3.test());";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/testProcedure/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test3/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().size() == 3 && !domain.isEmptyArg();
            assert domain.getArgs().contains("cq") && domain.getArgs().contains("2") && domain.getArgs().contains("test3.test()");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test3") && domain.getCallName().equals("test") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

    @Test
    public void callProcedureWithArgs_4() {
        String sql = "call testProcedure(2,'cq',test(test1()));";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/testProcedure/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/test1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CALL;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("testProcedure") &&//
                   domain.getArgs().size() == 3 && !domain.isEmptyArg();
            assert domain.getArgs().contains("cq") && domain.getArgs().contains("2") && domain.getArgs().contains("test(test1())");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test") &&//
                   domain.getArgs().size() == 1 && !domain.isEmptyArg() && domain.getArgs().contains("test1()");
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getCallName().equals("test1") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
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
            assert splitScripts.get(0).getScript().equals(sql);
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
    public void selectFunction_2() {
        String sql = "select test.test_function();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/test_function/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getCallName().equals("test_function") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

    @Test
    public void selectFunction_3() {
        String sql = "select test.test_function(test2.test_function1());";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/test_function/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test2/test_function1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getCallName().equals("test_function") &&//
                   domain.getArgs().size() == 1 && domain.getArgs().contains("test2.test_function1()") && !domain.isEmptyArg();
        }
        {
            RdbCallDomain domain = (RdbCallDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CALL && domain.getAuditKind() == SecQueryKind.CALL;
            assert domain.getCatalog() == null && domain.getSchema().equals("test2") && domain.getCallName().equals("test_function1") &&//
                   domain.getArgs().isEmpty() && domain.isEmptyArg();
        }
    }

}
