package com.clougence.clouddm.ds.secdomain.special.ob4my;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyShowDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyShowType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ObSecDomainResolve4ShowTest extends MySecDomainTestSupport {

    public ObSecDomainResolve4ShowTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }

    @Test
    public void describeTable_1() {
        String sql1 = "describe t1";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1),contextInfo());
        MyShowDomain domain = (MyShowDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
        assert domain.getCatalog() == null && domain.getTable().equals("t1");
        assert domain.getTarget().equals(TargetType.Column);
        assert domain.getShowType().equals(MyShowType.COLUMNS);

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        String sql2 = "describe t1 tt";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2),contextInfo());
        domain = (MyShowDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
        assert domain.getCatalog() == null && domain.getTable().equals("t1");
        assert domain.getTarget().equals(TargetType.Column);
        assert domain.getShowType().equals(MyShowType.COLUMNS);

        splitScripts = this.splitAnalysisSpi.splitScript(sql2, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql2);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        String sql3 = "desc t1";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql3),contextInfo());
        domain = (MyShowDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
        assert domain.getCatalog() == null && domain.getTable().equals("t1");
        assert domain.getTarget().equals(TargetType.Column);
        assert domain.getShowType().equals(MyShowType.COLUMNS);

        splitScripts = this.splitAnalysisSpi.splitScript(sql3, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql3);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        String sql4 = "desc t1 tt";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql4),contextInfo());
        domain = (MyShowDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
        assert domain.getCatalog() == null && domain.getTable().equals("t1");
        assert domain.getTarget().equals(TargetType.Column);
        assert domain.getShowType().equals(MyShowType.COLUMNS);

        splitScripts = this.splitAnalysisSpi.splitScript(sql4, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql4);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }
    }
}
