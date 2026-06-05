package com.clougence.clouddm.ds.secdomain.family.db2;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2ResAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2InsertDomain;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2SelectDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2SecDomainResolve4InsertTest extends Db2SecDomainTestSupport {

    public Db2SecDomainResolve4InsertTest(){
        this.analysisSpi = new Db2ResAnalysisSpi();
        this.resolveSpi = new Db2SecDomainResolveSpi();
        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Db2;
    }

    @Test
    public void insertInto_1() {
        String sql = "insert into \"test_schema\".\"table2\" (\"id\", \"b\") values (2, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2InsertDomain domain1 = (Db2InsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
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
        String sql = "insert into test_schema.table2 (id, b) values (2, 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2InsertDomain domain2 = (Db2InsertDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.INSERT && domain2.getAuditKind() == SecQueryKind.DML;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_schema") && domain2.getTable().equals("table2");
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
        String sql = "insert test_schema.table2 (id, b) values ((select id from table1), 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table1/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2InsertDomain domain1 = (Db2InsertDomain) domainList.get(0);
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
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
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
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void insertMultipleValues_1() {
        String sql = "insert into test_schema.table2 (id, b) values (2, 1),(3,5),(1,3);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2InsertDomain domain1 = (Db2InsertDomain) domainList.get(0);
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
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2InsertDomain domain2 = (Db2InsertDomain) domainList.get(0);
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
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2InsertDomain domain1 = (Db2InsertDomain) domainList.get(0);
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

    //    @Test
    //    public void insertIgnore_1() {
    //        String sql = "insert into test_schema.table2 (id, b) values (null, 1) on conflict do nothing;";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.Table &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
    //        }
    //
    //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            Db2InsertDomain domain1 = (Db2InsertDomain) domainList.get(0);
    //            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getSqlKind() == SqlKind.DML;
    //            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
    //            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
    //            assert !domain1.isFromSelect() &&   //
    //                   !domain1.isHasSubQuery() &&  //
    //                   domain1.isHasNullValue() &&  //
    //                   !domain1.isOnlyValues() &&   //
    //                   !domain1.isHasWith() &&      //
    //                   !domain1.isMultipleValues();
    //            assert domain1.getConflict() == RdbInsertConflictStrategy.IGNORE;
    //        }
    //    }
    //
    //    @Test
    //    public void insertConflictUpdate_1() {
    //        String sql = "insert into pub.test_schema.table2 (id, b) values (null, 1) ON CONFLICT (id) DO UPDATE SET (id, b) = (excluded.id, excluded.b);";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 1;
    //        {
    //            assert resList.get(0).getType() == TargetType.Table &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/pub/test_schema/table2/");
    //        }
    //
    //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            Db2InsertDomain domain1 = (Db2InsertDomain) domainList.get(0);
    //            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getSqlKind() == SqlKind.DML;
    //            assert domain1.getCatalog().equals("pub") && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
    //            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
    //            assert !domain1.isFromSelect() &&   //
    //                   !domain1.isHasSubQuery() &&  //
    //                   domain1.isHasNullValue() &&  //
    //                   !domain1.isOnlyValues() &&   //
    //                   !domain1.isHasWith() &&      //
    //                   !domain1.isMultipleValues();
    //            assert domain1.getConflict() == RdbInsertConflictStrategy.UPDATE;
    //        }
    //    }

    @Test
    public void insertFromSelect_1() {
        String sql = "insert into test_schema.table2 select * from \"table\";";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table2/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2InsertDomain domain1 = (Db2InsertDomain) domainList.get(0);
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
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(1);
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
        }
    }

    @Test
    public void insertFromSelect_2() {
        String sql = "insert into table2 " + //
                     "with tab1Cnt as (select id,name from table1) " + //
                     "select * from tab1Cnt;";

        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert list1.size() == 4;
        {
            Db2InsertDomain domain1 = (Db2InsertDomain) list1.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table2");
            assert domain1.getColumns().isEmpty();
            assert domain1.isFromSelect() &&    //
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   domain1.isOnlyValues() &&    //
                   !domain1.isHasWith() &&       //
                   !domain1.isMultipleValues();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) list1.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("tab1Cnt") &&//
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
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&       //
                   domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) list1.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.WITH;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("tab1Cnt") &&//
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
            Db2SelectDomain domain4 = (Db2SelectDomain) list1.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.WITH_BODY;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("table1") &&//
                   domain4.getSelectColumns().size() == 2 && domain4.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getSelectValue().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        //        }
    }
}
