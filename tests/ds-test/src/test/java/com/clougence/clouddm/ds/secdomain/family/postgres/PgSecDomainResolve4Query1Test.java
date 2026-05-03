package com.clougence.clouddm.ds.secdomain.family.postgres;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgSecDomainResolve4Query1Test extends PgSecDomainTestSupport {

    public PgSecDomainResolve4Query1Test(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PostgreSQL;
    }

    @Test
    public void selectQuery_1() {
        String sql = "select id,name from abc";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
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
    public void selectQuery_2() {
        String sql = "select id,name from test.abc";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
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
    public void selectQuery_3() {
        String sql = "select id,name from test1.test.abc";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test1/test/abc/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
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
    public void selectQuery_9() {
        String sql = "select id + name from test1.test.abc group by id having ss > 3 order by id desc";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test1/test/abc/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
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
    public void selectQuery_2_1() {
        String sql = "select 1";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_2_2() {
        String sql = "select 1 + 1";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.NORMAL;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
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
    public void selectQuery_2_3() {
        String sql = "select (1 + a) as a from users";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/users/");
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
            PgSelectDomain domain3 = (PgSelectDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.NORMAL;
            assert domain3.getSelectColumns().size() == 1 && domain3.getSelectColumns().contains("a") &&//
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

    //@Test
    //    public void selectQuery_3() {
    //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "select @@version");
    //        assert list1.size() == 1;
    //        {
    //            PgQueryDomain domain1 = (PgQueryDomain) list1.get(0);
    //            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getSqlKind() == SqlKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
    //            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
    //                   domain1.getSelectColumns().size() == 0 &&//
    //                   domain1.getSelectVariables().size() == 1 && domain1.getSelectVariables().contains("@@version") &&//
    //                   domain1.getSelectFunc().size() == 0 &&//
    //                   domain1.getWhereColumns().size() == 0;
    //            assert !domain1.isHasAs() &&         //
    //                   !domain1.isHasSelectAll() &&  //
    //                   !domain1.isSelectInSelect() &&//
    //                   !domain1.isFuncInSelect() &&  //
    //                   !domain1.isSelectInFrom() &&  //
    //                   domain1.isEmptyFrom() &&      //
    //                   domain1.isSimpleSelect();
    //            assert !domain1.isHasWhere() &&      //
    //                   !domain1.isSelectInWhere() && //
    //                   domain1.getJoinType() == RdbJoinType.NONE &&//
    //                   !domain1.isHasUnion() &&      //
    //                   !domain1.isHasWith();
    //            assert !domain1.isHasLimit();
    //        }
    //    }

    @Test
    public void selectQuery_4() {
        String sql = "select ads_version()";

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
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/ads_version/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().size() == 1 && domain1.getSelectFunc().contains("ads_version") &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("ads_version") &&//
                   domain2.getArgs().isEmpty();
        }
    }

    @Test
    public void selectQuery_5() {
        String sql = "select * from table_1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
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
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_6() {
        String sql = "select id,* from table_1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
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
    public void selectQuery_7() {
        String sql = "select id,* from (table_1)";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
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
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_10() {
        String sql = "select id,* from (table_1) a";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
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
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_8() {
        String sql = "select * from table_1 limit 10";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
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
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert domain1.isHasLimit();
        }
    }

    @Test
    public void selectLimit_1() {
        String sql = "select * from table_1 limit (select 23 from tb)";

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
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/tb/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
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
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert domain1.isHasLimit();
        }
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&   //
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
    public void emptyWhere_1() {
        String sql = "select * from table_1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void emptyWhere_2() {
        String sql = "select * from table_1 where 1=1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void emptyWhere_3() {
        String sql = "select * from table_1 where id=id";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void emptyWhere_4() {
        String sql = "select * from table_1 where id = (id)";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        // todo
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_1() {
        String sql = "select * from table_1 where id > 1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_2() {
        String sql = "select * from table_1 where id is not null";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_3() {
        String sql = "select * from table_1 where 'abc' = name and id > 3";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id")) &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_4() {
        String sql = "select * from table_1 where length(name) > 2";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/length/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("name") &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain1_2 = (RdbCallDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CALL && domain1_2.getAuditKind() == SecQueryKind.CALL;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getCallName().equalsIgnoreCase("length") &&//
                   domain1_2.getArgs().get(0).contains("name");
        }
    }

    @Test
    public void conditionWhere_5() {
        String sql = "select * from table_1 where (name = 'abc' and id > 3) or id = 1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id")) &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionWhere_6() {
        String sql = "select * from table_1 where 1>1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
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
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionIn_1() {
        String sql = "select * from table_1 where id in (1,2,3)";
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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionIn_2() {
        String sql = "select * from table_1 where id in (1,'2',3)";
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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void conditionIn_3() {
        String sql = "select * from table_1 where id in (select id from table_2)";
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
        assert domainList.size() == 2;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") && //
                   domain1.getJoinTypes().isEmpty();
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
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
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
    public void selectAs_1() {
        String sql = "select id as id1 from table_1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
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
        //        {
        //            RdbTableDomain domain1 = (RdbTableDomain) domainList.get(1);
        //            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getSqlKind() == SqlKind.QUERY;
        //            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1");
        //        }
    }

    @Test
    public void selectAs_2() {
        String sql = "select id as id from table_1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
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
    public void union_1() {
        String sql = "select * from table_1 union select * from table_2";

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
        assert domainList.size() == 2;
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            PgSelectDomain domain3 = (PgSelectDomain) domainList.get(1);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty();
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&   //
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
    }

    @Test
    public void union_2() {
        String sql = "select * from (select * from table_1 union select * from table_2) t";

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
        assert domainList.size() == 3;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(2);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void union_3() {
        String sql = "select * from table_1 a join (select * from table_1 union select * from table_2) b on a.id = b.id";

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
        assert domainList.size() == 3;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().get(0) == RdbJoinType.INNER_JOIN;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain3 = (PgSelectDomain) domainList.get(1);
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
                   domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
        {
            PgSelectDomain domain4 = (PgSelectDomain) domainList.get(2);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getSelectColumns().isEmpty() &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getWhereColumns().isEmpty() &&//
                   domain4.getJoinTypes().isEmpty();
            assert !domain4.isHasAs() &&         //
                   domain4.isHasSelectAll() &&   //
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

    @Test
    public void union_4() {
        String sql = "select id, name from table_1 where id in (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id)";

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
        assert domainList.size() == 4;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
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
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().get(0) == RdbJoinType.LEFT_JOIN;
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
            PgSelectDomain domain5 = (PgSelectDomain) domainList.get(2);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getSelectColumns().isEmpty() &&//
                   domain5.getSelectVariables().isEmpty() &&//
                   domain5.getSelectFunc().isEmpty() &&//
                   domain5.getWhereColumns().isEmpty() &&//
                   domain5.getJoinTypes().isEmpty();
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
            assert !domain5.isHasLimit();
        }

        {
            PgSelectDomain domain6 = (PgSelectDomain) domainList.get(3);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.SUB_FROM;
            assert domain6.getSelectColumns().isEmpty() &&//
                   domain6.getSelectVariables().isEmpty() &&//
                   domain6.getSelectFunc().isEmpty() &&//
                   domain6.getWhereColumns().isEmpty() &&//
                   domain6.getJoinTypes().isEmpty();
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom();
            assert !domain6.isHasWhere() &&      //
                   !domain6.isSelectInWhere() && //
                   domain6.isHasUnion() &&      //
                   !domain6.isHasWith();
            assert !domain6.isHasLimit();
        }

    }

    @Test
    public void union_5_1() {
        String sql = "select id = (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id limit 1) from table_1";

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
        assert domainList.size() == 4;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
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
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
        {
            PgSelectDomain domain4 = (PgSelectDomain) domainList.get(2);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getSelectColumns().isEmpty() &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getWhereColumns().isEmpty() &&//
                   domain4.getJoinTypes().isEmpty();
            assert !domain4.isHasAs() &&         //
                   domain4.isHasSelectAll() &&   //
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
        {
            PgSelectDomain domain5 = (PgSelectDomain) domainList.get(3);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getSelectColumns().isEmpty() &&//
                   domain5.getSelectVariables().isEmpty() &&//
                   domain5.getSelectFunc().isEmpty() &&//
                   domain5.getWhereColumns().isEmpty() &&//
                   domain5.getJoinTypes().isEmpty();
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
            assert !domain5.isHasLimit();
        }
    }

    @Test
    public void union_5_2() {
        String sql = "select (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id limit 1) = id from table_1";

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
        assert domainList.size() == 4;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().size() == 1 && domain2.getJoinTypes().get(0) == RdbJoinType.LEFT_JOIN;
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
            assert domain2.isHasLimit();
        }
        {
            PgSelectDomain domain4 = (PgSelectDomain) domainList.get(2);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getSelectColumns().isEmpty() &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getWhereColumns().isEmpty() &&//
                   domain4.getJoinTypes().isEmpty();
            assert !domain4.isHasAs() &&         //
                   domain4.isHasSelectAll() &&   //
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
        {
            PgSelectDomain domain5 = (PgSelectDomain) domainList.get(3);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getSelectColumns().isEmpty() &&//
                   domain5.getSelectVariables().isEmpty() &&//
                   domain5.getSelectFunc().isEmpty() &&//
                   domain5.getWhereColumns().isEmpty() && //
                   domain5.getJoinTypes().isEmpty();
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
            assert !domain5.isHasLimit();
        }
    }

    @Test
    public void union_6_1() {
        String sql = "select * from table1 union distinct select * from table2;";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.UNION;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain3 = (PgSelectDomain) domainList.get(1);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() && domain3.getJoinTypes().isEmpty();
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&   //
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
    }

    @Test
    public void union_6_2() {
        String sql = "select * from table1 union distinct select * from table2 union all select * from table3 union distinct select * from table4";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/table3/");
            assert resList.get(3).getType() == TargetType.Table &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_db/test_schema/table4/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.UNION;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain4 = (PgSelectDomain) domainList.get(1);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.UNION;
            assert domain4.getSelectColumns().isEmpty() &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getWhereColumns().isEmpty() && //
                   domain4.getJoinTypes().isEmpty();
            assert !domain4.isHasAs() &&         //
                   domain4.isHasSelectAll() &&   //
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
        {
            PgSelectDomain domain5 = (PgSelectDomain) domainList.get(2);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.UNION;
            assert domain5.getSelectColumns().isEmpty() &&//
                   domain5.getSelectVariables().isEmpty() &&//
                   domain5.getSelectFunc().isEmpty() &&//
                   domain5.getWhereColumns().isEmpty() && //
                   domain5.getJoinTypes().isEmpty();
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
            assert !domain5.isHasLimit();
        }
        {
            PgSelectDomain domain6 = (PgSelectDomain) domainList.get(3);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.UNION;
            assert domain6.getSelectColumns().isEmpty() &&//
                   domain6.getSelectVariables().isEmpty() &&//
                   domain6.getSelectFunc().isEmpty() &&//
                   domain6.getWhereColumns().isEmpty() && //
                   domain6.getJoinTypes().isEmpty();
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom();
            assert !domain6.isHasWhere() &&      //
                   !domain6.isSelectInWhere() && //
                   domain6.isHasUnion() &&      //
                   !domain6.isHasWith();
            assert !domain6.isHasLimit();
        }

    }

    @Test
    public void with_query_1() {
        String sql = "with tab1Cnt as (select * from table_1)," + //
                     "     tab2Cnt as (select * from table_2)" +//
                     "select * from tab1Cnt a join tab2Cnt b on a.id = b.id";

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
        assert domainList.size() == 3;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().get(0) == RdbJoinType.INNER_JOIN;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }

        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(2);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void with_query_2() {
        String sql = "with tab1Cnt as (with one as (select 1) select * from table_1)," + //
                     "     tab2Cnt as (select * from table_2)" +//
                     "select * from tab1Cnt a join tab2Cnt b on a.id = b.id";

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
        assert domainList.size() == 3;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().get(0) == RdbJoinType.INNER_JOIN;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   domain2.isHasWith();
            assert !domain2.isHasLimit();
        }

        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(2);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void with_query_3() {
        String sql = "with one as (select 1 as i),\n" +//
                     "     tab1Cnt as (select * from table_1),\n" +//
                     "     tab2Cnt as (select * from table_2 where (select i from one) = 1)\n" +//
                     "select * from tab1Cnt a join tab2Cnt b on a.id = b.id";

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
        assert domainList.size() == 5;
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().get(0) == RdbJoinType.INNER_JOIN;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && //
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }

        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(2);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert domain2.isHasWhere() &&       //
                   domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(3);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().containsAll(Arrays.asList("i")) && //
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(4);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getSelectValue().size() == 1 && domain2.getSelectValue().containsAll(Arrays.asList("1")) &&//
                   domain2.getJoinTypes().isEmpty();
            assert domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void with_query_4() {
        String sql = "select * from table_1 a join (with one as (select 1) select * from one) b on a.id = b.id";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().containsAll(Arrays.asList(RdbJoinType.INNER_JOIN));
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&  //
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
            PgSelectDomain domain3 = (PgSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty();
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&   //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   domain3.isEmptyFrom();
            assert !domain3.isHasWhere() &&       //
                   !domain3.isSelectInWhere() &&  //
                   !domain3.isHasUnion() &&       //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }

    }

    @Test
    public void with_query_5() {
        String sql = "select * from (with one as (select * from table_1) select * from one) t";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain3 = (PgSelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getSelectVariables().isEmpty() &&//
                   domain3.getSelectFunc().isEmpty() &&//
                   domain3.getWhereColumns().isEmpty() && //
                   domain3.getJoinTypes().isEmpty();
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&   //
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
    public void with_query_6_1() {
        String sql = "select id = (with one as (select 1) select a.id from one a limit 1) from table_1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&// 
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }

        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() && // 
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   domain2.isHasWith();
            assert domain2.isHasLimit();
        }

        {
            PgSelectDomain domain4 = (PgSelectDomain) domainList.get(2);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getSelectColumns().isEmpty() &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getSelectValue().size() == 1 && domain4.getSelectValue().contains("1") &&//
                   domain4.getWhereColumns().isEmpty() && //
                   domain4.getJoinTypes().isEmpty();
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   domain4.isEmptyFrom();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
            assert !domain4.isHasLimit();
        }
    }

    @Test
    public void with_query_6_2() {
        String sql = "select (with one as (select 1) select a.id from table_1 a limit 1) = id from table_1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
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
                   !domain2.isHasUnion() &&       //
                   domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void with_query_7() {
        String sql = "select * from table_1 where id in (with one as (select 1)  select id from table_1)";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_1/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") &&//
                   domain1.getJoinTypes().isEmpty();
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
            PgSelectDomain domain2 = (PgSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
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
                   !domain2.isHasUnion() &&       //
                   domain2.isHasWith();
            assert !domain2.isHasLimit();
        }

    }

    @Test
    public void selectAndInQuery_1() {
        String sql = "select id,name from abc where id = 1 and name in('a','b')";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("id", "name")) && //
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectInAndQuery_1() {
        String sql = "select id,name from abc where name in('a','b') and id = 1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id")) &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectOrInQuery_1() {
        String sql = "select id,name from abc where id = 1 or name in('a','b')";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("id", "name")) && // 
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectInOrQuery_1() {
        String sql = "select id,name from abc where name in('a','b') or id = 1";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id")) &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    //@Test
    public void complex_1() {
        //        String sql = "WITH updated_data AS (" + //
        //                     "    UPDATE table1" + //
        //                     "        SET column1 = 1" + //
        //                     "        WHERE condition = 1" + //
        //                     "        RETURNING id" + //
        //                     "), delete_data AS (" + //
        //                     "    delete from table2" + //
        //                     "        WHERE condition = 1" + //
        //                     "        RETURNING id" + //
        //                     ")" + //
        //                     "insert into table3" + //
        //                     "       select id from updated_data" + //
        //                     "       union all" + //
        //                     "       select id from delete_data;";
        //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 4;
    }

    //@Test
    public void complex_2() {
        //        String sql = "WITH updated_data AS (" + //
        //                     "    UPDATE table1" + //
        //                     "        SET column1 = 1" + //
        //                     "        WHERE condition = 1" + //
        //                     "        RETURNING id" + //
        //                     "), delete_data AS (" + //
        //                     "    delete from table1" + //
        //                     "        WHERE condition = 1" + //
        //                     "        RETURNING id" + //
        //                     ")" + //
        //                     "select * from table2 where id in (select id from updated_data)" + //
        //                     "union all" + //
        //                     "select * from table2 where id in (select id from delete_data) limit 10;";
        //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 4;
    }

    @Test
    public void selectArray() {
        String sql = "SELECT ARRAY[1,name::int,(select * from table1),test()] from abc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/table1/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/test/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().equals(Arrays.asList("name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().size() == 1 && domain1.getSelectFunc().contains("test") &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&  //
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
        {
            RdbCallDomain domain1 = (RdbCallDomain) domainList.get(2);
            assert domain1.getSqlType() == SecQueryType.CALL && domain1.getAuditKind() == SecQueryKind.CALL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getCallName().equals("test");
            assert domain1.getArgs().isEmpty();

        }
    }

    @Test
    public void selectJson() {
        String sql = "SELECT \n" + "    c_uuid,\n" + "    c_jsonb->'name' as jsonb_name,\n" + "    c_jsonb->>'name' as jsonb_name_text,\n"
                     + "    c_jsonb#>'{address,street}' as jsonb_nested,\n" + "    c_jsonb#>>'{address,street}' as jsonb_nested_text,\n"
                     + "    jsonb_array_elements(c_jsonb->'tags') as jsonb_tags\n" + "FROM tb_postgre_types;";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/tb_postgre_types/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/jsonb_array_elements/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("c_uuid", "c_jsonb")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().size() == 1 && domain1.getSelectFunc().contains("jsonb_array_elements") &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain1 = (RdbCallDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.CALL && domain1.getAuditKind() == SecQueryKind.CALL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getCallName().equals("jsonb_array_elements");
            assert domain1.getArgs().size() == 1;

        }
    }

    @Test
    public void selectJson1() {
        String sql = "SELECT \n" + "    c_uuid,\n" + "    c_jsonb->(select 'name') as jsonb_name,\n" + "    c_jsonb->>'name' as jsonb_name_text,\n"
                     + "    c_jsonb#>'{address,street}' as jsonb_nested,\n" + "    c_jsonb#>>'{address,street}' as jsonb_nested_text,\n"
                     + "    jsonb_array_elements(c_jsonb->'tags') as jsonb_tags\n" + "FROM tb_postgre_types;";

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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/tb_postgre_types/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/jsonb_array_elements/");
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
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            PgSelectDomain domain1 = (PgSelectDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("c_uuid", "c_jsonb")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().size() == 1 && domain1.getSelectFunc().contains("jsonb_array_elements") &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain1 = (RdbCallDomain) domainList.get(2);
            assert domain1.getSqlType() == SecQueryType.CALL && domain1.getAuditKind() == SecQueryKind.CALL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getCallName().equals("jsonb_array_elements");
            assert domain1.getArgs().size() == 1;
        }
    }
}
