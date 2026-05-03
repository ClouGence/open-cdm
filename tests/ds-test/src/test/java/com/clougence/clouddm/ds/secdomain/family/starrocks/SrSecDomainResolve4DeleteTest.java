package com.clougence.clouddm.ds.secdomain.family.starrocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.starrocks.analysis.SrResAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSecDomainResolveSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSplitAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrDeleteDomain;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyDeleteDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class SrSecDomainResolve4DeleteTest extends SrSecDomainTestSupport {

    public SrSecDomainResolve4DeleteTest(){
        this.analysisSpi = new SrResAnalysisSpi(null);
        this.resolveSpi = new SrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.StarRocks;
    }

    @Test
    public void delete_from_1() {
        String sql = "delete from `test`";

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
            SrDeleteDomain domain1 = (SrDeleteDomain) domainList.get(0);
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
            SrDeleteDomain domain1 = (SrDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void delete_from_2_2() {
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
            SrDeleteDomain domain2 = (SrDeleteDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.DELETE && domain2.getAuditKind() == SecQueryKind.DML;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("test") &&//
                   domain2.getWhereColumns().size() == 1 && domain2.getWhereColumns().contains("id");
            assert !domain2.isMultiDelete();
            assert domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() &&//
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
            SrDeleteDomain domain1 = (SrDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test1") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            SrSelectDomain domain2 = (SrSelectDomain) domainList.get(1);
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
            SrDeleteDomain domain1 = (SrDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test1") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   domain1.isSelectInWhere() && //
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            SrSelectDomain domain2 = (SrSelectDomain) domainList.get(1);
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

    @Test(expected = UnsupportedOperationException.class)
    public void delete_using_1() {
        String sql = "delete from table1 using table1,table2 where table1.id1=table2.id2 and table2.age = 2;";

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
            assert splitScripts.get(0).getType() == SecQueryType.DELETE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MyDeleteDomain domain1 = (MyDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table1") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().equals(Collections.singletonList("id1")); // 'table2.id2' belong to table2
            assert !domain1.isHasIgnore() &&    //
                   !domain1.isHasLimit() &&     //
                   !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getWhereColumns().size() == 1 && domain2.getWhereColumns().equals(Collections.singletonList("id1")); // 'table2.id2' belong to table2
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
            assert !domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getWhereColumns().size() == 2 && domain3.getWhereColumns().equals(Arrays.asList("id2", "age"));
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom();
            assert domain3.isHasWhere() &&       //
                   !domain3.isSelectInWhere() && //
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete_using_2() {
        String sql = "delete from t1 using table1 where id =1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DELETE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            MyDeleteDomain domain1 = (MyDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("t1") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id"); // 'table2.id2' belong to table2
            assert !domain1.isHasIgnore() &&    //
                   !domain1.isHasLimit() &&     //
                   !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty();// && domain2.getWhereColumns().contains("id"); // 'table2.id2' belong to table2
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
            assert !domain2.isHasLimit();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete_using_3() {
        String sql = "delete from t1 using table1 t1,table2 t2 where t1.id1=t2.id2 and t2.id2 = 2 and t2.age = 2;";

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
            assert splitScripts.get(0).getType() == SecQueryType.DELETE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            MyDeleteDomain domain1 = (MyDeleteDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DELETE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table1") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().equals(Collections.singletonList("id1")); // 'table2.id2' belong to table2
            assert !domain1.isHasIgnore() &&    //
                   !domain1.isHasLimit() &&     //
                   !domain1.isMultiDelete();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getWhereColumns().size() == 1 && domain2.getWhereColumns().equals(Collections.singletonList("id1")); // 'table2.id2' belong to table2
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
            assert !domain2.isHasLimit();
        }
        {
            MySelectDomain domain3 = (MySelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getSelectColumns().isEmpty() &&//
                   domain3.getWhereColumns().size() == 2 && domain3.getWhereColumns().equals(Arrays.asList("id2", "age"));
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom();
            assert domain3.isHasWhere() &&       //
                   !domain3.isSelectInWhere() && //
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
            assert !domain3.isHasLimit();
        }
    }
}
