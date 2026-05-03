package com.clougence.clouddm.ds.secdomain.special.gs;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.gauss.analysis.gs.GsResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4Query1Test;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4Query1Test extends PgSecDomainResolve4Query1Test {

    public PgFamilySecDomainResolve4Query1Test(){
        this.analysisSpi = new GsResAnalysisSpi(null);
        this.resolveSpi = new GsSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDB;
    }

    @Test
    public void minusTest() {
        String sql = "select * from table_1 MINUS select * from table_2";

        List<ResObject> resList = parserRes(sql);
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
}
