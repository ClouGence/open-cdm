package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4Query1Test extends MsSecDomainTestSupport {

    public MsSecDomainResolve4Query1Test(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void selectQuery_1() {
        String sql = "select id,name from abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_2_1() {
        String sql = "select 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   domain1.isEmptyFrom() &&      //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_2_2() {
        String sql = "select 1 + 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.NORMAL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void selectQuery_2_3() {
        String sql = "select (1 + a) as a from users";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/users/");
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
            MsSelectDomain domain3 = (MsSelectDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.NORMAL;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("users") &&//
                   domain3.getSelectColumns().size() == 1 && domain3.getSelectColumns().contains("a") &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
    }

    @Test
    public void selectQuery_3() {
        String sql = "select @@version";

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert list1.size() == 1;
        {
            MsSelectDomain domain1 = (MsSelectDomain) list1.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 1 && domain1.getSelectVariables().contains("@@version") &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   domain1.isEmptyFrom() &&      //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_4() {
        String sql = "SELECT SERVERPROPERTY('ProductVersion')";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/SERVERPROPERTY/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 1 && domain1.getSelectFunc().contains("SERVERPROPERTY") &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   domain1.isEmptyFrom() &&      //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("SERVERPROPERTY") &&//
                   domain2.getArgs().size() == 1 && domain2.getArgs().get(0).equals("'ProductVersion'");
        }
    }

    @Test
    public void selectQuery_5() {
        String sql = "select * from table_1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_6() {
        String sql = "select id,* from table_1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_7() {
        String sql = "select id,* from [table_1] t";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void emptyWhere_1() {
        String sql = "select * from table_1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void emptyWhere_2() {
        String sql = "select * from table_1 where 1=1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void emptyWhere_3() {
        String sql = "select * from table_1 where id=id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void emptyWhere_4() {
        String sql = "select * from table_1 where id = (id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_1() {
        String sql = "select * from table_1 where id > 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_2() {
        String sql = "select * from table_1 where id is not null";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_3() {
        String sql = "select * from table_1 where 'abc' = name and id > 3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id"));
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_4() {
        String sql = "select * from table_1 where length(name) > 2";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/length/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("name");
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain1_2 = (RdbCallDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CALL && domain1_2.getAuditKind() == SecQueryKind.CALL;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getCallName().equalsIgnoreCase("length") &&//
                   domain1_2.getArgs().get(0).contains("name");
        }
    }

    @Test
    public void conditionWhere_5() {
        String sql = "select * from table_1 where (name = 'abc' and id > 3) or id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id"));
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_6() {
        String sql = "select * from table_1 where 1>1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionIn_1() {
        String sql = "select * from table_1 where id in (1,2,3)";
        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionIn_2() {
        String sql = "select * from table_1 where id in (1,'2',3)";
        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionIn_3() {
        String sql = "select * from table_1 where id in (select id from table_2)";
        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_2/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_2") &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void selectAs_1() {
        String sql = "select id as id1 from table_1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void union_1() {
        String sql = "select * from table_1 union select * from table_2";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_2/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_1") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            MsSelectDomain domain3 = (MsSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_2") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&   //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
    }

    @Test
    public void union_2() {
        String sql = "select * from (select * from table_1 union select * from table_2) t";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("t") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("t") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(2);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_1") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            MsSelectDomain domain3 = (MsSelectDomain) domainList.get(3);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_2") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&   //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
    }

    @Test
    public void union_4() {
        String sql = "select id, name from table_1 where id in (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 7;
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            MsSelectDomain domain3 = (MsSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   !domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
        {
            MsSelectDomain domain4 = (MsSelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("b") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&   //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }
        {
            MsSelectDomain domain5 = (MsSelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable() == null &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   !domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   !domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
            assert !domain5.isHasLimit();
        }
        {
            MsSelectDomain domain6 = (MsSelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.UNION;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table_1") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom() &&     //
                   domain6.isSimpleSelect();
            assert !domain6.isHasWhere() &&      //
                   !domain6.isSelectInWhere() && //
                   domain6.getJoinType() == RdbJoinType.NONE &&//
                   !domain6.isHasUnion() &&      //
                   !domain6.isHasWith();
            assert !domain6.isHasLimit();
        }
        {
            MsSelectDomain domain7 = (MsSelectDomain) domainList.get(6);
            assert domain7.getSqlType() == SecQueryType.SELECT && domain7.getAuditKind() == SecQueryKind.QUERY && domain7.getMode() == RdbQueryMode.UNION;
            assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("table_2") &&//
                   domain7.getSelectColumns().size() == 0 &&//
                   domain7.getSelectVariables().size() == 0 &&//
                   domain7.getSelectFunc().size() == 0 &&//
                   domain7.getWhereColumns().size() == 0;
            assert !domain7.isHasAs() &&         //
                   domain7.isHasSelectAll() &&   //
                   !domain7.isSelectInSelect() &&//
                   !domain7.isFuncInSelect() &&  //
                   !domain7.isSelectInFrom() &&  //
                   !domain7.isEmptyFrom() &&     //
                   domain7.isSimpleSelect();
            assert !domain7.isHasWhere() &&      //
                   !domain7.isSelectInWhere() && //
                   domain7.getJoinType() == RdbJoinType.NONE &&//
                   !domain7.isHasUnion() &&      //
                   !domain7.isHasWith();
            assert !domain7.isHasLimit();
        }
    }

    @Test
    public void union_5_1() {
        String sql = "select id = (select top(1) a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id) from table_1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 7;
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
        {
            MsSelectDomain domain3 = (MsSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   !domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
        {
            MsSelectDomain domain4 = (MsSelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("b") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&   //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }
        {
            MsSelectDomain domain5 = (MsSelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable() == null &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   !domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   !domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
            assert !domain5.isHasLimit();
        }
        {
            MsSelectDomain domain6 = (MsSelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.UNION;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table_1") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom() &&     //
                   domain6.isSimpleSelect();
            assert !domain6.isHasWhere() &&      //
                   !domain6.isSelectInWhere() && //
                   domain6.getJoinType() == RdbJoinType.NONE &&//
                   !domain6.isHasUnion() &&      //
                   !domain6.isHasWith();
            assert !domain6.isHasLimit();
        }
        {
            MsSelectDomain domain7 = (MsSelectDomain) domainList.get(6);
            assert domain7.getSqlType() == SecQueryType.SELECT && domain7.getAuditKind() == SecQueryKind.QUERY && domain7.getMode() == RdbQueryMode.UNION;
            assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("table_2") &&//
                   domain7.getSelectColumns().size() == 0 &&//
                   domain7.getSelectVariables().size() == 0 &&//
                   domain7.getSelectFunc().size() == 0 &&//
                   domain7.getWhereColumns().size() == 0;
            assert !domain7.isHasAs() &&         //
                   domain7.isHasSelectAll() &&   //
                   !domain7.isSelectInSelect() &&//
                   !domain7.isFuncInSelect() &&  //
                   !domain7.isSelectInFrom() &&  //
                   !domain7.isEmptyFrom() &&     //
                   domain7.isSimpleSelect();
            assert !domain7.isHasWhere() &&      //
                   !domain7.isSelectInWhere() && //
                   domain7.getJoinType() == RdbJoinType.NONE &&//
                   !domain7.isHasUnion() &&      //
                   !domain7.isHasWith();
            assert !domain7.isHasLimit();
        }
    }

    @Test
    public void union_6_1() {
        String sql = "select * from table1 union select * from table2;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table1") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            MsSelectDomain domain3 = (MsSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table2") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&   //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
    }

    @Test
    public void union_6_2() {
        String sql = "select * from table1 union  select * from table2 union all select * from table3 union  select * from table4";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/table3/");
            assert resList.get(3).getType() == TargetType.Table &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_db/test_schema/table4/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 7;
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            MsSelectDomain domain3 = (MsSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable() == null &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   !domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   domain3.isHasUnion() &&       //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
        //
        {
            MsSelectDomain domain4 = (MsSelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.UNION;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("table1") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   domain4.isHasSelectAll() &&   //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }
        {
            MsSelectDomain domain5 = (MsSelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.UNION;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table2") &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
            assert !domain5.isHasLimit();
        }
        {
            MsSelectDomain domain6 = (MsSelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.UNION;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table3") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom() &&     //
                   domain6.isSimpleSelect();
            assert !domain6.isHasWhere() &&      //
                   !domain6.isSelectInWhere() && //
                   domain6.getJoinType() == RdbJoinType.NONE &&//
                   !domain6.isHasUnion() &&      //
                   !domain6.isHasWith();
            assert !domain6.isHasLimit();
        }
        {
            MsSelectDomain domain7 = (MsSelectDomain) domainList.get(6);
            assert domain7.getSqlType() == SecQueryType.SELECT && domain7.getAuditKind() == SecQueryKind.QUERY && domain7.getMode() == RdbQueryMode.UNION;
            assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("table4") &&//
                   domain7.getSelectColumns().size() == 0 &&//
                   domain7.getSelectVariables().size() == 0 &&//
                   domain7.getSelectFunc().size() == 0 &&//
                   domain7.getWhereColumns().size() == 0;
            assert !domain7.isHasAs() &&         //
                   domain7.isHasSelectAll() &&   //
                   !domain7.isSelectInSelect() &&//
                   !domain7.isFuncInSelect() &&  //
                   !domain7.isSelectInFrom() &&  //
                   !domain7.isEmptyFrom() &&     //
                   domain7.isSimpleSelect();
            assert !domain7.isHasWhere() &&      //
                   !domain7.isSelectInWhere() && //
                   domain7.getJoinType() == RdbJoinType.NONE &&//
                   !domain7.isHasUnion() &&      //
                   !domain7.isHasWith();
            assert !domain7.isHasLimit();
        }
    }

    @Test
    public void with_query_1() {
        String sql = "with tab1Cnt as (select * from table_1)," + //
                     "     tab2Cnt as (select * from table_2)" +//
                     "select * from tab1Cnt a join tab2Cnt b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 7;
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1.isHasUnion() &&       //
                   domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("tab1Cnt") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            MsSelectDomain domain3 = (MsSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("tab2Cnt") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&   //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   !domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&       //
                   !domain3.isSelectInWhere() &&  //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&       //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
        {
            MsSelectDomain domain4 = (MsSelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.WITH;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("tab1Cnt") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&   //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }
        {
            MsSelectDomain domain5 = (MsSelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.WITH_BODY;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_1") &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&       //
                   !domain5.isSelectInWhere() &&  //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&       //
                   !domain5.isHasWith();
            assert !domain5.isHasLimit();
        }
        {
            MsSelectDomain domain6 = (MsSelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.WITH;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("tab2Cnt") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   !domain6.isHasSelectAll() &&   //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom() &&     //
                   !domain6.isSimpleSelect();
            assert !domain6.isHasWhere() &&       //
                   !domain6.isSelectInWhere() &&  //
                   domain6.getJoinType() == RdbJoinType.NONE &&//
                   !domain6.isHasUnion() &&       //
                   !domain6.isHasWith();
            assert !domain6.isHasLimit();
        }
        {
            MsSelectDomain domain7 = (MsSelectDomain) domainList.get(6);
            assert domain7.getSqlType() == SecQueryType.SELECT && domain7.getAuditKind() == SecQueryKind.QUERY && domain7.getMode() == RdbQueryMode.WITH_BODY;
            assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("table_2") &&//
                   domain7.getSelectColumns().size() == 0 &&//
                   domain7.getSelectVariables().size() == 0 &&//
                   domain7.getSelectFunc().size() == 0 &&//
                   domain7.getWhereColumns().size() == 0;
            assert !domain7.isHasAs() &&         //
                   domain7.isHasSelectAll() &&   //
                   !domain7.isSelectInSelect() &&//
                   !domain7.isFuncInSelect() &&  //
                   !domain7.isSelectInFrom() &&  //
                   !domain7.isEmptyFrom() &&     //
                   domain7.isSimpleSelect();
            assert !domain7.isHasWhere() &&       //
                   !domain7.isSelectInWhere() &&  //
                   domain7.getJoinType() == RdbJoinType.NONE &&//
                   !domain7.isHasUnion() &&       //
                   !domain7.isHasWith();
            assert !domain7.isHasLimit();
        }
    }

    @Test
    public void with_query_5_1() {
        String sql = "select id = (select top(1) a.id from table_1 a ) from table_1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_1") &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&// 
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            //            assert !domain2.isHasLimit(); // todo  top = limit
        }
    }

    @Test
    public void with_query_5_2() {
        String sql = "select (select a.id from table_1 a) as id from table_1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_1") &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }

    }

    @Test
    public void selectAndInQuery_1() {
        String sql = "select id,name from abc where id = 1 and name in('a','b')";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("id", "name"));
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectInAndQuery_1() {
        String sql = "select id,name from abc where name in('a','b') and id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id"));
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectOrInQuery_1() {
        String sql = "select id,name from abc where id = 1 or name in('a','b')";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("id", "name"));
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectInOrQuery_1() {
        String sql = "select id,name from abc where name in('a','b') or id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id"));
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

}
