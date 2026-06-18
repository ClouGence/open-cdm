package com.clougence.clouddm.ds.secdomain.family.oracle;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraSecDomainResolve4Query1Test extends OraSecDomainTestSupport {

    public OraSecDomainResolve4Query1Test(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Oracle;
    }

    @Test
    public void selectQuery_1() {
        String sql = "select id,\"name\" from abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
    public void selectQueryLimit_1() {
        String sql = "select id,\"name\" from abc fetch first 10 row only";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
            assert domain1.isHasLimit();
        }
    }

    @Test
    public void selectAllQuery_1() {
        String sql = "select all id,name from \"tt\".abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/tt/abc/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
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
        }
    }

    @Test
    public void selectUniqueQuery_1() {
        String sql = "select unique id,name from \"abc\"";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
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
        }
    }

    //    @Test
    //    public void selectQuery_2_1() {
    //        String sql = "select 1 from dual";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.isEmpty();
    //
    //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
    //            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getSqlKind() == SqlKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
    //            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
    //                   domain1.getSelectColumns().size() == 0 &&//
    //                   domain1.getSelectVariables().size() == 0 &&//
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

    //    @Test
    //    public void selectQuery_2_2() {
    //        String sql = "select 1 + 1";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.isEmpty();
    //
    //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 1;
    //        {
    //            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(0);
    //            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getSqlKind() == SqlKind.QUERY && domain2.getMode() == RdbQueryMode.NORMAL;
    //            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
    //                   domain2.getSelectColumns().size() == 0 &&//
    //                   domain2.getSelectVariables().size() == 0 &&//
    //                   domain2.getSelectFunc().size() == 0 &&//
    //                   domain2.getWhereColumns().size() == 0;
    //            assert !domain2.isHasAs() &&         //
    //                   !domain2.isHasSelectAll() &&  //
    //                   !domain2.isSelectInSelect() &&//
    //                   !domain2.isFuncInSelect() &&  //
    //                   !domain2.isSelectInFrom() &&  //
    //                   domain2.isEmptyFrom();
    //            assert !domain2.isHasWhere() &&      //
    //                   !domain2.isSelectInWhere() && //
    //                   domain2.getJoinType() == RdbJoinType.NONE &&//
    //                   !domain2.isHasUnion() &&      //
    //                   !domain2.isHasWith();
    //            assert !domain2.isHasLimit();
    //        }
    //    }

    @Test
    public void selectQuery_2_3() {
        String sql = "select (1 + a) as aa from users";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/users/");
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
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void selectQuery_4() {
        String sql = "select \"tt\".\"ads_version\"() from dual";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/dual/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/tt/ads_version/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
                   !domain1.isEmptyFrom();    //
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("tt") && domain2.getCallName().equalsIgnoreCase("ads_version") &&//
                   domain2.getArgs().isEmpty();
        }
    }

    @Test
    public void selectQuery_6() {
        String sql = "select tt.ads_version() from dual";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/dual/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/tt/ads_version/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
                   !domain1.isEmptyFrom();    //
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("tt") && domain2.getCallName().equalsIgnoreCase("ads_version") &&//
                   domain2.getArgs().isEmpty();
        }
    }

    @Test
    public void selectQuery_5() {
        String sql = "select * from table_1";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void emptyWhere_1() {
        String sql = "select * from table_1";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void emptyWhere_2() {
        String sql = "select * from table_1 where 1=1";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void emptyWhere_3() {
        String sql = "select * from table_1 where id=id";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void emptyWhere_4() {
        String sql = "select * from table_1 where id = (id)";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() &&//
                   domain1.getJoinTypes().isEmpty();;
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
        }
    }

    @Test
    public void betweenWhere_1() {
        String sql = "select * from table_1 where id between 1 and 34";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") &&//
                   domain1.getJoinTypes().isEmpty();;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void conditionWhere_1() {
        String sql = "select * from table_1 where id > 1";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id") && //
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
        }
    }

    @Test
    public void conditionWhere_2() {
        String sql = "select * from table_1 where id is not null";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void conditionWhere_3() {
        String sql = "select * from table_1 where 'abc' = name and id > 3";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void conditionWhere_4() {
        String sql = "select * from table_1 where length(name) > 2";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/length/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("name") && //
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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id")) && //
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
        }
    }

    @Test
    public void conditionWhere_6() {
        String sql = "select * from table_1 where 1>1";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void conditionIn_1() {
        String sql = "select * from table_1 where id in (1,2,3)";
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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void conditionIn_2() {
        String sql = "select * from table_1 where id in (1,'2',3)";
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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void conditionIn_3() {
        String sql = "select * from table_1 where id in (select id from table_2)";
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
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
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
    }

    @Test
    public void conditionInLimit_3() {
        String sql = "select * from table_1 where id in (select id from table_2  fetch first 10 row only)";
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
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
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert domain2.isHasLimit();
        }
    }

    @Test
    public void selectAs_1() {
        String sql = "select id as \"id1\" from table_1";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void selectAs_2() {
        String sql = "select id as id from table_1";

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
        assert domainList.size() == 1;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
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
        }
    }

    @Test
    public void union_1() {
        String sql = "select * from table_1 union select * from table_2";

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
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(1);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
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
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
        }
    }

    @Test
    public void union_2() {
        String sql = "select * from (select * from table_1 union select * from table_2) t";

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

        assert domainList.size() == 3;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
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
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(2);
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
    }

    @Test
    public void unionLimit_2() {
        String sql = "select * from (select * from table_1  fetch first 10 row only union select * from table_2) t";

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

        assert domainList.size() == 3;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
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
            assert domain2.isHasLimit();
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(2);
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
    }

    @Test
    public void union_3() {
        String sql = "select * from table_0 a join (select * from table_1 union select * from table_2) b";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_0/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&  //
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
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
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
                   domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
        }
    }

    @Test
    public void union_4() {
        String sql = "select id, name from table_1 where id in (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id)";

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
        assert domainList.size() == 4;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
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
                   !domain2.isSelectInWhere() && //
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
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
        }
        {
            OraSelectDomain domain4 = (OraSelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable() == null &&//
                   domain4.getSelectColumns().isEmpty() &&//
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
        }
    }

    @Test
    public void union_5_1() {
        String sql = "select id = (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id) from table_1";

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
        assert domainList.size() == 4;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
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
        }
        {
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
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
        }
        {
            OraSelectDomain domain4 = (OraSelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable() == null &&//
                   domain4.getSelectColumns().isEmpty() &&//
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
        }

    }

    @Test
    public void unionLimit_5_1() {
        String sql = "select id = (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id  fetch first 10 row only) from table_1";

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
        assert domainList.size() == 4;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
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
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
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
        }
        {
            OraSelectDomain domain4 = (OraSelectDomain) domainList.get(3);
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
        }

    }

    @Test
    public void union_5_2() {
        String sql = "select (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id) = id from table_1";

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
        assert domainList.size() == 4;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
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
        }
        {
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
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
        }
        {
            OraSelectDomain domain4 = (OraSelectDomain) domainList.get(3);
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
        }
    }

    @Test
    public void union_6_1() {
        String sql = "select * from table1 union  select * from table2;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table2/");
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
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    @Test
    public void union_6_2() {
        String sql = "select * from table1 union  select * from table2 union all select * from table3 union  select * from table4";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table3/");
            assert resList.get(3).getType() == TargetType.Table &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/table4/");
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
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(0);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().isEmpty() &&//
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(2);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(3);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().isEmpty() &&//
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
        }
    }

    @Test
    public void with_query_1() {
        String sql = "with tab1Cnt as (select * from table_1)," + //
                     "     tab2Cnt as (select * from table_2)" +//
                     "select * from tab1Cnt a join tab2Cnt b on a.id = b.id";

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
        assert domainList.size() == 3;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   !domain1.isHasUnion() &&       //
                   domain1.isHasWith();
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().isEmpty() &&//
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
        }
        {
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
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
        }
    }

    @Test
    public void with_query_3() {
        String sql = "with one as (select 1 as i),\n" +//
                     "     tab1Cnt as (select * from table_1),\n" +//
                     "     tab2Cnt as (select * from table_2 where (select i from one) = 1)\n" +//
                     "select * from tab1Cnt a join tab2Cnt b on a.id = b.id";

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
        assert domainList.size() == 5;
        {
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && // 
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().isEmpty() &&//
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
        }
        {
            OraSelectDomain domain3 = (OraSelectDomain) domainList.get(2);
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
            assert domain3.isHasWhere() &&       //
                   domain3.isSelectInWhere() &&  //
                   !domain3.isHasUnion() &&       //
                   !domain3.isHasWith();
        }
        {
            OraSelectDomain domain4 = (OraSelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain4.getSelectColumns().size() == 1 && domain4.getSelectColumns().contains("i") && //
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getWhereColumns().isEmpty() && //
                   domain4.getJoinTypes().isEmpty();
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&   //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
        }
        {
            OraSelectDomain domain4 = (OraSelectDomain) domainList.get(4);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable() == null &&//
                   domain4.getSelectColumns().isEmpty() &&//
                   domain4.getSelectVariables().isEmpty() &&//
                   domain4.getSelectFunc().isEmpty() &&//
                   domain4.getSelectValue().size() == 1 && domain4.getSelectValue().contains("1") &&//
                   domain4.getWhereColumns().isEmpty() && //
                   domain4.getJoinTypes().isEmpty();
            assert domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   domain4.isEmptyFrom();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
        }
    }

    @Test
    public void with_query_4() {
        String sql = "select * from table_1 a join (with one as (select 1) select * from table_1) b";

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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().size() == 1 && domain1.getJoinTypes().contains(RdbJoinType.INNER_JOIN);
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   domain2.isHasWith();
        }

    }

    @Test
    public void with_query_5() {
        String sql = "select * from (with one as (select 1) select * from table_1) t";

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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty() && //
                   domain1.getJoinTypes().isEmpty();
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
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty() &&//
                   domain2.getJoinTypes().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   !domain2.isHasUnion() &&       //
                   domain2.isHasWith();
        }
    }

    @Test
    public void with_query_7() {
        String sql = "select * from table_1 where id in (with one as (select 1)  select id from table_1)";

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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
        }
        {
            OraSelectDomain domain2 = (OraSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
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
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
        }
    }

    @Test
    public void selectAndInQuery_1() {
        String sql = "select id,name from abc where id = 1 and name in('a','b')";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("id", "name")) && domain1.getJoinTypes().isEmpty();
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
        }
    }

    @Test
    public void selectInAndQuery_1() {
        String sql = "select id,name from abc where name in('a','b') and id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id")) && //
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
        }
    }

    @Test
    public void selectOrInQuery_1() {
        String sql = "select id,name from abc where id = 1 or name in('a','b')";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("id", "name")) &&//
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
        }
    }

    @Test
    public void selectInOrQuery_1() {
        String sql = "select id,name from abc where name in('a','b') or id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
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
            OraSelectDomain domain1 = (OraSelectDomain) domainList.get(0);
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
        }
    }

    /**
     * SELECT COUNT(*) * 10 FROM orders SAMPLE(10) SEED (1);
     */
}
