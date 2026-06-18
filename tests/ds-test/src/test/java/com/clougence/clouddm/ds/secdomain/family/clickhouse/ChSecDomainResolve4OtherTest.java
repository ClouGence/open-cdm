package com.clougence.clouddm.ds.secdomain.family.clickhouse;// package com.clougence.clouddm.dsfamily.mysql.secdomain;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4OtherTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4OtherTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void dropMView() {
        String sql = "drop  VIEW sale_detail_view";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/sale_detail_view/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_VIEW && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("sale_detail_view");
        }
    }

    @Test
    public void dropView2() {
        String sql = "drop  VIEW x.sale_detail_view";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/x/sale_detail_view/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_VIEW && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("x") && domain.getView().equals("sale_detail_view");
        }
    }

    @Test
    public void createView2() {
        String sql = "Create view a.`ss` as select * from testdb";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/a/ss/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_VIEW && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("a") && domain.getView().equals("ss");
        }
    }
}
