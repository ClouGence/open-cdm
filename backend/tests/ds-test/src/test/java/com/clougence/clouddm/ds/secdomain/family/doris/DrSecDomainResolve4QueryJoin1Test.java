package com.clougence.clouddm.ds.secdomain.family.doris;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.doris.analysis.DrResAnalysisSpi;
import com.clougence.clouddm.ds.doris.analysis.DrSecDomainResolveSpi;
import com.clougence.clouddm.ds.doris.analysis.DrSplitAnalysisSpi;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrSecDomainResolve4QueryJoin1Test extends DrSecDomainTestSupport {

    public DrSecDomainResolve4QueryJoin1Test(){
        this.analysisSpi = new DrResAnalysisSpi(null);
        this.resolveSpi = new DrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new DrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Doris;
    }

    @Test
    public void joinQuery_1_1() {
        String sql = "select * from `table_1` a join `table_2` b on a.id = b.id";

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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1 = (DrSelectDomain) domainList.get(0);
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
        String sql = "select * from table_1 a join table_2 b";

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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
    public void multiJoinQuery_1() {
        String sql = "select * from table_1 a left join table_2 b on a.id = b.id right join table_3 c on a.id = c.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/table_3/");
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
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        String sql = "select * from table_1 a inner join table_2 b";

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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        String sql = "select * from table_1 a cross join table_2 b on a.id = b.id";

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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        String sql = "select * from table_1 a cross join table_2 b on (a.id)";

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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
        assert domainList.size() == 1;
        {
            DrSelectDomain domain1_1 = (DrSelectDomain) domainList.get(0);
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
