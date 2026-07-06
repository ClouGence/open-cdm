package com.clougence.clouddm.ds.secdomain.special.hologres;

import java.util.List;

import org.junit.Test;

import com.clougence.sql.postgres.resource.PgResAnalysisSpi;
import com.clougence.sql.postgres.security.PgSecDomainResolveSpi;
import com.clougence.sql.postgres.split.PgSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4Query1Test;
import com.clougence.clouddm.sdk.sql.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4Query1Test extends PgSecDomainResolve4Query1Test {

    public PgFamilySecDomainResolve4Query1Test(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Hologres;
    }

    @Test
    public void blockTest() {
        String sql = "do $$\n" + "begin\n" + "    CREATE TABLE base_sales3(\n" + "  day text not null,\n" + "  hour int ,\n" + "  ts timestamptz,\n" + "  amount float,\n"
                     + "  pk text not null primary key\n" + ");\n" + "CALL SET_TABLE_PROPERTY('base_sales3', 'mutate_type', 'appendonly');\n" + "\n"
                     + "--当实时物化视图被Drop后，可以取消明细表的appendonly属性，执行以下命令\n" + "--CALL SET_TABLE_PROPERTY('base_sales', 'mutate_type', 'none');\n" + "\n"
                     + "CREATE MATERIALIZED VIEW mv_sales3 AS\n" + "  SELECT\n" + "    day,\n" + "    hour,\n" + "    avg(amount) AS amount_avg\n" + "  FROM base_sales34\n"
                     + "  GROUP BY day, hour;\n" + "end $$";
        List<ResObject> resList = parserRes(sql);

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SQL_BLOCK;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
    }
}
