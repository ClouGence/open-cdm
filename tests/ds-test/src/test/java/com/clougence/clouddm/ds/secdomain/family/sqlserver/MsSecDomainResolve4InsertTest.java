package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsInsertDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4InsertTest extends MsSecDomainTestSupport {

    public MsSecDomainResolve4InsertTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void insert_1() {
        String sql = "insert [pub].[test_schema].[table2] ([id], [b]) values (2, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/pub/test_schema/table2/");
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
            MsInsertDomain domain1 = (MsInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog().equals("pub") && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertInto_1() {
        String sql = "insert into [pub].[test_schema].[table2] ([id], [b]) values (2, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/pub/test_schema/table2/");
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
            MsInsertDomain domain1 = (MsInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog().equals("pub") && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertInto_2() {
        String sql = "insert into pub.test_schema.table2 (id, b) values (2, default);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/pub/test_schema/table2/");
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
            MsInsertDomain domain2 = (MsInsertDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.INSERT && domain2.getAuditKind() == SecQueryKind.DML;
            assert domain2.getCatalog().equals("pub") && domain2.getSchema().equals("test_schema") && domain2.getTable().equals("table2");
            assert domain2.getColumns().contains("id") && domain2.getColumns().contains("b");
            assert !domain2.isFromSelect() &&   //
                   !domain2.isHasSubQuery() &&  //
                   !domain2.isHasNullValue() && //
                   !domain2.isOnlyValues() &&   //
                   !domain2.isHasWith() &&      //
                   !domain2.isMultipleValues();
        }
    }

    @Test
    public void insertValueSelect() {
        String sql = "insert test_schema.table2 (id, b) values ((select top 1 id from table1), 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
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
            MsInsertDomain domain1 = (MsInsertDomain) domainList.get(0);
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
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table1") &&//
                   domain2.getSelectColumns().size() == 1 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void insertMultipleValues_1() {
        String sql = "insert into test_schema.table2 (id, b) values (2, 1),(3,5),(1,3);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
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
            MsInsertDomain domain1 = (MsInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   domain1.isMultipleValues();
        }
    }

    @Test
    public void insertMultipleValues_2() {
        String sql = "insert into test_schema.table2 (id, b) values (2, 1),(3,5),(1,null);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
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
            MsInsertDomain domain2 = (MsInsertDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.INSERT && domain2.getAuditKind() == SecQueryKind.DML;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_schema") && domain2.getTable().equals("table2");
            assert domain2.getColumns().contains("id") && domain2.getColumns().contains("b");
            assert !domain2.isFromSelect() &&   //
                   !domain2.isHasSubQuery() &&  //
                   domain2.isHasNullValue() &&  //
                   !domain2.isOnlyValues() &&   //
                   !domain2.isHasWith() &&      //
                   domain2.isMultipleValues();
        }
    }

    @Test
    public void insertInto_NullValue_1() {
        String sql = "insert into test_schema.table2 (id, b) values (null, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
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
            MsInsertDomain domain1 = (MsInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   !domain1.isHasSubQuery() &&  //
                   domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertFromSelect_1() {
        String sql = "insert into test_schema.table2 select * from [table];";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table/");
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
            MsInsertDomain domain1 = (MsInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().isEmpty();
            assert domain1.isFromSelect() &&    //
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   domain1.isOnlyValues() &&    //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
        {
            MsSelectDomain domain1 = (MsSelectDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.INSERT;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void insertDefaultValue_1() {
        String sql = "insert into test_schema.table2 default values;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
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
            MsInsertDomain domain1 = (MsInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().isEmpty();
            assert !domain1.isFromSelect() &&    //
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   domain1.isOnlyValues() &&    //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

}
