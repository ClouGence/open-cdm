package com.clougence.clouddm.ds.secdomain.family.postgres;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgInsertDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgSecDomainResolve4InsertTest extends PgSecDomainTestSupport {

    public PgSecDomainResolve4InsertTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PostgreSQL;
    }

    @Test
    public void insertInto_1() {
        String sql = "insert into \"pub\".\"test_schema\".\"table2\" (\"id\", \"b\") values (2, 1);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain1 = (PgInsertDomain) domainList.get(0);
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
        String sql = "insert into pub.test_schema.table2 (id, b) values (2, 1);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain2 = (PgInsertDomain) domainList.get(0);
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
        String sql = "insert into test_schema.table2 (id, b) values ((select id from table1 limit 1), 1);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain1 = (PgInsertDomain) domainList.get(0);
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
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getSelectColumns().size() == 1 &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
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
        String sql = "insert into test_schema.table2 (id, b) values (2, 1),(3,5),(1.1,3);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain1 = (PgInsertDomain) domainList.get(0);
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

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain2 = (PgInsertDomain) domainList.get(0);
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

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain1 = (PgInsertDomain) domainList.get(0);
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
    public void insertIgnore_1() {
        String sql = "insert into test_schema.table2 (id, b) values (null, 1) on conflict do nothing;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain1 = (PgInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   !domain1.isHasSubQuery() &&  //
                   domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
            assert domain1.getConflict() == RdbInsertConflictStrategy.IGNORE;
        }
    }

    @Test
    public void insertConflictUpdate_1() {
        String sql = "insert into pub.test_schema.table2 (id, b) values (null, 1) ON CONFLICT (id) DO UPDATE SET (id, b) = (excluded.id, excluded.b);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain1 = (PgInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog().equals("pub") && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("table2");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("b");
            assert !domain1.isFromSelect() &&   //
                   !domain1.isHasSubQuery() &&  //
                   domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
            assert domain1.getConflict() == RdbInsertConflictStrategy.UPDATE;
        }
    }

    @Test
    public void insertFromSelect_1() {
        String sql = "insert into test_schema.table2 select * from \"table\";";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
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
            PgInsertDomain domain1 = (PgInsertDomain) domainList.get(0);
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.INSERT;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
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
        String sql1 = "insert into table2 " + //
                      "with tab1Cnt as (select id,name from table1) " + //
                      "select * from tab1Cnt;";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql1), contextInfo());
        assert list1.size() == 3;
        {
            PgInsertDomain domain1 = (PgInsertDomain) list1.get(0);
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
            PgSelectDomain domain2 = (PgSelectDomain) list1.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
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
            assert !domain2.isHasLimit();
        }
        {
            PgSelectDomain domain4 = (PgSelectDomain) list1.get(2);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getSelectColumns().size() == 2 && domain4.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getSelectValue().isEmpty() &&//
                   domain4.getWhereColumns().isEmpty() &&//
                   domain4.getJoinTypes().isEmpty();
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }
    }
}
