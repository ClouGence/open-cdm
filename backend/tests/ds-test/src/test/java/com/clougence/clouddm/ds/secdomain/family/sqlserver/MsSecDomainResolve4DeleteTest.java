package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsDeleteDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4DeleteTest extends MsSecDomainTestSupport {

    public MsSecDomainResolve4DeleteTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void delete_from_2() {
        String sql = "delete from test where id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DELETE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsDeleteDomain domain1 = (MsDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void delete_from_3() {
        String sql = "delete test where id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DELETE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsDeleteDomain domain2 = (MsDeleteDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.DELETE && domain2.getAuditKind() == SecQueryKind.DML;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("test") &&//
                   domain2.getWhereColumns().size() == 1 && domain2.getWhereColumns().contains("id");
            assert !domain2.isMultiDelete();
            assert domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() &&//
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&     //
                   !domain2.isHasWith();
        }
    }

    @Test
    public void delete_hasSubQuery_1() {
        String sql = "delete from test1 where id = (select max(id) from test2)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test2/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/max/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DELETE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MsDeleteDomain domain1 = (MsDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test1") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("test2") &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   domain2.isFuncInSelect() &&   //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            RdbCallDomain domain3 = (RdbCallDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.CALL && domain3.getAuditKind() == SecQueryKind.CALL;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getCallName().equalsIgnoreCase("max") &&//
                   domain3.getArgs().get(0).contains("id");
        }
    }

    @Test
    public void delete_hasFromJoin_1() {
        String sql = "DELETE FROM test_schema.test1   FROM test_schema.test2 AS spqh  " +//
                     "INNER JOIN test_schema.test3 AS sp " +//
                     "ON spqh.BusinessEntityID = sp.BusinessEntityID " +//
                     "WHERE sp.SalesYTD > 2500000.00;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/test3/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DELETE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MsDeleteDomain domain1 = (MsDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("test1") &&//
                   domain1.getWhereColumns().isEmpty();
            assert !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_schema") && domain2.getTable().equals("test2") &&//
                   domain2.getSelectColumns().isEmpty() &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&   //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom();
            assert domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
    }

    // druid   @Test
    //    public void delete_hasWith_1() {
    //        String sql = "WITH Sales_CTE (IFD, NAME)\n" +
    //                "AS\n" +
    //                "(\n" +
    //                "    SELECT ID, NAME\n" +
    //                "    FROM dadwddwa\n" +
    //                "    WHERE ID IS NOT NULL\n" +
    //                ")\n" +
    //                "delete from Sales_CTE where IFD >10 ";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 3;
    //        {
    //            assert resList.get(0).getType() == TargetType.Table &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test1/");
    //            assert resList.get(1).getType() == TargetType.Table &&//
    //                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test2/");
    //            assert resList.get(2).getType() == TargetType.Table &&//
    //                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/test3/");
    //        }
    //
    //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 3;
    //        {
    //            MsDeleteDomain domain1 = (MsDeleteDomain) domainList.get(0);
    //            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getSqlKind() == SqlKind.DML;
    //            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_schema") && domain1.getTable().equals("test1") &&//
    //                   domain1.getWhereColumns().isEmpty();
    //            assert !domain1.isMultiDelete();
    //            assert domain1.isHasWhere() &&      //
    //                   !domain1.isSelectInWhere() && //
    //                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
    //                   !domain1.isHasUnion() &&     //
    //                   !domain1.isHasWith();
    //        }
    //        {
    //            MsSelectDomain domain2 = (MsSelectDomain) domainList.get(1);
    //            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getSqlKind() == SqlKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
    //            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_schema") && domain2.getTable().equals("test2") &&//
    //                   domain2.getSelectColumns().isEmpty() &&//
    //                   domain2.getWhereColumns().size() == 0;
    //            assert !domain2.isHasAs() &&         //
    //                   !domain2.isHasSelectAll() &&  //
    //                   !domain2.isSelectInSelect() &&//
    //                   !domain2.isFuncInSelect() &&   //
    //                   !domain2.isSelectInFrom() &&  //
    //                   !domain2.isEmptyFrom();
    //            assert domain2.isHasWhere() &&      //
    //                   !domain2.isSelectInWhere() && //
    //                   domain2.getJoinType() == RdbJoinType.NONE &&//
    //                   !domain2.isHasUnion() &&      //
    //                   !domain2.isHasWith();
    //            assert !domain2.isHasLimit();
    //        }
    //    }

}
