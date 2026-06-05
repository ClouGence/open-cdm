package com.clougence.clouddm.ds.secdomain.special.por4x;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.polardb.analysis.porx.PorXResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyInsertDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PoXSecDomainResolve4InsertTest extends MySecDomainTestSupport {

    public PoXSecDomainResolve4InsertTest(){
        this.analysisSpi = new PorXResAnalysisSpi(null);
        this.resolveSpi = new PorXSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDbX;
    }

    @Test
    public void insertColumn_1() {
        String sql = "insert low_priority into test (a) values(1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test");
            assert domain1.getColumns().size() == 1 && domain1.getColumns().contains("a");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertColumn_2() {
        String sql = "insert low_priority into test (a) values(1) ON DUPLICATE KEY UPDATE col_name = 1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getColumns().size() == 2 && domain1.getColumns().contains("a") && domain1.getColumns().contains("col_name");
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.UPDATE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertSet_1() {
        String sql = "insert DELAYED into test set a=1,b=3;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test");
            assert domain1.getColumns().size() == 2 && domain1.getColumns().equals(Arrays.asList("a", "b"));
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertSet_2() {
        String sql = "insert DELAYED into test set a=1,b=3  ON DUPLICATE KEY UPDATE col_name = 1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getColumns().size() == 3 && domain1.getColumns().equals(Arrays.asList("a", "b", "col_name"));
            assert !domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.UPDATE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() && //
                   !domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
    }

    @Test
    public void insertSelect_1() {
        String sql = "insert HIGH_PRIORITY into test select * from test1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test");
            assert domain1.getColumns().isEmpty();
            assert domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.NONE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() &&  //
                   domain1.isOnlyValues() &&   //
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
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
    public void insertSelect_2() {
        String sql = "insert HIGH_PRIORITY into test select * from test1  ON DUPLICATE KEY UPDATE col_name = 1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            MyInsertDomain domain1 = (MyInsertDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.INSERT && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getColumns().size() == 1 && domain1.getColumns().get(0).equals("col_name");
            assert domain1.isFromSelect() &&   //
                   domain1.getConflict() == RdbInsertConflictStrategy.UPDATE &&//
                   !domain1.isHasSubQuery() &&  //
                   !domain1.isHasNullValue() &&  //
                   !domain1.isOnlyValues() &&   //todo
                   !domain1.isHasWith() &&      //
                   !domain1.isMultipleValues();
        }
        {
            MySelectDomain domain2 = (MySelectDomain) domainList.get(1);
            assert domain2.getSqlType() == SecQueryType.SELECT && domain2.getAuditKind() == SecQueryKind.QUERY && domain2.getMode() == RdbQueryMode.INSERT;
            assert domain2.getSelectColumns().isEmpty() &&//
                   domain2.getSelectVariables().isEmpty() &&//
                   domain2.getSelectFunc().isEmpty() &&//
                   domain2.getWhereColumns().isEmpty();
            assert !domain2.isHasAs() &&         //
                   domain2.isHasSelectAll() &&   //
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
}
