package com.clougence.clouddm.ds.secdomain.special.por4x;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.polardb.analysis.porx.PorXResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyUpdateDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PoXSecDomainResolve4UpdateTest extends MySecDomainTestSupport {

    public PoXSecDomainResolve4UpdateTest(){
        this.analysisSpi = new PorXResAnalysisSpi(null);
        this.resolveSpi = new PorXSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDbX;
    }

    @Test
    public void columnIgnore_1() {
        String sql = "update low_priority ignore table_name set aa = default where id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_name/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyUpdateDomain domain1 = (MyUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_name") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().contains("aa") &&//
                   domain1.getWhereColumns().size() == 1 && domain1.getWhereColumns().contains("id");
            assert domain1.isHasIgnore() &&     //
                   !domain1.isHasLimit() &&     //
                   !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }
}
