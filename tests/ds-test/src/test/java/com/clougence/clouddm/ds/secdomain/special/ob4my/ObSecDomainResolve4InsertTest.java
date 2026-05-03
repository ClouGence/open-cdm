package com.clougence.clouddm.ds.secdomain.special.ob4my;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyInsertDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ObSecDomainResolve4InsertTest extends MySecDomainTestSupport {

    public ObSecDomainResolve4InsertTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }

    @Test
    public void describeTable_1() {
        // can't parse
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(DataSourceType.OceanBase, "insert into table set a=1,b=2");
        //        MyShowDomain domain = (MyShowDomain) list1.get(0);
        //        assert domain.getSqlType() == SecQueryType.MYSQL_SHOW && domain.getSqlKind() == SqlKind.QUERY;
        //        assert domain.getCatalog() == null && domain.getTable().equals("t1");
        //        assert domain.getTarget().equals(TargetType.Column);
        //        assert domain.getShowType().equals(MyShowType.COLUMNS);

        String sql1 = "INSERT INTO test1()values (1,2)";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MyInsertDomain domain = (MyInsertDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.INSERT && domain.getAuditKind() == SecQueryKind.DML;
        assert domain.getCatalog() == null && domain.getTable().equals("test1");

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }

        String sql2 = "INSERT INTO test1()value (1,2),(3,4)";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (MyInsertDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.INSERT && domain.getAuditKind() == SecQueryKind.DML;
        assert domain.getCatalog() == null && domain.getTable().equals("test1");

        splitScripts = this.splitAnalysisSpi.splitScript(sql2, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql2);
            assert splitScripts.get(0).getType() == SecQueryType.INSERT;
        }
    }
}
