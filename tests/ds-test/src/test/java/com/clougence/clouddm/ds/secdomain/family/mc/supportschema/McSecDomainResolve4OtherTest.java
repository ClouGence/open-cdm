package com.clougence.clouddm.ds.secdomain.family.mc.supportschema;// package com.clougence.clouddm.dsfamily.mysql.secdomain;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.maxcompute.analysis.McResAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainResolveSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbResourceDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

// https://dev.mysql.com/doc/refman/8.4/en/show.html
public class McSecDomainResolve4OtherTest extends McSecDomainTestSupport {

    public McSecDomainResolve4OtherTest(){
        this.analysisSpi = new McResAnalysisSpi(null);
        this.resolveSpi = new McSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new McSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MaxCompute;
    }

    @Test
    public void createView() {
        String sql = "CREATE VIEW IF NOT EXISTS sale_detail_view \n" + "(store_name, customer_id, price, sale_date, region) \n" + "comment 'a view for table sale_detail' \n"
                     + "AS SELECT * FROM sale_detail;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/sale_detail_view/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("sale_detail_view");
        }
    }

    @Test
    public void createView2() {
        String sql = "CREATE MATERIALIZED VIEW IF NOT EXISTS sales_mv\n" + "LIFECYCLE 365\n" + "BUILD DEFERRED\n" + "(\n" + "    sale_id ,\n" + "    product_name  ,\n"
                     + "    total_amount  ,\n" + "    sale_date \n" + ")\n" + "DISABLE REWRITE\n" + "COMMENT '销售汇总物化视图，用于加速报表查询'\n" + "PARTITIONED BY (sale_date)\n"
                     + "CLUSTERED BY (product_name)\n" + "SORTED BY (total_amount DESC)\n" + "INTO 32 BUCKETS\n" + "REFRESH EVERY 2 HOURS\n" + "TBLPROPERTIES(\n"
                     + "    \"compressionstrategy\"=\"high\",\n" + "    \"enable_auto_substitute\"=\"true\",\n" + "    \"enable_auto_refresh\"=\"true\",\n"
                     + "    \"refresh_interval_minutes\"=\"120\",\n" + "    \"only_refresh_max_pt\"=\"true\"\n" + ")\n" + "AS\n" + "SELECT\n" + "    s.sale_id,\n"
                     + "    p.product_name,\n" + "    s.amount AS total_amount,\n" + "    s.sale_date\n" + "FROM\n" + "    my_project.sales s\n" + "JOIN\n"
                     + "    my_project.products p\n" + "ON\n" + "    s.product_id = p.product_id\n" + "WHERE\n" + "    s.sale_date >= '20240101';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/sales_mv/");
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
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("sales_mv");
        }
    }

    @Test
    public void alterMView() {
        String sql = "ALTER MATERIALIZED VIEW mf_mv_blank_pts REBUILD PARTITION (ds>0);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/mf_mv_blank_pts/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_VIEW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbViewDomain domain = (RdbViewDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_VIEW && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getView().equals("mf_mv_blank_pts");
        }
    }

    @Test
    public void dropView() {
        String sql = "drop VIEW IF  EXISTS sale_detail_view;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/sale_detail_view/");
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
    public void dropMView() {
        String sql = "drop MATERIALIZED VIEW IF  EXISTS sale_detail_view;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/sale_detail_view/");
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
    public void analyzeTable1() {
        String sql = "analyze table srcpart_test partition(ds='20201221') compute statistics for columns (key , value);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/srcpart_test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbResourceDomain domain = (RdbResourceDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getName().equals("srcpart_test");
        }
    }

    @Test
    public void analyzeTable2() {
        String sql = "analyze table a.`b`.srcpart_test partition(ds='20201221') compute statistics for columns (key , value);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/a/b/srcpart_test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbResourceDomain domain = (RdbResourceDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getCatalog().equals("a") && domain.getSchema().equals("b") && domain.getName().equals("srcpart_test");
        }
    }

}
