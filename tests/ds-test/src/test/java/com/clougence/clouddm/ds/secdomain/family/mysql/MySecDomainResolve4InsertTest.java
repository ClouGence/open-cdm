package com.clougence.clouddm.ds.secdomain.family.mysql;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyInsertDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MySecDomainResolve4InsertTest extends MySecDomainTestSupport {

    public MySecDomainResolve4InsertTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
    }

    @Test
    public void insertInto_1() {
        String sql = "insert into `test_sch`.`table2` (`id`, `b`) values (2, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_sch/table2/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_sch") && domain1.getTable().equals("table2");
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
    public void insertInto_2() {
        String sql = "insert `test_schema`.`table2` (`id`, `b`) values (2, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
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
    public void insertValueSelect() {
        String sql = "insert `test_schema`.`table2` (`id`, `b`) values ((select id from table1 limit 1), 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table1/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getSelectColumns().size() == 1 &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void insertMultipleValues_1() {
        String sql = "insert `test_schema`.`table2` (`id`, `b`) values (2, 1),(3,5),(1,3);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   domain1.isMultipleValues();
        }
    }

    @Test
    public void insertMultipleValues_2() {
        String sql = "insert `test_schema`.`table2` (`id`, `b`) values (2, 1),(3,5),(1,null);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   domain1.isMultipleValues();
        }
    }

    @Test
    public void insertInto_NullValue_1() {
        String sql = "insert into `test_schema`.`table2` (`id`, `b`) values (null, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
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
    public void insertInto_NullValue_2() {
        String sql = "insert into `test_schema`.`table2` (`id`, `b`) value (null, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
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
    public void insertIgnore_1() {
        String sql = "insert ignore into `test_schema`.`table2` (`id`, `b`) values (null, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.IGNORE &&//
                   !domain1.isHasSubQuery() &&  //
                   domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertDuplicate_1() {
        String sql = "insert into `test_schema`.`table2` (`id`, `b`) values (null, 1) ON DUPLICATE KEY UPDATE b = 1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(DataSourceType.PolarDbX, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.UPDATE &&//
                   !domain1.isHasSubQuery() &&  //
                   domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertFromSelect_1() {
        String sql = "insert into `test_sch`.`table2` select * from `table`;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_sch/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_sch") && domain1.getTable().equals("table2");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(1);
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
        String sql = "insert into `table2` " + //
                     "with tab1Cnt as (select id,name from table1) " + //
                     "select * from tab1Cnt;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table1/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table2");
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
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty();
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
            assert !domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().size() == 2 &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty();
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
            assert !domain3.isHasLimit();
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
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/orders1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/orders2/");
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
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table2");
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
            MySelectDomain domain3 = (MySelectDomain) domainList.get(1);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.INSERT;
            assert domain3.getSelectColumns().size() == 3 && domain3.getSelectColumns().equals(Arrays.asList("customer_id", "order_date", "total_amount")) &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty();
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
            assert !domain3.isHasLimit();
        }
        {
            MySelectDomain domain4 = (MySelectDomain) domainList.get(2);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.INSERT;
            assert domain4.getSelectColumns().size() == 3 && domain4.getSelectColumns().equals(Arrays.asList("customer_id", "order_date", "total_amount")) &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getWhereColumns().isEmpty();
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }
    }
}
