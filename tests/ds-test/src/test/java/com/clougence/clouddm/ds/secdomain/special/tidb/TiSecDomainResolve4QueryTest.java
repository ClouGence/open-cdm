package com.clougence.clouddm.ds.secdomain.special.tidb;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.ds.tidb.analysis.TiResAnalysisSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class TiSecDomainResolve4QueryTest extends MySecDomainTestSupport {

    public TiSecDomainResolve4QueryTest(){
        this.analysisSpi = new TiResAnalysisSpi(null);
        this.resolveSpi = new TiSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new TiSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.TiDB;
    }
    // can't parse
    //    @Test
    //    public void selectFetch_1() {
    //        List<RuleDomain> list = resolveSpi.resolveDomain( this.dataSourceType, "select * from table_name fetch first row only");
    //        RdbQueryDomain domain = (RdbQueryDomain) list.get(0);
    //        assert domain.getSqlType() == SecQueryType.SELECT && domain.getSqlKind() == SqlKind.QUERY;
    //        assert domain.getJoinType() == RdbJoinType.NONE;
    //        assert !domain.isEmptyFrom();
    //        assert domain.isSimpleSelect();
    //
    //        assert domain.getCatalog()==null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
    //    }

    @Test
    public void selectForUpdate_1() {
        String sql = "select * from table_name for update";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_name/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 0 &&//
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
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectForLockMode_1() {
        String sql = "select * from table_name lock in share mode";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_name/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().size() == 0 &&//
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
            assert !domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() && //
                   domain1.getJoinType() == RdbJoinType.NONE &&//
                   !domain1.isHasUnion() &&      //
                   !domain1.isHasWith();
            assert !domain1.isHasLimit();
        }
    }

    @Test
    public void selectHint_1() {
        String sql = "select  /*+ HASH_JOIN(@sel_1 t1@sel_1, t3) */ * from table_name lock in share mode";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_name/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty();
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

    //@Test
    public void selectPriority_1() {
        String sql = "select /*+ HASH_JOIN(@sel_1 t1@sel_1, t3) */ priority * from table_name lock in share mode";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_name/");
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
            MySelectDomain domain1 = (MySelectDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SELECT && domain1.getAuditKind() == SecQueryKind.QUERY && domain1.getMode() == RdbQueryMode.NORMAL;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_name") &&//
                   domain1.getSelectColumns().isEmpty() &&//
                   domain1.getSelectVariables().isEmpty() &&//
                   domain1.getSelectFunc().isEmpty() &&//
                   domain1.getWhereColumns().isEmpty();
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
}
