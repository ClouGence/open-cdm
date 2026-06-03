package com.clougence.clouddm.ds.secdomain.special.ob4my;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.ob4my.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySelectDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ObSecDomainResolve4QueryTest extends MySecDomainTestSupport {

    public ObSecDomainResolve4QueryTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }

    @Test
    public void select_1() {
        String sql1 = "select SQL_CALC_FOUND_ROWS * from all_column  limit 100";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MySelectDomain domain = (MySelectDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;

        String sql2 = "select all * from all_column  limit 100";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (MySelectDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;

        String sql3 = "select distinct * from all_column  limit 100";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql3), contextInfo());
        domain = (MySelectDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;

        String sql4 = "select unique * from all_column  limit 100";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql4), contextInfo());
        domain = (MySelectDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;
    }

    @Test
    public void selectWithRollup_1() {
        String sql1 = "select  * from all_column group by `column_name` with rollup";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MySelectDomain domain = (MySelectDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;

    }

    @Test
    public void selectOver_1() {
        String sql1 = "SELECT last_name, salary, SUM(salary) OVER (ORDER BY hiredate)  Variance\n"
                      + "FROM employees WHERE manager_id = 100 ORDER BY last_name, salary, \"Variance\";";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MySelectDomain domain = (MySelectDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }

    }

    @Test
    public void selectInto_1() {
        String sql1 = "SELECT distinct * FROM A FULL JOIN B USING(ID) INTO OUTFILE 'test.sql' \n" + "FIELDS TERMINATED BY ',' ENCLOSED BY '\"'\n" + "LINES TERMINATED BY '\\n';";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1),contextInfo());
        MySelectDomain domain = (MySelectDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.SELECT && domain.getAuditKind() == SecQueryKind.QUERY;
        //        assert domain.getCatalog() == null && domain.getTable().equals("employees");

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.SELECT;
        }
    }
}
