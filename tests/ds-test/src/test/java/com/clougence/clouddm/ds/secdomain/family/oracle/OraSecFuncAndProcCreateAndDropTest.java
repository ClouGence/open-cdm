package com.clougence.clouddm.ds.secdomain.family.oracle;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbFunctionDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbProcedureDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraSecFuncAndProcCreateAndDropTest extends OraSecDomainTestSupport {

    public OraSecFuncAndProcCreateAndDropTest(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Oracle;
    }

    @Test
    public void dropFunction() {
        String sql = "drop function test;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("test");
        }
    }

    @Test
    public void dropFunction2() {
        String sql = "drop function schema1.test;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/schema1/test/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("test");
        }
    }

    @Test
    public void dropFunction3() {
        String sql = "drop function \"schema1\".\"test\";";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/schema1/test/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("test");
        }
    }

    @Test
    public void dropProcedure() {
        String sql = "drop procedure test;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_PROCEDURE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbProcedureDomain domain = (RdbProcedureDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_PROCEDURE && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("test");
        }
    }

    @Test
    public void dropProcedure2() {
        String sql = "drop procedure schema1.test;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/schema1/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_PROCEDURE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbProcedureDomain domain = (RdbProcedureDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_PROCEDURE && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("test");
        }
    }

    @Test
    public void dropProcedure3() {
        String sql = "drop procedure \"schema1\".\"test\";";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/schema1/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_PROCEDURE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbProcedureDomain domain = (RdbProcedureDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_PROCEDURE && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("test");
        }
    }

    @Test
    public void createFunction() {
        String sql = "CREATE OR REPLACE FUNCTION test (emp_id NUMBER)\n" + "RETURN NUMBER\n" + "IS\n" + "    emp_salary NUMBER;\n" + "BEGIN\n"
                     + "    SELECT salary INTO emp_salary\n" + "    FROM employees\n" + "    WHERE employee_id = emp_id;\n" + "\n" + "    RETURN emp_salary;\n" + "EXCEPTION\n"
                     + "    WHEN NO_DATA_FOUND THEN\n" + "        RETURN NULL;\n" + "END;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("test");
        }
    }

    @Test
    public void createProcedure() {
        String sql = "CREATE OR REPLACE PROCEDURE test\n" + "IS\n" + "BEGIN\n" + "    DBMS_OUTPUT.PUT_LINE('Hello, Oracle!');\n" + "END;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_PROCEDURE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbProcedureDomain domain = (RdbProcedureDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_PROCEDURE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("test");
        }
    }

    @Test
    public void createFunction1() {
        String sql = "CREATE OR REPLACE FUNCTION \"schema1\".\"test\" (emp_id NUMBER)\n" + "RETURN NUMBER\n" + "IS\n" + "    emp_salary NUMBER;\n" + "BEGIN\n"
                     + "    SELECT salary INTO emp_salary\n" + "    FROM employees\n" + "    WHERE employee_id = emp_id;\n" + "\n" + "    RETURN emp_salary;\n" + "EXCEPTION\n"
                     + "    WHEN NO_DATA_FOUND THEN\n" + "        RETURN NULL;\n" + "END;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/schema1/test/");
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
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("test");
        }
    }

    @Test
    public void createProcedure1() {
        String sql = "CREATE OR REPLACE PROCEDURE \"schema1\".\"test\"\n" + "IS\n" + "BEGIN\n" + "    DBMS_OUTPUT.PUT_LINE('Hello, Oracle!');\n" + "END;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/schema1/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_PROCEDURE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbProcedureDomain domain = (RdbProcedureDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_PROCEDURE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getName().equals("test");
        }
    }

}
