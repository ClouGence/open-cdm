package com.clougence.clouddm.ds.secdomain.family.mysql;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MySecDomainResolve4QueryJoin2Test extends MySecDomainTestSupport {

    public MySecDomainResolve4QueryJoin2Test(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
    }

    @Test
    public void from_join_1() {
        String sql = "select * from (select 1 id,'name' name) a join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
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
                   domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&    //
                   domain2.getSelectVariables().isEmpty() &&  //
                   domain2.getSelectFunc().isEmpty() &&       //
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getSelectValue().size() == 2 && domain2.getSelectValue().containsAll(Arrays.asList("1", "name"));
            assert domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void from_join_2() {
        String sql = "select * from table_1 a join (select 1 id,'name' name) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
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
                   domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&    //
                   domain2.getSelectVariables().isEmpty() &&  //
                   domain2.getSelectFunc().isEmpty() &&       //
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getSelectValue().size() == 2 && domain2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain2.getJoinTypes().isEmpty();
            assert domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void from_join_3() {
        String sql = "select a.id,b.name from table_1 a join (select id as id1, name from table_2) b on a.id = b.id1";

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
        assert domainList.size() == 2;
        {
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().size() == 2 && domain2.getSelectColumns().containsAll(Arrays.asList("id", "name")) &&    //
                   domain2.getSelectVariables().isEmpty() &&  //
                   domain2.getSelectFunc().isEmpty() &&       //
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().isEmpty();
            assert domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void from_innerJoin_1() {
        String sql = "select * from (select 1 id,'name' name) a inner join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_innerJoin_2() {
        String sql = "select * from table_1 a inner join (select 1 id,'name' name) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_crossJoin_1() {
        String sql = "select * from (select 1 id,'name' name) a cross join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_crossJoin_2() {
        String sql = "select * from table_1 a cross join (select 1 id,'name' name) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_leftJoin_1_1() {
        String sql = "select * from (select 1 id,'name' name) a left join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() && //
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) && //
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_leftJoin_1_2() {
        String sql = "select * from table_1 a left join (select 1 id,'name' name) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_leftJoin_2_1() {
        String sql = "select * from (select 1 id,'name' name) a left outer join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) && //
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_leftJoin_2_2() {
        String sql = "select * from table_1 a left outer join (select 1 id,'name' name) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_rightJoin_1_1() {
        String sql = "select * from (select 1 id,'name' name) a right join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() && //
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_rightJoin_1_2() {
        String sql = "select * from table_1 a right join (select 1 id,'name' name) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() && //
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }

    }

    @Test
    public void from_rightJoin_2_1() {
        String sql = "select * from (select 1 id,'name' name) a right outer join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void from_rightJoin_2_2() {
        String sql = "select * from table_1 a right outer join (select 1 id,'name' name) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
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
                   domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getSelectValue().size() == 2 && domain1_2.getSelectValue().containsAll(Arrays.asList("1", "name")) && //
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }

    }

    @Test
    public void join_at_from_1() {
        String sql = "select * from (select a.id ,b.name from table_1 a left join table_2 b on a.id = b.id) t";

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
        assert domainList.size() == 2;
        {
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() && //
                   domain1_1.getJoinTypes().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().size() == 2 && domain1_2.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getJoinTypes().size() == 1 && domain1_2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void join_at_where_1() {
        String sql = "select * from table_3 where id in (select a.id from table_1 a left join table_2 b on a.id = b.id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().size() == 1 && domain1_1.getWhereColumns().contains("id") && //
                   domain1_1.getJoinTypes().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert domain1_1.isHasWhere() &&       //
                   domain1_1.isSelectInWhere() &&  //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain1_2.getSelectColumns().size() == 1 && domain1_2.getSelectColumns().equals(Arrays.asList("id")) &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getJoinTypes().size() == 1 && domain1_2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void join_at_where_2() {
        String sql = "select * from table_1 where id = CAST((select a.id from table_2 a left join table_3 b on a.id = b.id where a.id = 1 limit 1) as CHAR);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/CAST/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
            assert resList.get(3).getType() == TargetType.Table &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/table_3/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().size() == 1 && domain1_1.getWhereColumns().contains("id") && //
                   domain1_1.getJoinTypes().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            RdbCallDomain domain1_2 = (RdbCallDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CALL && domain1_2.getAuditKind() == SecQueryKind.CALL;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getCallName().equalsIgnoreCase("cast") &&//
                   domain1_2.getArgs().get(0).contains("(select a.id from table_2 a left join table_3 b on a.id = b.id where a.id = 1 limit 1)");
        }
        {
            MySelectDomain domain1_3 = (MySelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_CALL;
            assert domain1_3.getSelectColumns().size() == 1 && domain1_3.getSelectColumns().contains("id") &&//
                   domain1_3.getSelectVariables().isEmpty() &&//
                   domain1_3.getSelectFunc().isEmpty() &&//
                   domain1_3.getWhereColumns().size() == 1 && domain1_3.getWhereColumns().contains("id") &&//
                   domain1_3.getJoinTypes().size() == 1 && domain1_3.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom();
            assert domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
            assert domain1_3.isHasLimit();
        }

    }

    @Test
    public void join_at_select_1() {
        String sql = "select id = (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1) from table_3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().size() == 1 && domain1_1.getSelectColumns().contains("id") &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() && //
                   domain1_1.getJoinTypes().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   !domain1_1.isHasSelectAll() &&  //
                   domain1_1.isSelectInSelect() && //
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain1_2.getSelectColumns().size() == 1 && domain1_2.getSelectColumns().equals(Arrays.asList("id")) &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() && //
                   domain1_2.getJoinTypes().size() == 1 && domain1_2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert domain1_2.isHasLimit();
        }

    }

    @Test
    public void subquery_at_from_1() {
        String sql = "select * from (select * from table_1) t";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() && //
                   domain1_1.getJoinTypes().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() && //
                   domain1_2.getJoinTypes().isEmpty();
            assert !domain1_2.isHasAs() &&         //
                   domain1_2.isHasSelectAll() &&   //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void subquery_at_from_2() {
        String sql = "select * from (select * from table_1) t";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().isEmpty() &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() && //
                   domain1_1.getJoinTypes().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() && domain1_2.getJoinTypes().isEmpty();
            assert !domain1_2.isHasAs() &&         //
                   domain1_2.isHasSelectAll() &&   //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void subquery_at_from_3_1() {
        String sql = "select t.id, t.name from (select * from table_1) t";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().size() == 2 && domain1_1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   !domain1_1.isHasSelectAll() &&  //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&////
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert !domain1_2.isHasAs() &&         //
                   domain1_2.isHasSelectAll() &&   //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void subquery_at_from_3_2() {
        String sql = "select t.id1, t.name from (select id as id1, name from table_1) t";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
            MySelectDomain domain1_1 = (MySelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getSelectColumns().size() == 2 && domain1_1.getSelectColumns().equals(Arrays.asList("id1", "name")) &&//
                   domain1_1.getSelectVariables().isEmpty() &&//
                   domain1_1.getSelectFunc().isEmpty() &&//
                   domain1_1.getWhereColumns().isEmpty() &&//
                   domain1_1.getJoinTypes().isEmpty();
            assert !domain1_1.isHasAs() &&         //
                   !domain1_1.isHasSelectAll() &&  //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
            assert !domain1_1.isHasLimit();
        }
        {
            MySelectDomain domain1_2 = (MySelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getSelectColumns().size() == 2 && domain1_2.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() &&//
                   domain1_2.getJoinTypes().isEmpty();
            assert domain1_2.isHasAs() &&          //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void subquery_at_where_1() {
        String sql = "select * from table_1 where id in (select a.id from table_2)";

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
        assert domainList.size() == 2;
        {
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") && domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void subquery_at_where_2() {
        String sql = "select id, name from table_1 where id in (select a.id from table_2 a left join (select * from table_3) b on a.id = b.id)";

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
        assert domainList.size() == 3;
        {
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() && //
                   domain3.getJoinTypes().isEmpty();
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
    }

    @Test
    public void subquery_at_where_3() {
        String sql = "select id, name from table_1 where id in (select a.id from table_2 a left join (select id as id1, name from table_3) b on a.id = b.id1)";

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
        assert domainList.size() == 3;
        {
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().size() == 2 && domain3.getSelectColumns().containsAll(Arrays.asList("id", "name")) &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() &&//
                   domain3.getJoinTypes().isEmpty();
            assert domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
    }

    @Test
    public void subquery_at_select_1_1() {
        String sql = "select id = (select a.id from table_1 a left join (select * from table_2) b on a.id = b.id limit 1) from table_3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() &&//
                   domain3.getJoinTypes().isEmpty();
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
    }

    @Test
    public void subquery_at_select_1_2() {
        String sql = "select (select a.id from table_1 a left join (select * from table_2) b on a.id = b.id limit 1) = id from table_3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() && //
                   domain3.getJoinTypes().isEmpty();
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
    }

    @Test
    public void subquery_at_select_1_3() {
        String sql = "select (select a.id from table_1 a left join (select id as id1, name from table_2) b on a.id = b.id1 limit 1) = id from table_3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().size() == 2 && domain3.getSelectColumns().containsAll(Arrays.asList("id", "name")) &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() && //
                   domain3.getJoinTypes().isEmpty();
            assert domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
    }

    @Test
    public void subquery_at_select_2_1() {
        String sql = "select id = (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1) from table_3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void subquery_at_select_2_2() {
        String sql = "select (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1) = id from table_3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void subquery_at_select_3() {
        String sql = "select t1.id = (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1)" +//
                     "from (select * from table_3) t1 where t1.id in (select id from table_4)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(3).getType() == TargetType.Table &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/table_4/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.LEFT_JOIN);
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() &&//
                   domain3.getJoinTypes().isEmpty();
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
            MySelectDomain domain4 = (MySelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain4.getSelectColumns().size() == 1 && domain4.getSelectColumns().contains("id") &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getWhereColumns().isEmpty() &&//
                   domain4.getJoinTypes().isEmpty();
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

    //@Test
    public void subquery_at_select_4() {
        //        String sql = "select t1.id = (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1)" +//
        //                     "from (select * from table_1) t1 join (select * from table_1) t2 on t1.id = t2.id" + //
        //                     "where t1.id in (select id from table_1)";
        //
        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 3;
        //        {
        //            assert resList.get(0).getType() == TargetType.Table &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_3/");
        //            assert resList.get(1).getType() == TargetType.Table &&//
        //                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
        //            assert resList.get(2).getType() == TargetType.Table &&//
        //                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
        //        }
        //
        //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
        // assert list1.size() == 6;
        // TODO
    }

    @Test
    public void subquery_at_select_5_1() {
        String sql = "select *,(select id from `key_datatime` b join `two_pk_table` a  on a.`id` = b.`a1` where a.id > 1 limit 1)" +//
                     "from two_pk_table_bak where  1=1 is not null";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/two_pk_table_bak/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/key_datatime/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/two_pk_table/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().size() == 1 && domain2.getWhereColumns().contains("id") &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void subquery_at_select_5_2() {
        String sql = "select *,(select id as id1 from `key_datatime` b join `two_pk_table` a  on a.`id` = b.`a1` where a.id > 1 limit 1)" +//
                     "from two_pk_table_bak where  1=1 is not null";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/two_pk_table_bak/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/key_datatime/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/two_pk_table/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().size() == 1 && domain2.getWhereColumns().contains("id") &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
            assert domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void call_at_select_1() {
        String sql = "select count(*) as c,cast(id AS SIGNED) as b from two_pk_table where  1=1 is not null group by id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/two_pk_table/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/count/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/cast/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().size() == 2 && domain1.getSelectFunc().equals(Arrays.asList("count", "cast")) &&//
                   domain1.getWhereColumns().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("count") &&//
                   domain2.getArgs().get(0).contains("*");
        }
        {
            RdbCallDomain domain3 = (RdbCallDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.CALL && domain3.getAuditKind() == SecQueryKind.CALL;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getCallName().equalsIgnoreCase("cast") &&//
                   domain3.getArgs().get(0).contains("id");
        }
    }

    @Test
    public void call_at_select_2() {
        String sql = "select count(*) as c,CONCAT('1', cast(age AS SIGNED)),cast((select id from table_1 limit 1) AS SIGNED) from two_pk_table where  1=1 is not null group by id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 5;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/two_pk_table/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/count/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/CONCAT/");
            assert resList.get(3).getType() == TargetType.Function &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/cast/");
            assert resList.get(4).getType() == TargetType.Table &&//
                   resList.get(4).toDsResPath().getResPath().equals("/test_schema/table_1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 6;
        {
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("age") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().size() == 3 && domain1.getSelectFunc().equals(Arrays.asList("count", "cast", "CONCAT")) &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() && //
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("count") &&//
                   domain2.getArgs().get(0).contains("*");
        }
        {
            RdbCallDomain domain3 = (RdbCallDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.CALL && domain3.getAuditKind() == SecQueryKind.CALL;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getCallName().equalsIgnoreCase("concat") &&//
                   domain3.getArgs().get(0).equals("'1'") &&//
                   domain3.getArgs().get(1).contains("cast(age AS SIGNED)");
        }
        {
            RdbCallDomain domain4 = (RdbCallDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.CALL && domain4.getAuditKind() == SecQueryKind.CALL;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getCallName().equalsIgnoreCase("cast") &&//
                   domain4.getArgs().get(0).contains("age");
        }
        {
            RdbCallDomain domain5 = (RdbCallDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.CALL && domain5.getAuditKind() == SecQueryKind.CALL;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getCallName().equalsIgnoreCase("cast") &&//
                   domain5.getArgs().get(0).contains("(select id from table_1 limit 1)");
        }
        {
            MySelectDomain domain6 = (MySelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.SUB_CALL;
            assert domain6.getSelectColumns().size() == 1 && domain6.getSelectColumns().contains("id") &&//
                   domain6.getSelectVariables().isEmpty() &&//
                   domain6.getSelectFunc().isEmpty() &&//
                   domain6.getWhereColumns().isEmpty() && //
                   domain6.getJoinTypes().isEmpty();
            assert !domain6.isHasAs() &&         //
                   !domain6.isHasSelectAll() &&  //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom();
            assert !domain6.isHasWhere() &&      //
                   !domain6.isSelectInWhere() && //
                   !domain6.isHasUnion() &&      //
                   !domain6.isHasWith();
            assert domain6.isHasLimit();
        }
    }

    @Test
    public void call_at_select_3() {
        String sql = "select count(*) as cnt from table_1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/count/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().size() == 1 && domain1.getSelectFunc().contains("count") &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("count") &&//
                   domain2.getArgs().get(0).contains("*");
        }
    }
}
