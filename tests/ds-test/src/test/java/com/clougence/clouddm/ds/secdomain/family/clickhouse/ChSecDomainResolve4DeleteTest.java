package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChDeleteDomain;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4DeleteTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4DeleteTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void delete_from_1() {
        String sql = "alter table test delete where 1=1";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
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
            ChDeleteDomain domain1 = (ChDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test") &&//
                   domain1.getWhereColumns().isEmpty();
            assert !domain1.isMultiDelete();
            assert !domain1.isHasWhere() &&     //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void delete_from_2_1() {
        String sql = "alter table test delete where id = 1";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
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
            ChDeleteDomain domain1 = (ChDeleteDomain) domainList.get(0);
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
    public void delete_from_2_2() {
        String sql = "alter table test delete where id = 1";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
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
            ChDeleteDomain domain2 = (ChDeleteDomain) domainList.get(0);
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

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test2/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/max/");
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
            ChDeleteDomain domain1 = (ChDeleteDomain) domainList.get(0);
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
            ChSelectDomain domain2 = (ChSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 &&//
                   domain2.getSelectFunc().size() == 1 && domain2.getSelectFunc().contains("max") &&//
                   domain2.getWhereColumns().isEmpty();
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
    public void delete_hasSubQuery_hasLimit_1() {
        String sql = "delete from test1 where id = (select max(id) from test2 limit 2)";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test2/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/max/");
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
            ChDeleteDomain domain1 = (ChDeleteDomain) domainList.get(0);
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
            ChSelectDomain domain2 = (ChSelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getWhereColumns().isEmpty();
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
            assert domain2.isHasLimit();
        }
        {
            RdbCallDomain domain3 = (RdbCallDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.CALL && domain3.getAuditKind() == SecQueryKind.CALL;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getCallName().equalsIgnoreCase("max") &&//
                   domain3.getArgs().get(0).contains("id");
        }
    }

}
