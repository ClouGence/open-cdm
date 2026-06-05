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

public class Db2SecDomainResolve4Query1Test extends Db2SecDomainTestSupport {

    public Db2SecDomainResolve4Query1Test(){
        this.analysisSpi = new Db2ResAnalysisSpi();
        this.resolveSpi = new Db2SecDomainResolveSpi();
        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Db2;
    }

    @Test
    public void selectQuery_1() {
        String sql = "select id,name from abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   domain1.getSelectColumns().size() == 2 && domain1.getSelectColumns().equals(Arrays.asList("id", "name")) &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_2_1() {
        String sql = "select 1 from SYSIBM.SYSDUMMY1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
        }
        //
        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("SYSIBM") && domain1.getTable().equals("SYSDUMMY1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_2_2() {
        String sql = "select 1 + 1 from SYSIBM.SYSDUMMY1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.NORMAL;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("SYSIBM") && domain2.getTable().equals("SYSDUMMY1") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
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
            assert !domain2.isHasLimit();
        }
    }

    @Test
    public void selectQuery_2_3() {
        String sql = "select (1 + a) as a from users";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/users/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.NORMAL;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("users") &&//
                   domain3.getSelectColumns().size() == 1 && domain3.getSelectColumns().contains("a") &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
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
            assert !domain3.isHasLimit();
        }
    }

    @Test
    public void selectQuery_4() {
        String sql = "select value('1','2') from SYSIBM.SYSDUMMY1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
            assert resList.get(1).getType() == TargetType.Function &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/value/");
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
            assert domain1.getCatalog() == null && domain1.getSchema().equals("SYSIBM") && domain1.getTable().equals("SYSDUMMY1") &&//
                   domain1.getSelectColumns().size() == 0 &&    //
                   domain1.getSelectVariables().size() == 0 &&  //
                   domain1.getSelectFunc().size() == 1 && domain1.getSelectFunc().contains("value") &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
                   domain1.isFuncInSelect() &&   //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            RdbCallDomain domain2 = (RdbCallDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.CALL && domain2.getAuditKind() == SecQueryKind.CALL;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getCallName().equalsIgnoreCase("value") &&//
                   domain2.getArgs().size() == 2;
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_6() {
        String sql = "select id,* from table_1";

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
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_7() {
        String sql = "select id,* from table_1 as a";

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
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectQuery_8() {
        String sql = "select * from table_1 limit 10";

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
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0 && domain1.getWhereColumns().isEmpty();
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert domain1.isHasLimit();
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id"));
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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
        //
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
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("name");
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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
        //
        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 2 && domain1.getWhereColumns().equals(Arrays.asList("name", "id"));
            assert !domain1.isHasAs() &&          //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&        //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
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
                   domain1.isSimpleSelect();
            assert domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
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
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
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
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
    }

    @Test
    public void selectAs_1() {
        String sql = "select id as id1 from table_1";

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
        assert domainList.size() == 1;
        {
            Db2SelectDomain domain1 = (Db2SelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_1") &&//
                   domain1.getSelectColumns().size() == 1 && domain1.getSelectColumns().contains("id") &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert domain1.isHasAs() &&         //
                   !domain1.isHasSelectAll() &&  //
                   !domain1.isSelectInSelect() &&//
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
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
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
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
                   !domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   !domain1.isSelectInFrom() &&  //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_1") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_2") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&   //
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
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("t") &&//
                   domain1.getSelectColumns().size() == 0 &&//
                   domain1.getSelectVariables().size() == 0 &&//
                   domain1.getSelectFunc().size() == 0 &&//
                   domain1.getWhereColumns().size() == 0;
            assert !domain1.isHasAs() &&         //
                   domain1.isHasSelectAll() &&   //
                   !domain1.isSelectInSelect() &&//
                   !domain1.isFuncInSelect() &&  //
                   domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&       //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.SUB_FROM;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("t") &&//
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
                   domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(2);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.UNION;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("table_1") &&//
                   domain2.getSelectColumns().size() == 0 &&//
                   domain2.getSelectVariables().size() == 0 &&//
                   domain2.getSelectFunc().size() == 0 &&//
                   domain2.getWhereColumns().size() == 0;
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
                   !domain2.isSelectInSelect() &&//
                   !domain2.isFuncInSelect() &&  //
                   !domain2.isSelectInFrom() &&  //
                   !domain2.isEmptyFrom() &&     //
                   domain2.isSimpleSelect();
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&      //
                   !domain2.isHasWith();
            assert !domain2.isHasLimit();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(3);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.UNION;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("table_2") &&//
                   domain3.getSelectColumns().size() == 0 &&//
                   domain3.getSelectVariables().size() == 0 &&//
                   domain3.getSelectFunc().size() == 0 &&//
                   domain3.getWhereColumns().size() == 0;
            assert !domain3.isHasAs() &&         //
                   domain3.isHasSelectAll() &&   //
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
            assert !domain3.isHasLimit();
        }
    }

    @Test
    public void union_3() {
        String sql = "select * from table_1 a join (select * from table_1 union select * from table_2) as b on a.name = b.name";

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
        assert domainList.size() == 6;
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
                   !domain1.isSelectInFrom() &&   //
                   !domain1.isEmptyFrom() &&     //
                   !domain1.isSimpleSelect();
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1.isHasUnion() &&       //
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
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable() == null &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&   //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&      //
                   !domain4.isSelectInWhere() && //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   domain4.isHasUnion() &&      //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.UNION;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_1") &&//
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
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.UNION;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table_2") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 7;
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
                   !domain2.isSelectInWhere() && //
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
                   !domain4.isHasSelectAll() &&   //
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
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable() == null &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   !domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   !domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
        {
            Db2SelectDomain domain6 = (Db2SelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.UNION;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table_1") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
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
        {
            Db2SelectDomain domain7 = (Db2SelectDomain) domainList.get(6);
            assert domain7.getSqlType() == SecQueryType.SELECT && domain7.getAuditKind() == SecQueryKind.QUERY && domain7.getMode() == RdbQueryMode.UNION;
            assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("table_2") &&//
                   domain7.getSelectColumns().size() == 0 &&//
                   domain7.getSelectVariables().size() == 0 &&//
                   domain7.getSelectFunc().size() == 0 &&//
                   domain7.getWhereColumns().size() == 0;
            assert !domain7.isHasAs() &&         //
                   domain7.isHasSelectAll() &&   //
                   !domain7.isSelectInSelect() &&//
                   !domain7.isFuncInSelect() &&  //
                   !domain7.isSelectInFrom() &&  //
                   !domain7.isEmptyFrom() &&     //
                   domain7.isSimpleSelect();
            assert !domain7.isHasWhere() &&      //
                   !domain7.isSelectInWhere() && //
                   domain7.getJoinType() == RdbJoinType.NONE &&//
                   !domain7.isHasUnion() &&      //
                   !domain7.isHasWith();
        }
    }

    @Test
    public void union_5_1() {
        String sql = "select id = (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id limit 1) from table_1";

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
        assert domainList.size() == 7;
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
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
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
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
                   !domain4.isHasSelectAll() &&   //
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
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable() == null &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   !domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   !domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
        {
            Db2SelectDomain domain6 = (Db2SelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.UNION;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table_1") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
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
        {
            Db2SelectDomain domain7 = (Db2SelectDomain) domainList.get(6);
            assert domain7.getSqlType() == SecQueryType.SELECT && domain7.getAuditKind() == SecQueryKind.QUERY && domain7.getMode() == RdbQueryMode.UNION;
            assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("table_2") &&//
                   domain7.getSelectColumns().size() == 0 &&//
                   domain7.getSelectVariables().size() == 0 &&//
                   domain7.getSelectFunc().size() == 0 &&//
                   domain7.getWhereColumns().size() == 0;
            assert !domain7.isHasAs() &&         //
                   domain7.isHasSelectAll() &&   //
                   !domain7.isSelectInSelect() &&//
                   !domain7.isFuncInSelect() &&  //
                   !domain7.isSelectInFrom() &&  //
                   !domain7.isEmptyFrom() &&     //
                   domain7.isSimpleSelect();
            assert !domain7.isHasWhere() &&      //
                   !domain7.isSelectInWhere() && //
                   domain7.getJoinType() == RdbJoinType.NONE &&//
                   !domain7.isHasUnion() &&      //
                   !domain7.isHasWith();
        }
    }

    @Test
    public void union_5_2() {
        String sql = "select (select a.id from table_1 a left join (select * from table_1 union select * from table_2) b on a.id = b.id limit 1) = id from table_1";

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
        assert domainList.size() == 7;
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
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
            assert !domain2.isHasWhere() &&      //
                   !domain2.isSelectInWhere() && //
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
                   !domain4.isHasSelectAll() &&   //
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
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable() == null &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
                   !domain5.isHasSelectAll() &&   //
                   !domain5.isSelectInSelect() &&//
                   !domain5.isFuncInSelect() &&  //
                   !domain5.isSelectInFrom() &&  //
                   !domain5.isEmptyFrom() &&     //
                   !domain5.isSimpleSelect();
            assert !domain5.isHasWhere() &&      //
                   !domain5.isSelectInWhere() && //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   domain5.isHasUnion() &&      //
                   !domain5.isHasWith();
        }
        {
            Db2SelectDomain domain6 = (Db2SelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.UNION;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("table_1") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   domain6.isHasSelectAll() &&   //
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
        {
            Db2SelectDomain domain7 = (Db2SelectDomain) domainList.get(6);
            assert domain7.getSqlType() == SecQueryType.SELECT && domain7.getAuditKind() == SecQueryKind.QUERY && domain7.getMode() == RdbQueryMode.UNION;
            assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("table_2") &&//
                   domain7.getSelectColumns().size() == 0 &&//
                   domain7.getSelectVariables().size() == 0 &&//
                   domain7.getSelectFunc().size() == 0 &&//
                   domain7.getWhereColumns().size() == 0;
            assert !domain7.isHasAs() &&         //
                   domain7.isHasSelectAll() &&   //
                   !domain7.isSelectInSelect() &&//
                   !domain7.isFuncInSelect() &&  //
                   !domain7.isSelectInFrom() &&  //
                   !domain7.isEmptyFrom() &&     //
                   domain7.isSimpleSelect();
            assert !domain7.isHasWhere() &&      //
                   !domain7.isSelectInWhere() && //
                   domain7.getJoinType() == RdbJoinType.NONE &&//
                   !domain7.isHasUnion() &&      //
                   !domain7.isHasWith();
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

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql);
        //            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 7;
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1.isHasUnion() &&       //
                   domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("tab1Cnt") &&//
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
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("tab2Cnt") &&//
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
            assert !domain3.isHasWhere() &&       //
                   !domain3.isSelectInWhere() &&  //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&       //
                   !domain3.isHasWith();
        }
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.WITH;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("tab1Cnt") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&   //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(4);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.WITH_BODY;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_1") &&//
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
            assert !domain5.isHasWhere() &&       //
                   !domain5.isSelectInWhere() &&  //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&       //
                   !domain5.isHasWith();
        }
        {
            Db2SelectDomain domain6 = (Db2SelectDomain) domainList.get(5);
            assert domain6.getSqlType() == SecQueryType.SELECT && domain6.getAuditKind() == SecQueryKind.QUERY && domain6.getMode() == RdbQueryMode.WITH;
            assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("tab2Cnt") &&//
                   domain6.getSelectColumns().size() == 0 &&//
                   domain6.getSelectVariables().size() == 0 &&//
                   domain6.getSelectFunc().size() == 0 &&//
                   domain6.getWhereColumns().size() == 0;
            assert !domain6.isHasAs() &&         //
                   !domain6.isHasSelectAll() &&   //
                   !domain6.isSelectInSelect() &&//
                   !domain6.isFuncInSelect() &&  //
                   !domain6.isSelectInFrom() &&  //
                   !domain6.isEmptyFrom() &&     //
                   !domain6.isSimpleSelect();
            assert !domain6.isHasWhere() &&       //
                   !domain6.isSelectInWhere() &&  //
                   domain6.getJoinType() == RdbJoinType.NONE &&//
                   !domain6.isHasUnion() &&       //
                   !domain6.isHasWith();
        }
        {
            Db2SelectDomain domain7 = (Db2SelectDomain) domainList.get(6);
            assert domain7.getSqlType() == SecQueryType.SELECT && domain7.getAuditKind() == SecQueryKind.QUERY && domain7.getMode() == RdbQueryMode.WITH_BODY;
            assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("table_2") &&//
                   domain7.getSelectColumns().size() == 0 &&//
                   domain7.getSelectVariables().size() == 0 &&//
                   domain7.getSelectFunc().size() == 0 &&//
                   domain7.getWhereColumns().size() == 0;
            assert !domain7.isHasAs() &&         //
                   domain7.isHasSelectAll() &&   //
                   !domain7.isSelectInSelect() &&//
                   !domain7.isFuncInSelect() &&  //
                   !domain7.isSelectInFrom() &&  //
                   !domain7.isEmptyFrom() &&     //
                   domain7.isSimpleSelect();
            assert !domain7.isHasWhere() &&       //
                   !domain7.isSelectInWhere() &&  //
                   domain7.getJoinType() == RdbJoinType.NONE &&//
                   !domain7.isHasUnion() &&       //
                   !domain7.isHasWith();
        }
    }

    @Test
    public void with_query_3() {
        String sql = "with one as (select 1 as i from SYSIBM.SYSDUMMY1),\n" +//
                     "     tab1Cnt as (select * from table_1),\n" +//
                     "     tab2Cnt as (select * from table_2 where (select i from one) = 1)\n" +//
                     "select * from tab1Cnt a join tab2Cnt b on a.id = b.id";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/SYSIBM/SYSDUMMY1/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_1/");
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
        assert domainList.size() == 10;
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
            assert !domain1.isHasWhere() &&       //
                   !domain1.isSelectInWhere() &&  //
                   domain1.getJoinType() == RdbJoinType.INNER_JOIN &&//
                   !domain1.isHasUnion() &&       //
                   domain1.isHasWith();
        }
        {
            Db2SelectDomain domain2 = (Db2SelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.JOIN;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("tab1Cnt") &&//
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
            assert !domain2.isHasWhere() &&       //
                   !domain2.isSelectInWhere() &&  //
                   domain2.getJoinType() == RdbJoinType.NONE &&//
                   !domain2.isHasUnion() &&       //
                   !domain2.isHasWith();
        }
        {
            Db2SelectDomain domain3 = (Db2SelectDomain) domainList.get(2);
            assert domain3.getSqlType() == SecQueryType.SELECT && domain3.getAuditKind() == SecQueryKind.QUERY && domain3.getMode() == RdbQueryMode.JOIN;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("tab2Cnt") &&//
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
            assert !domain3.isHasWhere() &&       //
                   !domain3.isSelectInWhere() &&  //
                   domain3.getJoinType() == RdbJoinType.NONE &&//
                   !domain3.isHasUnion() &&       //
                   !domain3.isHasWith();
        }
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(3);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.WITH;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("one") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&   //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&     //
                   !domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain4 = (Db2SelectDomain) domainList.get(4);
            assert domain4.getSqlType() == SecQueryType.SELECT && domain4.getAuditKind() == SecQueryKind.QUERY && domain4.getMode() == RdbQueryMode.WITH_BODY;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("SYSIBM") && domain4.getTable().equals("SYSDUMMY1") &&//
                   domain4.getSelectColumns().size() == 0 &&//
                   domain4.getSelectVariables().size() == 0 &&//
                   domain4.getSelectFunc().size() == 0 &&//
                   domain4.getSelectValue().size() == 1 && domain4.getSelectValue().contains("i") &&//
                   domain4.getWhereColumns().size() == 0;
            assert !domain4.isHasAs() &&         //
                   !domain4.isHasSelectAll() &&  //
                   !domain4.isSelectInSelect() &&//
                   !domain4.isFuncInSelect() &&  //
                   !domain4.isSelectInFrom() &&  //
                   !domain4.isEmptyFrom() &&      //
                   domain4.isSimpleSelect();
            assert !domain4.isHasWhere() &&       //
                   !domain4.isSelectInWhere() &&  //
                   domain4.getJoinType() == RdbJoinType.NONE &&//
                   !domain4.isHasUnion() &&       //
                   !domain4.isHasWith();
        }
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(5);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.WITH;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("tab1Cnt") &&//
                   domain5.getSelectColumns().size() == 0 &&//
                   domain5.getSelectVariables().size() == 0 &&//
                   domain5.getSelectFunc().size() == 0 &&//
                   domain5.getWhereColumns().size() == 0;
            assert !domain5.isHasAs() &&         //
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
        {
            Db2SelectDomain domain5 = (Db2SelectDomain) domainList.get(6);
            assert domain5.getSqlType() == SecQueryType.SELECT && domain5.getAuditKind() == SecQueryKind.QUERY && domain5.getMode() == RdbQueryMode.WITH_BODY;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("table_1") &&//
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
            assert !domain5.isHasWhere() &&       //
                   !domain5.isSelectInWhere() &&  //
                   domain5.getJoinType() == RdbJoinType.NONE &&//
                   !domain5.isHasUnion() &&       //
                   !domain5.isHasWith();
        }
        {
            Db2SelectDomain domain8 = (Db2SelectDomain) domainList.get(7);
            assert domain8.getSqlType() == SecQueryType.SELECT && domain8.getAuditKind() == SecQueryKind.QUERY && domain8.getMode() == RdbQueryMode.WITH;
            assert domain8.getCatalog() == null && domain8.getSchema() == null && domain8.getTable().equals("tab2Cnt") &&//
                   domain8.getSelectColumns().size() == 0 &&//
                   domain8.getSelectVariables().size() == 0 &&//
                   domain8.getSelectFunc().size() == 0 &&//
                   domain8.getWhereColumns().size() == 0;
            assert !domain8.isHasAs() &&         //
                   !domain8.isHasSelectAll() &&   //
                   !domain8.isSelectInSelect() &&//
                   !domain8.isFuncInSelect() &&  //
                   !domain8.isSelectInFrom() &&  //
                   !domain8.isEmptyFrom() &&     //
                   !domain8.isSimpleSelect();
            assert !domain8.isHasWhere() &&       //
                   !domain8.isSelectInWhere() &&  //
                   domain8.getJoinType() == RdbJoinType.NONE &&//
                   !domain8.isHasUnion() &&       //
                   !domain8.isHasWith();
        }
        {
            Db2SelectDomain domain8 = (Db2SelectDomain) domainList.get(8);
            assert domain8.getSqlType() == SecQueryType.SELECT && domain8.getAuditKind() == SecQueryKind.QUERY && domain8.getMode() == RdbQueryMode.WITH_BODY;
            assert domain8.getCatalog() == null && domain8.getSchema() == null && domain8.getTable().equals("table_2") &&//
                   domain8.getSelectColumns().size() == 0 &&//
                   domain8.getSelectVariables().size() == 0 &&//
                   domain8.getSelectFunc().size() == 0 &&//
                   domain8.getWhereColumns().size() == 0;
            assert !domain8.isHasAs() &&         //
                   domain8.isHasSelectAll() &&   //
                   !domain8.isSelectInSelect() &&//
                   !domain8.isFuncInSelect() &&  //
                   !domain8.isSelectInFrom() &&  //
                   !domain8.isEmptyFrom() &&     //
                   domain8.isSimpleSelect();
            assert domain8.isHasWhere() &&       //
                   domain8.isSelectInWhere() &&  //
                   domain8.getJoinType() == RdbJoinType.NONE &&//
                   !domain8.isHasUnion() &&      //
                   !domain8.isHasWith();
        }
        {
            Db2SelectDomain domain9 = (Db2SelectDomain) domainList.get(9);
            assert domain9.getSqlType() == SecQueryType.SELECT && domain9.getAuditKind() == SecQueryKind.QUERY && domain9.getMode() == RdbQueryMode.SUB_WHERE;
            assert domain9.getCatalog() == null && domain9.getSchema() == null && domain9.getTable().equals("one") &&//
                   domain9.getSelectColumns().size() == 1 && domain9.getSelectColumns().contains("i") &&//
                   domain9.getSelectVariables().size() == 0 &&//
                   domain9.getSelectFunc().size() == 0 &&//
                   domain9.getWhereColumns().size() == 0;
            assert !domain9.isHasAs() &&         //
                   !domain9.isHasSelectAll() &&   //
                   !domain9.isSelectInSelect() &&//
                   !domain9.isFuncInSelect() &&  //
                   !domain9.isSelectInFrom() &&  //
                   !domain9.isEmptyFrom() &&     //
                   domain9.isSimpleSelect();
            assert !domain9.isHasWhere() &&       //
                   !domain9.isSelectInWhere() &&  //
                   domain9.getJoinType() == RdbJoinType.NONE &&//
                   !domain9.isHasUnion() &&       //
                   !domain9.isHasWith();
        }
    }

    //@Test
    public void complex_1() {
        String sql = "WITH updated_data AS (" + //
                     "    UPDATE table1" + //
                     "        SET column1 = 1" + //
                     "        WHERE condition = 1" + //
                     "        RETURNING id" + //
                     "), delete_data AS (" + //
                     "    delete from table2" + //
                     "        WHERE condition = 1" + //
                     "        RETURNING id" + //
                     ")" + //
                     "insert into table3" + //
                     "       select id from updated_data" + //
                     "       union all" + //
                     "       select id from delete_data;";

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
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
}
