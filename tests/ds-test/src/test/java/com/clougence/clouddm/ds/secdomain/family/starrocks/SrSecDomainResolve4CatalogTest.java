package com.clougence.clouddm.ds.secdomain.family.starrocks;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.starrocks.analysis.SrResAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSecDomainResolveSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSplitAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrCatalogDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class SrSecDomainResolve4CatalogTest extends SrSecDomainTestSupport {

    public SrSecDomainResolve4CatalogTest(){
        this.analysisSpi = new SrResAnalysisSpi(null);
        this.resolveSpi = new SrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.StarRocks;
    }

    @Test
    public void createCatalog() {
        String sql = "CREATE external CATALOG m1 PROPERTIES ( " + " \"type\"=\"jdbc\", " + " \"user\"=\"root\", " + " \"password\"=\"123456789\", "
                     + " \"jdbc_url\" = \"jdbc:mysql://192.168.0.180:3306/clouddm1?useSSL=false\", "
                     + " \"driver_url\" = \"https://doris-community-test-1308700295.cos.ap-hongkong.myqcloud.com/jdbc_driver/mysql-connector-java-8.0.25.jar\", "
                     + " \"driver_class\" = \"com.mysql.cj.jdbc.Driver\" " + ");";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/m1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_CATALOG;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrCatalogDomain domain = (SrCatalogDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_CATALOG && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("m1");
            assert domain.getOptions().size() == 6;
            assert domain.getOptions().get("type").equals("jdbc") && domain.getOptions().get("user").equals("root") && domain.getOptions().get("password").equals("123456789")
                   && domain.getOptions().get("jdbc_url").equals("jdbc:mysql://192.168.0.180:3306/clouddm1?useSSL=false")
                   && domain.getOptions()
                       .get("driver_url")
                       .equals("https://doris-community-test-1308700295.cos.ap-hongkong.myqcloud.com/jdbc_driver/mysql-connector-java-8.0.25.jar")
                   && domain.getOptions().get("driver_class").equals("com.mysql.cj.jdbc.Driver");
        }
    }

    @Test
    public void createCatalogComment() {
        String sql = "CREATE external CATALOG `m1` comment 'testttt' PROPERTIES ( " + " \"type\"=\"jdbc\", " + " \"user\"=\"root\", " + " \"password\"=\"123456789\", "
                     + " \"jdbc_url\" = \"jdbc:mysql://192.168.0.180:3306/clouddm1?useSSL=false\", "
                     + " \"driver_url\" = \"https://doris-community-test-1308700295.cos.ap-hongkong.myqcloud.com/jdbc_driver/mysql-connector-java-8.0.25.jar\", "
                     + " \"driver_class\" = \"com.mysql.cj.jdbc.Driver\" " + ");";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/m1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_CATALOG;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrCatalogDomain domain = (SrCatalogDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_CATALOG && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("m1") && domain.getComment().equals("testttt");
            assert domain.getOptions().size() == 6;
            assert domain.getOptions().get("type").equals("jdbc") && domain.getOptions().get("user").equals("root") && domain.getOptions().get("password").equals("123456789")
                   && domain.getOptions().get("jdbc_url").equals("jdbc:mysql://192.168.0.180:3306/clouddm1?useSSL=false")
                   && domain.getOptions()
                       .get("driver_url")
                       .equals("https://doris-community-test-1308700295.cos.ap-hongkong.myqcloud.com/jdbc_driver/mysql-connector-java-8.0.25.jar")
                   && domain.getOptions().get("driver_class").equals("com.mysql.cj.jdbc.Driver");
        }
    }

    @Test
    public void dropCatalog() {
        String sql = "drop CATALOG m1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/m1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_CATALOG;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrCatalogDomain domain = (SrCatalogDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_CATALOG && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog().equals("m1");
            assert domain.getOptions().isEmpty();
            assert !domain.isIfExists() && domain.getComment() == null;
        }
    }

    @Test
    public void dropCatalog_1() {
        String sql = "drop CATALOG if exists `m1`;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/m1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_CATALOG;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrCatalogDomain domain = (SrCatalogDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_CATALOG && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog().equals("m1");
            assert domain.getOptions().isEmpty();
            assert domain.isIfExists() && domain.getComment() == null;
        }
    }

    @Test
    public void alterCatalogComment() {
        String sql = "ALTER CATALOG catalog_name set (\"aa\" = \"bb\");";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/catalog_name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_CATALOG;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrCatalogDomain domain = (SrCatalogDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_CATALOG && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog().equals("catalog_name") && domain.getNewName() == null;
            assert domain.getOptions().size() == 1;
        }
    }

}
