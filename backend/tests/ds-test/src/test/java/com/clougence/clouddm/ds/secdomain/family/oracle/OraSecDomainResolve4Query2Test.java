package com.clougence.clouddm.ds.secdomain.family.oracle;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraSecDomainResolve4Query2Test extends OraSecDomainTestSupport {

    public OraSecDomainResolve4Query2Test(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Oracle;
    }

    @Test
    public void selectQuery_1() {
        String sql = "SELECT LPAD(' ', (LEVEL - 1) * 4) || last_name AS indented_last_name, \n" + "       employee_id, \n" + "       manager_id, \n" + "       job_title\n"
                     + "FROM abc\n" + "START WITH manager_id IS NULL  \n" + "CONNECT BY PRIOR employee_id = manager_id \n" + "ORDER SIBLINGS BY last_name;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/LPAD/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 5 && domain1.getSelectColumns().equals(Arrays.asList("LEVEL", "last_name", "employee_id", "manager_id", "job_title")) &&//
                   domain1.getSelectVariables().isEmpty() && domain1.getSelectFunc().contains("LPAD") &&//
                   domain1.getSelectFunc().size() == 1 &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain1 = (RdbCallDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.CALL && domain1.getAuditKind() == SecQueryKind.CALL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getCallName().equals("LPAD");
            assert domain1.getArgs().size() == 2 && domain1.getArgs().equals(Arrays.asList(" ", "(LEVEL - 1) * 4"));
        }
    }

    @Test
    public void selectQuery_2() {
        String sql = "SELECT id,salary, CASE WHEN salary >= 1200 then salary WHEN salary >= 1000 THEN '一般' ELSE '继续努力' END AS type FROM employee;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/employee/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "salary")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void selectQuery_3() {
        String sql = "SELECT id,salary, CASE WHEN salary >= (select id from table1) then salary WHEN salary >= 1000 THEN '一般' ELSE '继续努力' END AS type FROM employee;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/employee/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table1/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "salary")) &&//
                   domain1.getSelectVariables().isEmpty() && //
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().equals(Arrays.asList("id")) &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
    }

    @Test
    public void selectQuery_4() {
        String sql = "SELECT id,\"salary\", CASE WHEN salary >= 1200 then salary WHEN \"salary\" >= 1000 THEN '一般' ELSE (select id from table1) END AS type FROM employee;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/employee/");

            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table1/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "salary")) &&//
                   domain1.getSelectVariables().isEmpty() && domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }

        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().equals(Arrays.asList("id")) &&//
                   domain2.getSelectVariables().isEmpty() && domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
    }

}
