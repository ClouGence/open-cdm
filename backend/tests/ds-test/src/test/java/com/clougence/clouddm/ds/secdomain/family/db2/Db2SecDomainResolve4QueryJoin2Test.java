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
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2SecDomainResolve4QueryJoin2Test extends Db2SecDomainTestSupport {

    public Db2SecDomainResolve4QueryJoin2Test(){
        this.analysisSpi = new Db2ResAnalysisSpi();
        this.resolveSpi = new Db2SecDomainResolveSpi();
        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Db2;
    }

    @Test
    public void from_join_1() {
        String sql = "select * from (select 1 id,'name' name from SYSIBM.SYSDUMMY1) a join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
        assert domainList.size() == 4;
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
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("a") &&//
                   domain2.getSelectColumns().size() == 0 &&    //
                   domain2.getSelectVariables().size() == 0 &&  //
                   domain2.getSelectFunc().size() == 0 &&       //
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
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
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("SYSIBM") && domain3.getTable().equals("SYSDUMMY1") &&//
                   domain3.getSelectColumns().size() == 0 &&    //
                   domain3.getSelectVariables().size() == 0 &&  //
                   domain3.getSelectFunc().size() == 0 &&       //
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   domain3.isSimpleSelect();
            assert !domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
        }
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.JOIN;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("table_2") &&//
                   domain4.getSelectColumns().size() == 0 &&    //
                   domain4.getSelectVariables().size() == 0 &&  //
                   domain4.getSelectFunc().size() == 0 &&       //
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
    }

    @Test
    public void from_join_2() {
        String sql = "select * from table_1 a join (select 1 id,'name' name from SYSIBM.SYSDUMMY1) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
                   domain2.getSelectColumns().size() == 0 &&    //
                   domain2.getSelectVariables().size() == 0 &&  //
                   domain2.getSelectFunc().size() == 0 &&       //
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
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
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("b") &&//
                   domain3.getSelectColumns().size() == 0 &&    //
                   domain3.getSelectVariables().size() == 0 &&  //
                   domain3.getSelectFunc().size() == 0 &&       //
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("SYSIBM") && domain4.getTable().equals("SYSDUMMY1") &&//
                   domain4.getSelectColumns().size() == 0 &&    //
                   domain4.getSelectVariables().size() == 0 &&  //
                   domain4.getSelectFunc().size() == 0 &&       //
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable() == null &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
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
                   domain2.getSelectColumns().size() == 0 &&    //
                   domain2.getSelectVariables().size() == 0 &&  //
                   domain2.getSelectFunc().size() == 0 &&       //
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
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
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("b") &&//
                   domain3.getSelectColumns().size() == 0 &&    //
                   domain3.getSelectVariables().size() == 0 &&  //
                   domain3.getSelectFunc().size() == 0 &&       //
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("table_2") &&//
                   domain4.getSelectColumns().size() == 2 && domain4.getSelectColumns().equals(Arrays.asList("id", "name")) &&    //
                   domain4.getSelectVariables().size() == 0 &&  //
                   domain4.getSelectFunc().size() == 0 &&       //
                   domain4.getWhereColumns().size() == 0;
            assert domain4.isHasAs() &&          //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&      //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
    }

    @Test
    public void from_innerJoin_1() {
        String sql = "select * from (select 1 id,'name' name from SYSIBM.SYSDUMMY1) a inner join table_2 b on a.id = b.id;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }
        //
        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
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
                   domain1_1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("a") &&//
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
    public void from_innerJoin_2() {
        String sql = "select * from table_1 a inner join (select 1 id,'name' name from SYSIBM.SYSDUMMY1) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
                   domain1_2.getSelectValue().size() == 0 &&//
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("b") &&//
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
        {
            Db2SelectDomain domain1_4 = (Db2SelectDomain) domainList.get(3);
            assert domain1_4.getSqlType() == SecQueryType.SELECT && domain1_4.getAuditKind() == SecQueryKind.QUERY && domain1_4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_4.getCatalog() == null && domain1_4.getSchema().equals("SYSIBM") && domain1_4.getTable().equals("SYSDUMMY1") &&//
                   domain1_4.getSelectColumns().size() == 0 &&//
                   domain1_4.getSelectVariables().size() == 0 &&//
                   domain1_4.getSelectFunc().size() == 0 &&//
                   domain1_4.getSelectValue().size() == 2 && domain1_4.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_4.getWhereColumns().size() == 0;
            assert !domain1_4.isHasAs() &&         //
                   !domain1_4.isHasSelectAll() &&  //
                   !domain1_4.isSelectInSelect() &&//
                   !domain1_4.isFuncInSelect() &&  //
                   !domain1_4.isSelectInFrom() &&  //
                   !domain1_4.isEmptyFrom() &&     //
                   domain1_4.isSimpleSelect();
            assert !domain1_4.isHasWhere() &&      //
                   !domain1_4.isSelectInWhere() && //
                   domain1_4.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_4.isHasUnion() &&      //
                   !domain1_4.isHasWith();
        }
    }

    @Test
    public void from_crossJoin_1() {
        String sql = "select * from (select 1 id,'name' name from SYSIBM.SYSDUMMY1) a cross join table_2 b";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
                   domain1_1.getJoinType() == RdbJoinType.CROSS_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("a") &&//
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
    public void from_crossJoin_2() {
        String sql = "select * from table_1 a cross join (select 1 id,'name' name from SYSIBM.SYSDUMMY1) b where a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
                   domain1_2.getSelectFunc().size() == 0 && domain1_2.getWhereColumns().contains("id") &&//
                   domain1_2.getWhereColumns().size() == 1;
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("b") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void from_leftJoin_1_1() {
        String sql = "select * from (select 1 id,'name' name from SYSIBM.SYSDUMMY1) a left join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("a") &&//
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
    public void from_leftJoin_1_2() {
        String sql = "select * from table_1 a left join (select 1 id,'name' name from SYSIBM.SYSDUMMY1) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("b") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void from_leftJoin_2_1() {
        String sql = "select * from (select 1 id,'name' name from SYSIBM.SYSDUMMY1) a left outer join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("a") &&//
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
    public void from_leftJoin_2_2() {
        String sql = "select * from table_1 a left outer join (select 1 id,'name' name from SYSIBM.SYSDUMMY1) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("b") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void from_rightJoin_1_1() {
        String sql = "select * from (select 1 id,'name' name from SYSIBM.SYSDUMMY1) a right join table_2 b on a.id = b.id;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
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
                   domain1_1.getJoinType() == RdbJoinType.RIGHT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("a") &&//
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
    public void from_rightJoin_1_2() {
        String sql = "select * from table_1 a right join (select 1 id,'name' name from SYSIBM.SYSDUMMY1) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("b") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
    }

    @Test
    public void from_rightJoin_2_1() {
        String sql = "select * from (select 1 id,'name' name from SYSIBM.SYSDUMMY1) a right outer join table_2 b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
                   domain1_1.getJoinType() == RdbJoinType.RIGHT_JOIN &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("a") &&//
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_2") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
    public void from_rightJoin_2_2() {
        String sql = "select * from table_1 a right outer join (select 1 id,'name' name from SYSIBM.SYSDUMMY1) b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
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
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("b") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 0 &&//
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
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(3);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema().equals("SYSIBM") && domain1_3.getTable().equals("SYSDUMMY1") &&//
                   domain1_3.getSelectColumns().size() == 0 &&//
                   domain1_3.getSelectVariables().size() == 0 &&//
                   domain1_3.getSelectFunc().size() == 0 &&//
                   domain1_3.getSelectValue().size() == 2 && domain1_3.getSelectValue().equals(Arrays.asList("id", "name")) &&//
                   domain1_3.getWhereColumns().size() == 0;
            assert !domain1_3.isHasAs() &&         //
                   !domain1_3.isHasSelectAll() &&  //
                   !domain1_3.isSelectInSelect() &&//
                   !domain1_3.isFuncInSelect() &&  //
                   !domain1_3.isSelectInFrom() &&  //
                   !domain1_3.isEmptyFrom() &&     //
                   domain1_3.isSimpleSelect();
            assert !domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
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
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("t") &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable() == null &&//
                   domain1_2.getSelectColumns().size() == 2 && domain1_2.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
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
                   domain1_2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_1") &&//
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
            assert domain1_4.getCatalog() == null && domain1_4.getSchema() == null && domain1_4.getTable().equals("table_2") &&//
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
    public void join_at_where_1() {
        String sql = "select * from table_1 where id in (select a.id from table_1 a left join table_2 b on a.id = b.id)";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 4;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("table_1") &&//
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
                   domain1_1.isSimpleSelect();
            assert domain1_1.isHasWhere() &&       //
                   domain1_1.isSelectInWhere() &&  //
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable() == null &&//
                   domain1_2.getSelectColumns().size() == 1 && domain1_2.getSelectColumns().equals(Arrays.asList("id")) &&//
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
                   domain1_2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_1") &&//
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
            assert domain1_4.getCatalog() == null && domain1_4.getSchema() == null && domain1_4.getTable().equals("table_2") &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 5;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("table_1") &&//
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
                   domain1_1.isSimpleSelect();
            assert domain1_1.isHasWhere() &&      //
                   domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            RdbCallDomain domain1_2 = (RdbCallDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CALL && domain1_2.getAuditKind() == SecQueryKind.CALL;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getCallName().equalsIgnoreCase("cast") &&//
                   domain1_2.getArgs().get(0).contains("SELECT a.id");
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable() == null &&//
                   domain1_3.getSelectColumns().size() == 1 && domain1_3.getSelectColumns().contains("id") &&//
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
            assert domain1_3.isHasWhere() &&      //
                   !domain1_3.isSelectInWhere() && //
                   domain1_3.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_3.isHasUnion() &&      //
                   !domain1_3.isHasWith();
        }
        {
            Db2SelectDomain domain1_4 = (Db2SelectDomain) domainList.get(3);
            assert domain1_4.getSqlType() == SecQueryType.SELECT && domain1_4.getAuditKind() == SecQueryKind.QUERY && domain1_4.getMode() == RdbQueryMode.JOIN;
            assert domain1_4.getCatalog() == null && domain1_4.getSchema() == null && domain1_4.getTable().equals("table_2") &&//
                   domain1_4.getSelectColumns().size() == 0 &&//
                   domain1_4.getSelectVariables().size() == 0 &&//
                   domain1_4.getSelectFunc().size() == 0 &&//
                   domain1_4.getWhereColumns().size() == 1 && domain1_4.getWhereColumns().contains("id");
            assert !domain1_4.isHasAs() &&         //
                   !domain1_4.isHasSelectAll() &&  //
                   !domain1_4.isSelectInSelect() &&//
                   !domain1_4.isFuncInSelect() &&  //
                   !domain1_4.isSelectInFrom() &&  //
                   !domain1_4.isEmptyFrom() &&     //
                   !domain1_4.isSimpleSelect();
            assert domain1_4.isHasWhere() &&       //
                   !domain1_4.isSelectInWhere() && //
                   domain1_4.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_4.isHasUnion() &&      //
                   !domain1_4.isHasWith();
        }
        {
            Db2SelectDomain domain1_5 = (Db2SelectDomain) domainList.get(4);
            assert domain1_5.getSqlType() == SecQueryType.SELECT && domain1_5.getAuditKind() == SecQueryKind.QUERY && domain1_5.getMode() == RdbQueryMode.JOIN;
            assert domain1_5.getCatalog() == null && domain1_5.getSchema() == null && domain1_5.getTable().equals("table_3") &&//
                   domain1_5.getSelectColumns().size() == 0 &&//
                   domain1_5.getSelectVariables().size() == 0 &&//
                   domain1_5.getSelectFunc().size() == 0 &&//
                   domain1_5.getWhereColumns().size() == 0;
            assert !domain1_5.isHasAs() &&         //
                   !domain1_5.isHasSelectAll() &&  //
                   !domain1_5.isSelectInSelect() &&//
                   !domain1_5.isFuncInSelect() &&  //
                   !domain1_5.isSelectInFrom() &&  //
                   !domain1_5.isEmptyFrom() &&     //
                   !domain1_5.isSimpleSelect();
            assert domain1_5.isHasWhere() &&       //
                   !domain1_5.isSelectInWhere() && //
                   domain1_5.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_5.isHasUnion() &&      //
                   !domain1_5.isHasWith();
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
        //
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
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("table_3") &&//
                   domain1_1.getSelectColumns().size() == 1 && domain1_1.getSelectColumns().contains("id") &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   !domain1_1.isHasSelectAll() &&  //
                   domain1_1.isSelectInSelect() && //
                   !domain1_1.isFuncInSelect() &&  //
                   !domain1_1.isSelectInFrom() &&  //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable() == null &&//
                   domain1_2.getSelectColumns().size() == 1 && domain1_2.getSelectColumns().equals(Arrays.asList("id")) &&//
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
                   domain1_2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
        {
            Db2SelectDomain domain1_3 = (Db2SelectDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.SELECT && domain1_3.getAuditKind() == SecQueryKind.QUERY && domain1_3.getMode() == RdbQueryMode.JOIN;
            assert domain1_3.getCatalog() == null && domain1_3.getSchema() == null && domain1_3.getTable().equals("table_1") &&//
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
            assert domain1_4.getCatalog() == null && domain1_4.getSchema() == null && domain1_4.getTable().equals("table_2") &&//
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
    public void subquery_at_from_1() {
        String sql = "select * from (select * from table_1) t";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("t") &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   domain1_2.isHasSelectAll() &&   //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("t") &&//
                   domain1_1.getSelectColumns().size() == 0 &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   domain1_1.isHasSelectAll() &&   //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   domain1_2.isHasSelectAll() &&   //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("t") &&//
                   domain1_1.getSelectColumns().size() == 2 && domain1_1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   !domain1_1.isHasSelectAll() &&  //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   domain1_2.isHasSelectAll() &&   //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom() &&     //
                   domain1_2.isSimpleSelect();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   domain1_2.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2SelectDomain domain1_1 = (Db2SelectDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.SELECT && domain1_1.getAuditKind() == SecQueryKind.QUERY && domain1_1.getMode() == RdbQueryMode.NORMAL;
            assert domain1_1.getCatalog() == null && domain1_1.getSchema() == null && domain1_1.getTable().equals("t") &&//
                   domain1_1.getSelectColumns().size() == 2 && domain1_1.getSelectColumns().equals(Arrays.asList("id1", "name")) &&//
                   domain1_1.getSelectVariables().size() == 0 &&//
                   domain1_1.getSelectFunc().size() == 0 &&//
                   domain1_1.getWhereColumns().size() == 0;
            assert !domain1_1.isHasAs() &&         //
                   !domain1_1.isHasSelectAll() &&  //
                   !domain1_1.isSelectInSelect() &&//
                   !domain1_1.isFuncInSelect() &&  //
                   domain1_1.isSelectInFrom() &&   //
                   !domain1_1.isEmptyFrom() &&     //
                   !domain1_1.isSimpleSelect();
            assert !domain1_1.isHasWhere() &&      //
                   !domain1_1.isSelectInWhere() && //
                   domain1_1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1_1.isHasUnion() &&      //
                   !domain1_1.isHasWith();
        }
        {
            Db2SelectDomain domain1_2 = (Db2SelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema() == null && domain1_2.getTable().equals("table_1") &&//
                   domain1_2.getSelectColumns().size() == 2 && domain1_2.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert domain1_2.isHasAs() &&          //
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
    }

    @Test
    public void subquery_at_where_1() {
        String sql = "select * from table_1 where id in (select id from table_2)";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 2;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_2") &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
    }

    @Test
    public void subquery_at_where_2() {
        String sql = "select id, name from table_1 where id in (select a.id from table_1 a left join (select * from table_2) b on a.id = b.id)";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 5;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("b") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_2") &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&  //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
    }

    @Test
    public void subquery_at_where_3() {
        String sql = "select id, name from sch.table_1 where id in (select a.id from scc.table_1 a left join (select id as id1, name from table_2) b on a.id = b.id1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/sch/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/scc/table_1/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 5;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("sch") && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("scc") && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("b") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_2") &&//
                   domain5.getSelectColumns().size() == 2 && domain5.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert domain5.isHasAs() &&          //
                   !domain5.isHasSelectAll() &&  //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   !domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
    }

    @Test
    public void subquery_at_select_1_1() {
        String sql = "select id = (select a.id from table_1 a left join (select * from table_2) b on a.id = b.id limit 1) from table_1";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 5;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("b") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_2") &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
    }

    @Test
    public void subquery_at_select_1_2() {
        String sql = "select (select a.id from table_1 a left join (select * from table_2) b on a.id = b.id limit 1) = id from table_1";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 5;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("b") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_2") &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
    }

    @Test
    public void subquery_at_select_1_3() {
        String sql = "select (select a.id from table_1 a left join (select id as id1, name from table_2) b on a.id = b.id1 limit 1) = id from table_1";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 5;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.SUB_FROM;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("b") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_2") &&//
                   domain5.getSelectColumns().size() == 2 && domain5.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert domain5.isHasAs() &&          //
                   !domain5.isHasSelectAll() &&  //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   !domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
    }

    @Test
    public void subquery_at_select_2_1() {
        String sql = "select id = (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1) from table_1";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 4;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.JOIN;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("table_2") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
    }

    @Test
    public void subquery_at_select_2_2() {
        String sql = "select (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1) = id from table_1";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 4;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.JOIN;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("table_2") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
    }

    @Test
    public void subquery_at_select_3() {
        String sql = "select t1.id = (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1)" +//
                     "from (select * from table_3) t1 where t1.id in (select id from table_4)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
            assert resList.get(2).getType() == TargetType.Table &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/table_3/");
            assert resList.get(3).getType() == TargetType.Table &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/table_4/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 6;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("t1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getSelectColumns().contains("id");
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.LEFT_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_1") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
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
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.JOIN;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("table_2") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.SUB_FROM;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_3") &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
        {
            Db2SelectDomain domain6 = (Db2SelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table_4") &&//
                   domain6.getSelectColumns().size() == 1 && domain6.getSelectColumns().contains("id") &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   !domain6.isHasSelectAll() &&  //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom() &&     //
                   domain6.isSimpleSelect();
            assert !domain6.isHasWhere() &&      //
                   !domain6.isSelectInWhere() && //
                   domain6.getJoinType() == RdbJoinType.NONE &&//
                   !domain6.isHasUnion() &&      //
                   !domain6.isHasWith();
        }
    }

    //@Test parser unsupport
    public void subquery_at_select_4() {
        String sql = "select t1.id = (select a.id from table_1 a left join table_2 b on a.id = b.id limit 1)" +//
                     "from (select * from table_1) t1 join (select * from table_1) t2 on t1.id = t2.id" + //
                     "where t1.id in (select id from table_1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_2/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        // assert domainList.size() == 6;
    }

    @Test
    public void subquery_at_select_5_1() {
        String sql = "select *,(select a.id from \"key_datatime\" b join \"two_pk_table\" a  on a.\"id\" = b.\"a1\" where a.id > 1 limit 1)" +//
                     "from two_pk_table where  1=1 is not null";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/two_pk_table/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/key_datatime/");
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
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("two_pk_table") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 1 && domain2.getSelectColumns().contains("id");
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("key_datatime") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   !domain3.isSimpleSelect();
            assert domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
        }
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.JOIN;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("two_pk_table") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 1 && domain4.getWhereColumns().contains("id");
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
    }

    @Test
    public void subquery_at_select_5_2() {
        String sql = "select *,(select a.id as id1 from \"key_datatime\" b join \"two_pk_table\" a on a.\"id\" = b.\"a1\" where a.id > 1 limit 1)" +//
                     "from two_pk_table where  1=1 is not null";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/two_pk_table/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/key_datatime/");
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
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("two_pk_table") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   domain1.isSelectInSelect() && //
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable() == null &&//
                   domain2.getSelectColumns().size() == 1 && domain2.getSelectColumns().contains("id") &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 1 && domain2.getSelectColumns().contains("id");
            assert !domain2.isHasAs() &&         //
                   !domain2.isHasSelectAll() &&  //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   !domain2.isSimpleSelect();
            assert domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("key_datatime") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   !domain3.isHasSelectAll() &&  //
                   !domain3.isSelectInSelect() &&//
                   !domain3.isFuncInSelect() &&  //
                   !domain3.isSelectInFrom() &&  //
                   !domain3.isEmptyFrom() &&     //
                   !domain3.isSimpleSelect();
            assert domain3.isHasWhere() &&      //
                   !domain3.isSelectInWhere() && //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&      //
                   !domain3.isHasWith();
        }
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.JOIN;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("two_pk_table") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 1 && domain4.getWhereColumns().contains("id");
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
    }

    @Test
    public void call_at_select_1() {
        String sql = "select length(id) as c,cast(id AS varchar(200)) as b from two_pk_table where  1=1 is not null";
        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/two_pk_table/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/length/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/CAST/");
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
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("two_pk_table") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 2 && domain1.getSelectFunc().equals(Arrays.asList("length", "CAST")) &&//
                   domain1.getWhereColumns().size() == 0;
            assert domain1.isHasAs() &&          //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("length") &&//
                   domain2.getArgs().get(0).contains("id");
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
        String sql = "select length(id) as c,CONCAT('1', cast(age AS varchar(200))),cast((select id from table_1 limit 1) AS varchar(200)) from two_pk_table where  1=1 is not null";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 5;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/two_pk_table/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/length/");
            assert resList.get(2).getType() == TargetType.Function &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/CONCAT/");
            assert resList.get(3).getType() == TargetType.Function &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/CAST/");
            assert resList.get(4).getType() == TargetType.Table &&//
                   resList.get(4).toDsResPath().getResPath().equals("/test_schema/table_1/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 6;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("two_pk_table") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "age")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 3 && domain1.getSelectFunc().equals(Arrays.asList("length", "CAST", "CONCAT")) &&//
                   domain1.getWhereColumns().size() == 0;
            assert domain1.isHasAs() &&          //
                   !domain1.isHasSelectAll() &&  //
                   domain1.isSelectInSelect() && //
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("length") &&//
                   domain2.getArgs().get(0).contains("id");
        }
        {
            RdbCallDomain domain3 = (RdbCallDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.CALL && domain3.getAuditKind() == SecQueryKind.CALL;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getCallName().equalsIgnoreCase("concat") &&//
                   domain3.getArgs().get(0).equals("'1'") &&//
                   domain3.getArgs().get(1).contains("CAST(age AS varchar(200))");
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
                   domain5.getArgs().get(0).contains("SELECT id");
        }
        {
            Db2SelectDomain domain6 = (Db2SelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.SUB_SELECT;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table_1") &&//
                   domain6.getSelectColumns().size() == 1 && domain6.getSelectColumns().contains("id") &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   !domain6.isHasSelectAll() &&  //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom() &&     //
                   domain6.isSimpleSelect();
            assert !domain6.isHasWhere() &&      //
                   !domain6.isSelectInWhere() && //
                   domain6.getJoinType() == RdbJoinType.NONE &&//
                   !domain6.isHasUnion() &&      //
                   !domain6.isHasWith();
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 1 && domain1.getSelectFunc().contains("count") &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("count") &&//
                   domain2.getArgs().get(0).contains("*");
        }
    }
}
