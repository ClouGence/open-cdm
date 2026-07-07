package com.clougence.clouddm.ds.secdomain.family.doris;

import java.util.List;

import org.junit.Test;

import com.clougence.sql.doris.resource.DrResAnalysisSpi;
import com.clougence.sql.doris.security.DrSecDomainResolveSpi;
import com.clougence.sql.doris.split.DrSplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class DrNoDomainTest extends DrSecDomainTestSupport {

    public DrNoDomainTest(){
        this.analysisSpi = new DrResAnalysisSpi(null);
        this.resolveSpi = new DrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new DrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Doris;
    }

    @Test
    public void switchCatalog() {
        String sql = "switch aa";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SWITCH_CATALOG;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.isEmpty();
    }

    @Test
    public void switchSchema() {
        String sql = "use jj";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SWITCH_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.isEmpty();
    }

}
