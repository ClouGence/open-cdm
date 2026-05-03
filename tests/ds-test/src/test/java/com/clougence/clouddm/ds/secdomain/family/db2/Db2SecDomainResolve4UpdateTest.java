package com.clougence.clouddm.ds.secdomain.family.db2;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2ResAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2SelectDomain;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2UpdateDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2SecDomainResolve4UpdateTest extends Db2SecDomainTestSupport {

    public Db2SecDomainResolve4UpdateTest(){
        this.analysisSpi = new Db2ResAnalysisSpi();
        this.resolveSpi = new Db2SecDomainResolveSpi();
        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Db2;
    }

    @Test
    public void update_1() {
        String sql = "update \"table\" set id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2UpdateDomain domain1 = (Db2UpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().contains("id") &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert !domain1.isHasWhere() &&     //
                   !domain1.isSelectInWhere() &&//
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void update_2() {
        String sql = "update \"table\" set id = 1 where id = 2 or name = 3 and zz =4";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2UpdateDomain domain1 = (Db2UpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().contains("id") &&//
                   domain1.getWhereColumns().size() == 3 && domain1.getWhereColumns().equals(Arrays.asList("id", "name", "zz"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void update_3() {
        String sql = "update \"table\" set id = 1,age = 2 where name = 'name'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2UpdateDomain domain1 = (Db2UpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 2 && domain1.getSetColumns().equals(Arrays.asList("id", "age")) &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().equals(Arrays.asList("name"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void update_where_using_cast_1() {
        String sql = "update table1 set name = 1 where id = cast(id AS int)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/CAST/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2UpdateDomain domain1 = (Db2UpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table1") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("name")) &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().equals(Arrays.asList("id"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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
        String sql = "update table1 set name = 1 where id = cast(id AS int)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/CAST/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2UpdateDomain domain1_1 = (Db2UpdateDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.UPDATE && domain1_1.getAuditKind() == SecQueryKind.DML;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("table1") &&//
                   domain1_1.getSetColumns().size() == 1 && domain1_1.getSetColumns().equals(Arrays.asList("name")) &&//
                   domain1_1.getWhereColumns().size() == 1 && domain1_1.getWhereColumns().equals(Arrays.asList("id"));
            assert !domain1_1.isSelectInSet() &&  //
                   !domain1_1.isMultiUpdate();
            assert domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() &&//
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
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
    public void update_where_using_func_1() {
        String sql = "update table1 set name = 1 where id = avg((select id from table2))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/avg/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table2/");
        }
        //
        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2UpdateDomain domain1 = (Db2UpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table1") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("name")) &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().equals(Arrays.asList("id"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("avg") &&//
                   domain2.getArgs().get(0).contains("SELECT id");
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table2") &&//
                   domain3.getSelectColumns().size() == 1 && domain3.getSelectColumns().contains("id") &&//
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
        }
    }

    @Test
    public void update_set_include_subquery_1() {
        String sql = "update \"table\" set id = (select id2 from test limit 1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2UpdateDomain domain1 = (Db2UpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("id")) &&//
                   domain1.getWhereColumns().size() == 0;
            assert domain1.isSelectInSet() &&   //
                   !domain1.isMultiUpdate();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SET;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("test") &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id2") &&//
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
        }
    }

    @Test
    public void update_where_include_subquery_1() {
        String sql = "update \"table\" set id = 2 where table.name = (select id2 from test limit 1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2UpdateDomain domain1 = (Db2UpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().equals(Arrays.asList("id")) &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("name");
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("test") &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id2") &&//
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
        }
    }
}
