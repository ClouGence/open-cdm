package com.clougence.clouddm.ds.secdomain.family.doris;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.doris.analysis.DrResAnalysisSpi;
import com.clougence.clouddm.ds.doris.analysis.DrSecDomainResolveSpi;
import com.clougence.clouddm.ds.doris.analysis.DrSplitAnalysisSpi;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrSelectDomain;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrSecDomainResolve4UpdateTest extends DrSecDomainTestSupport {

    public DrSecDomainResolve4UpdateTest(){
        this.analysisSpi = new DrResAnalysisSpi(null);
        this.resolveSpi = new DrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new DrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Doris;
    }

    @Test
    public void update_1() {
        String sql = "update `table` set id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            DrUpdateDomain domain1 = (DrUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().contains("id") &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert !domain1.isHasWhere() &&     //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void update_2() {
        String sql = "update a.b.`table` set id = 1 where id = 2 or name = 3 and zz =4";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/a/b/table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            DrUpdateDomain domain1 = (DrUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog().equals("a") && domain1.getSchema().equals("b") && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().contains("id") &&//
                   domain1.getWhereColumns().size() == 3 && domain1.getWhereColumns().equals(Arrays.asList("id", "name", "zz"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void update_3() {
        String sql = "update `table` set id = 1,age = 2 where name = 'name'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            DrUpdateDomain domain1 = (DrUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 2 && domain1.getSetColumns().equals(Arrays.asList("id", "age")) &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().equals(Arrays.asList("name"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void update_where_using_cast_1() {
        String sql = "update table1 set name = 1 where id = cast(id AS SIGNED)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/cast/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            DrUpdateDomain domain1 = (DrUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table1") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("name")) &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().equals(Arrays.asList("id"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("cast") &&//
                   domain2.getArgs().contains("id");
        }
    }

    @Test
    public void update_where_using_cast_2() {
        String sql = "update table1 set name = 1 where id = cast(id AS SIGNED)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/cast/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            DrUpdateDomain domain1_1 = (DrUpdateDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.UPDATE && domain1_1.getAuditKind() == SecQueryKind.DML;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("table1") &&//
                   domain1_1.getSetColumns().size() == 1 && domain1_1.getSetColumns().equals(Arrays.asList("name")) &&//
                   domain1_1.getWhereColumns().size() == 1 && domain1_1.getWhereColumns().equals(Arrays.asList("id"));
            assert !domain1_1.isSelectInSet() &&  //
                   !domain1_1.isMultiUpdate();
            assert domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() &&//
                   !domain1_1.isHasUnion() &&     //
                   !domain1_1.isHasWith();
        }
        {
            RdbCallDomain domain1_2 = (RdbCallDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CALL && domain1_2.getAuditKind() == SecQueryKind.CALL;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getCallName().equalsIgnoreCase("cast") &&//
                   domain1_2.getArgs().contains("id");
        }
    }

    @Test
    public void update_where_using_cast_3() {
        String sql = "update table1 set name = 1 where id = cast(id AS SIGNED)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/cast/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            DrUpdateDomain domain2_1 = (DrUpdateDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.UPDATE && domain2_1.getAuditKind() == SecQueryKind.DML;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema() == null && domain2_1.getTable().equals("table1") &&//
                   domain2_1.getSetColumns().size() == 1 && domain2_1.getSetColumns().equals(Arrays.asList("name")) &&//
                   domain2_1.getWhereColumns().size() == 1 && domain2_1.getWhereColumns().equals(Arrays.asList("id"));
            assert !domain2_1.isSelectInSet() &&  //
                   !domain2_1.isMultiUpdate();
            assert domain2_1.isHasWhere() &&      //
                   !domain2_1.isSelectInWhere() &&//
                   !domain2_1.isHasUnion() &&     //
                   !domain2_1.isHasWith();
        }
        {
            RdbCallDomain domain2_2 = (RdbCallDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CALL && domain2_2.getAuditKind() == SecQueryKind.CALL;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema() == null && domain2_2.getCallName().equalsIgnoreCase("cast") &&//
                   domain2_2.getArgs().contains("id");
        }
    }

    @Test
    public void update_where_using_func_1() {
        String sql = "update table1 set name = 1 where id = avg((select id from table2))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/avg/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            DrUpdateDomain domain1 = (DrUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table1") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("name")) &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().equals(Arrays.asList("id"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("avg") &&//
                   domain2.getArgs().get(0).contains("(select id from table2)");
        }
        {
            DrSelectDomain domain3 = (DrSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_CALL;
            assert domain3.getSelectColumns().size() == 1 && domain3.getSelectColumns().contains("id") &&//
                   domain3.getWhereColumns().size() == 0 && domain3.getWhereColumns().isEmpty();
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
    public void update_set_include_subquery_1() {
        String sql = "update `table` set id = (select id2 from test limit 1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            DrUpdateDomain domain1 = (DrUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("id")) &&//
                   domain1.getWhereColumns().size() == 0;
            assert domain1.isSelectInSet() &&   //
                   !domain1.isMultiUpdate();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            DrSelectDomain domain2 = (DrSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SET;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id2") &&//
                   domain2.getWhereColumns().size() == 0 && domain2.getWhereColumns().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void update_where_include_subquery_1() {
        String sql = "update `table` set id = 2 where name = (select id2 from test limit 1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            DrUpdateDomain domain1 = (DrUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("id")) &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("name");
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            DrSelectDomain domain2 = (DrSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id2") &&//
                   domain2.getWhereColumns().size() == 0 && domain2.getWhereColumns().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void update_using_withQuery_1() {
        String sql = "with tab1Cnt as (select * from table1)\n" +//
                     "update table2 set cc = 1 where id1 in (select id_1 from tab1Cnt) or id2 in (select id_2 from table3);";
        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/table3/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            DrUpdateDomain domain1 = (DrUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table2") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("cc")) &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("id1", "id2"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&     //
                   domain1.isHasWith();
        }
        {
            DrSelectDomain domain2 = (DrSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id_1") &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            DrSelectDomain domain3 = (DrSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
        {
            DrSelectDomain domain4 = (DrSelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain4.getSelectColumns().size() == 1 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }
    }
}
