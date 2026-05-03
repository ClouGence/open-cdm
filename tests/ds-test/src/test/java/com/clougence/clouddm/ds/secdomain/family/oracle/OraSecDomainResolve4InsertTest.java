package com.clougence.clouddm.ds.secdomain.family.oracle;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraInsertDomain;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraSecDomainResolve4InsertTest extends OraSecDomainTestSupport {

    public OraSecDomainResolve4InsertTest(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Oracle;
    }

    @Test
    public void insertInto_1() {
        String sql = "insert into \"test_sch\".\"table2\" (\"id\", \"b\") values (2, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equalsIgnoreCase("/test_sch/table2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraInsertDomain domain1 = (OraInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equalsIgnoreCase("test_sch") && domain1.getTable().equalsIgnoreCase("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertIntoDefault_1() {
        String sql = "insert into \"test_sch\".\"table2\" (\"id\", \"b\") values (default, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equalsIgnoreCase("/test_sch/table2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraInsertDomain domain1 = (OraInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equalsIgnoreCase("test_sch") && domain1.getTable().equalsIgnoreCase("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertInto_NullValue_1() {
        String sql = "insert into \"test_schema\".\"table2\" (\"id\", \"b\") values (null, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/table2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraInsertDomain domain1 = (OraInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equalsIgnoreCase("test_schema") && domain1.getTable().equalsIgnoreCase("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertIntoFunction_1() {
        String sql = "insert into \"test_schema\".\"table2\" (\"id\", \"b\") values (\"testFunc\"(1), 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/testFunc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            OraInsertDomain domain1 = (OraInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equalsIgnoreCase("test_schema") && domain1.getTable().equalsIgnoreCase("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getArgs().size() == 1 && domain2.getArgs().contains("1");
        }
    }

    @Test
    public void insertFromSelect_1() {
        String sql = "insert into \"test_sch\".\"table2\" select * from table;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equalsIgnoreCase("/test_sch/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            OraInsertDomain domain1 = (OraInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equalsIgnoreCase("test_sch") && domain1.getTable().equalsIgnoreCase("table2");
            assert domain1.getColumns().isEmpty();
            assert domain1.isFromSelect() &&    //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   domain1.isOnlyValues() &&    //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.INSERT;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void insertFromSelect_2() {
        String sql = "insert into \"table2\" " + //
                     "with tab1Cnt as (select id,name from table1) " + //
                     "select * from tab1Cnt;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/table1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            OraInsertDomain domain1 = (OraInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equalsIgnoreCase("table2");
            assert domain1.getColumns().isEmpty();
            assert domain1.isFromSelect() &&    //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   domain1.isOnlyValues() &&    //
                   !domain1.isHasWith() &&       //
                   !domain1.isMultipleValues();
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   domain2.isHasWith();
        }
        {
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().size() == 2 && domain3.getSelectColumns().containsAll(Arrays.asList("id", "name")) &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() && //
                   domain3.getJoinTypes().isEmpty();
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&   //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom();
            assert !domain3.isHasWhere() &&       //
                   !domain3.isSelectInWhere() &&  //
                   !domain3.isHasUnion() &&       //
                   !domain3.isHasWith();
        }

    }

    @Test
    public void insertFromSelect_3() {
        String sql = "insert into table2 (customer_id, order_date, total_amount)\n" +//
                     "select customer_id, order_date, total_amount from orders1\n" +//
                     "union\n" +//
                     "select customer_id, order_date, total_amount from orders2;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/orders1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equalsIgnoreCase("/test_schema/orders2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            OraInsertDomain domain1 = (OraInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equalsIgnoreCase("table2");
            assert domain1.getColumns().equals(Arrays.asList("customer_id", "order_date", "total_amount"));
            assert domain1.isFromSelect() &&    //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&       //
                   !domain1.isMultipleValues();
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getSelectColumns().size() == 3 && domain2.getSelectColumns().containsAll(Arrays.asList("customer_id", "order_date", "total_amount")) &&//
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
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.isHasUnion() &&        //
                   !domain2.isHasWith();
        }
        {
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.INSERT;
            assert domain3.getSelectColumns().size() == 3 && domain3.getSelectColumns().equals(Arrays.asList("customer_id", "order_date", "total_amount")) &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() && //
                   domain3.getJoinTypes().isEmpty();
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
        }

    }

    //insert into (select COLUMN_NAME,COLUMN_NAME_2 from JIERUI.TABLE_NAME ) values(1,2)

    // insert all into TABLE_NAME values (2,2,3) into TABLE_NAME values (1,2,3) select 1 from "PUBLIC".DUAL ;
    //
    //insert all when 1=1 then into TABLE_NAME values (2,2,3) else into TABLE_NAME values (1,2,3) select 1 from "PUBLIC".DUAL ;
}
