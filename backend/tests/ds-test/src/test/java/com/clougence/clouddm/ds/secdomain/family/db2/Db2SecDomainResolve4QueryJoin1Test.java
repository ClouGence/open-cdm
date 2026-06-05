package com.clougence.clouddm.ds.secdomain.family.db2;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2ResAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2SelectDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2SecDomainResolve4QueryJoin1Test extends Db2SecDomainTestSupport {

    public Db2SecDomainResolve4QueryJoin1Test(){
        this.analysisSpi = new Db2ResAnalysisSpi();
        this.resolveSpi = new Db2SecDomainResolveSpi();
        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Db2;
    }

    @Test
    public void joinQuery_1_1() {
        String sql = "select * from table_1 a join table_2 b on a.id = b.id";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_1") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_2") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&   //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   !domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
        }
    }

    @Test
    public void joinQuery_1_3() {
        String sql = "select * from table_1 a join table_2 b on (a.id)";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_1") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_2") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&   //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   !domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
        }
    }

    @Test
    public void joinQuery_2_1() {
        String sql = "select * from table_1, table_2";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.CROSS_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void joinQuery_2_2() {
        String sql = "select * from table_1 a, table_2 b where a.id1 = b.id2";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 2 && domain1_1.getWhereColumns().equals(Arrays.asList("id1", "id2"));
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert domain1_1.isHasWhere() &&       //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.CROSS_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 1 && domain1_2.getWhereColumns().equals(Arrays.asList("id1"));
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&   //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert domain1_2.isHasWhere() &&       //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 1 && domain1_3.getWhereColumns().equals(Arrays.asList("id2"));
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&   //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert domain1_3.isHasWhere() &&       //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void multiJoinQuery_1() {
        String sql = "select * from table_1 a left join table_2 b on a.id = b.id right join table_3 c on a.id = c.id";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_3/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&   //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
        {
            Db2SelectDomain domain1_4 = (Db2SelectDomain) domainList.get(3);
            assert domain1_4.getSqlType() == SecQueryType.SELECT && domain1_4.getAuditKind() == SecQueryKind.QUERY && domain1_4.getMode() == RdbQueryMode.JOIN;
            assert domain1_4.getCatalog() == null && domain1_4.getSchema() == null && domain1_4.getTable().equals("table_3") &&//
                   domain1_4.getSelectColumns().size() == 0 &&//
                   domain1_4.getSelectVariables().size() == 0 &&//
                   domain1_4.getSelectFunc().size() == 0 &&//
                   domain1_4.getWhereColumns().size() == 0;
            assert !domain1_4.isHasAs() &&         //
                   !domain1_4.isHasSelectAll() &&   //
                   !domain1_4.isSelectInSelect() &&//
                   !domain1_4.isFuncInSelect() &&  //
                   !domain1_4.isSelectInFrom() &&  //
                   !domain1_4.isEmptyFrom() &&     //
                   !domain1_4.isSimpleSelect();
            assert !domain1_4.isHasWhere() &&      //
                   !domain1_4.isSelectInWhere() && //
                   domain1_4.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_4.isHasUnion() &&      //
                   !domain1_4.isHasWith();
        }
    }

    @Test
    public void innerJoinQuery_1_1() {
        String sql = "select * from table_1 a inner join table_2 b on a.id = b.id";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void innerJoinQuery_1_3() {
        String sql = "select * from table_1 a inner join table_2 b on (a.id)";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void crossJoinQuery_1_1() {
        String sql = "select * from table_1 a cross join table_2 b where a.id = b.id";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }
        //
        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 1 && domain1_1.getWhereColumns().contains("id");
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert domain1_1.isHasWhere() &&       //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.CROSS_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 1 && domain1_2.getWhereColumns().contains("id");
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert domain1_2.isHasWhere() &&       //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 1 && domain1_3.getWhereColumns().contains("id");
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert domain1_3.isHasWhere() &&       //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void crossJoinQuery_1_2() {
        String sql = "select * from table_1 a cross join table_2 b";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.CROSS_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void leftJoinQuery_1_1() {
        String sql = "select * from table_1 a left join table_2 b on a.id = b.id";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void leftJoinQuery_1_3() {
        String sql = "select * from table_1 a left join table_2 b on (a.id)";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void leftJoinQuery_2_1() {
        String sql = "select * from table_1 a left outer join table_2 b on a.id = b.id";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void leftJoinQuery_2_3() {
        String sql = "select * from table_1 a left outer join table_2 b on (a.id)";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void rightJoinQuery_1_1() {
        String sql = "select * from table_1 a right join table_2 b on a.id = b.id";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.RIGHT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void rightJoinQuery_1_3() {
        String sql = "select * from table_1 a right join table_2 b on (a.id)";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.RIGHT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void rightJoinQuery_2_1() {
        String sql = "select * from table_1 a right outer join table_2 b on a.id = b.id";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.RIGHT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void rightJoinQuery_2_3() {
        String sql = "select * from table_1 a right outer join table_2 b on (a.id)";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable() == null &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.RIGHT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.JOIN;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   !domain1_2.isHasSelectAll() &&  //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   !domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   !domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }
}
