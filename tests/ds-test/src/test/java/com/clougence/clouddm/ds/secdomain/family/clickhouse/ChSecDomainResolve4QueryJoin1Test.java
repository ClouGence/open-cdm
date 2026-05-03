package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4QueryJoin1Test extends ChSecDomainTestSupport {

    public ChSecDomainResolve4QueryJoin1Test(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void joinQuery_1_1() {
        String sql = "select * from `table_1` a join `table_2` b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1 = (ChSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
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

    @Test
    public void joinQuery_1_2() {
        String sql = "select * from table_1 a join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1 = (ChSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
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

    @Test
    public void joinQuery_1_3() {
        String sql = "select * from table_1 a join table_2 b on (a.id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1 = (ChSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
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

    @Test
    public void joinQuery_2_1() {
        String sql = "select * from table_1, table_2";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.CROSS_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void joinQuery_2_2() {
        String sql = "select * from table_1 a, table_2 b where a.id1 = b.id2";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().size() == 2 && domain1_1.getWhereColumns().equals(Arrays.asList("id1", "id2")) && //
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.CROSS_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert domain1_1.isHasWhere() &&       //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void naturalJoinQuery_1() {
        String sql1 = "select * from table_1 a, table_2 b where a.id1 = b.id2";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        String sql2 = "select * from table_1 a natural join table_2 b on 1=1";
        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql2), contextInfo());
        assert domainList.size() == 1;
        {
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }

    }

    //@Test
    public void naturalJoinQuery_2() {
        //        String sql = "select * from table_1 a natural inner join table_2 b";
        //
        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 2;
        //        {
        //            assert resList.get(0).getType() == TargetType.Table &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
        //            assert resList.get(1).getType() == TargetType.Table &&//
        //                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        //        }
        //
        //        // passer unsupport
        //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 3;
        //        {
        //            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
        //            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
        //            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
        //                   domain1_1.getSelectColumns().size() == 0 &&//
        //                   domain1_1.getSelectVariables().size() == 0 &&//
        //                   domain1_1.getSelectFunc().size() == 0 &&//
        //                   domain1_1.getWhereColumns().size() == 0;
        //            assert !domain1_1.isHasAs() &&         //
        //                   domain1_1.isHasSelectAll() &&   //
        //                   !domain1_1.isSelectInSelect() &&//
        //                   !domain1_1.isFuncInSelect() &&  //
        //                   !domain1_1.isSelectInFrom() &&  //
        //                   !domain1_1.isEmptyFrom() &&     //
        //                   !domain1_1.isSimpleSelect();
        //            assert !domain1_1.isHasWhere() &&      //
        //                   !domain1_1.isSelectInWhere() && //
        //                   domain1_1.getJoinType() == RdbJoinType.INNER_JOIN && domain1_1.getOptions().get(MySecDomainOptionKeys.OPT_JOIN_NATURAL).equals("true") &&//
        //                   !domain1_1.isHasUnion() &&      //
        //                   !domain1_1.isHasWith();
        //            assert !domain1_1.isHasLimit();
        //        }
        //        {
        //            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
        //            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
        //            assert domain1_2.getCatalog() == null  && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
        //                   domain1_2.getSelectColumns().size() == 0 &&//
        //                   domain1_2.getSelectVariables().size() == 0 &&//
        //                   domain1_2.getSelectFunc().size() == 0 &&//
        //                   domain1_2.getWhereColumns().size() == 0;
        //            assert !domain1_2.isHasAs() &&          //
        //                   !domain1_2.isHasSelectAll() &&   //
        //                   !domain1_2.isSelectInSelect() &&//
        //                   !domain1_2.isFuncInSelect() &&  //
        //                   !domain1_2.isSelectInFrom() &&  //
        //                   !domain1_2.isEmptyFrom() &&     //
        //                   !domain1_2.isSimpleSelect();
        //            assert !domain1_2.isHasWhere() &&      //
        //                   !domain1_2.isSelectInWhere() && //
        //                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
        //                   !domain1_2.isHasUnion() &&      //
        //                   !domain1_2.isHasWith();
        //            assert !domain1_2.isHasLimit();
        //        }
        //        {
        //            MySelectDomain domain1_3 = (MySelectDomain) domainList.get(2);
        //            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
        //            assert domain1_3.getCatalog() == null  && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
        //                   domain1_3.getSelectColumns().size() == 0 &&//
        //                   domain1_3.getSelectVariables().size() == 0 &&//
        //                   domain1_3.getSelectFunc().size() == 0 &&//
        //                   domain1_3.getWhereColumns().size() == 0;
        //            assert !domain1_3.isHasAs() &&         //
        //                   !domain1_3.isHasSelectAll() &&   //
        //                   !domain1_3.isSelectInSelect() &&//
        //                   !domain1_3.isFuncInSelect() &&  //
        //                   !domain1_3.isSelectInFrom() &&  //
        //                   !domain1_3.isEmptyFrom() &&     //
        //                   !domain1_3.isSimpleSelect();
        //            assert !domain1_3.isHasWhere() &&      //
        //                   !domain1_3.isSelectInWhere() && //
        //                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
        //                   !domain1_3.isHasUnion() &&      //
        //                   !domain1_3.isHasWith();
        //            assert !domain1_3.isHasLimit();
        //        }
    }

    @Test
    public void naturalJoinQuery_3() {
        String sql = "select * from table_1 a natural cross join table_2 b";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.CROSS_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void naturalJoinQuery_4() {
        String sql = "select * from table_1 a natural left join table_2 b on 1=1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //domain1_1.getOptions().get(MySecDomainOptionKeys.OPT_JOIN_NATURAL).equals("true") &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }

    }

    //@Test
    public void naturalJoinQuery_5() {
        // TODO druid has bug.
        //        List<RuleDomain> list5 = resolveSpi.resolveDomain(dataSourceType, "select * from table_1 a natural right join table_2 b");
        //        MyQueryDomain domain5 = (MyQueryDomain) list5.get(0);
        //        assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getDdlKind() == SqlDdlKind.QUERY;
        //        assert domain5.getJoinType() == RdbJoinType.RIGHT_JOIN;
        //        assert !domain5.isEmptyFrom() && !domain5.isHasSubQuery();
        //        assert domain5.getCatalog() == null  && domain5.getSchema() == null && domain5.getTable().equals("table_2");
        //        assert domain5.getOptions().get(MySecDomainOptionKeys.OPT_JOIN_NATURAL).equals("true");
    }

    @Test
    public void multiJoinQuery_1() {
        String sql = "select * from table_1 a left join table_2 b on a.id = b.id right join table_3 c on a.id = c.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_3/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 2 && domain1_1.getJoinTypes().containsAll(Arrays.asList(RdbJoinType.LEFT_JOIN, RdbJoinType.RIGHT_JOIN));
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }

    }

    @Test
    public void innerJoinQuery_1_1() {
        String sql = "select * from table_1 a inner join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void innerJoinQuery_1_2() {
        String sql = "select * from table_1 a inner join table_2 b on 1=1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }

    }

    @Test
    public void innerJoinQuery_1_3() {
        String sql = "select * from table_1 a inner join table_2 b on (a.id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }

    }

    @Test
    public void crossJoinQuery_1_1() {
        String sql = "select * from table_1 a cross join table_2 b";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.CROSS_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void crossJoinQuery_1_2() {
        String sql = "select * from table_1 a cross join table_2 b";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.CROSS_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void crossJoinQuery_1_3() {
        String sql = "select * from table_1 a cross join table_2 b";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() && //
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.CROSS_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void leftJoinQuery_1_1() {
        String sql = "select * from table_1 a left join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void leftJoinQuery_1_2() {
        String sql = "select * from table_1 a left join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void leftJoinQuery_1_3() {
        String sql = "select * from table_1 a left join table_2 b on (a.id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void leftJoinQuery_2_1() {
        String sql = "select * from table_1 a left outer join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void leftJoinQuery_2_2() {
        String sql = "select * from table_1 a left outer join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void leftJoinQuery_2_3() {
        String sql = "select * from table_1 a left outer join table_2 b on (a.id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void rightJoinQuery_1_1() {
        String sql = "select * from table_1 a right join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.RIGHT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void rightJoinQuery_1_2() {
        String sql = "select * from table_1 a right join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.RIGHT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void rightJoinQuery_1_3() {
        String sql = "select * from table_1 a right join table_2 b on (a.id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.RIGHT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void rightJoinQuery_2_1() {
        String sql = "select * from table_1 a right outer join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.RIGHT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void rightJoinQuery_2_2() {
        String sql = "select * from table_1 a right outer join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.RIGHT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }

    @Test
    public void rightJoinQuery_2_3() {
        String sql = "select * from table_1 a right outer join table_2 b on (a.id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            ChSelectDomain domain1_1 = (ChSelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().size() == 1 && domain1_1.getJoinTypes().contains(RdbJoinType.RIGHT_JOIN);
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
    }
}
