package com.clougence.clouddm.ds.secdomain.family.postgres;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgCatalogDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgSecDomainResolve4DatabaseTest extends PgSecDomainTestSupport {

    public PgSecDomainResolve4DatabaseTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PostgreSQL;
    }

    @Test
    public void createDataBase_1() {
        String sql = "create database abc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain = (RdbCatalogDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_CATALOG && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("abc") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_owner_1() {
        String sql = "create database abc owner zyc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_CATALOG && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog().equals("abc") && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_owner_2() {
        String sql = "create database abc owner = zyc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain2 = (RdbCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_CATALOG && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog().equals("abc") && domain2.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_owner_3() {
        String sql = "create database abc with owner zyc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain3 = (RdbCatalogDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_CATALOG && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog().equals("abc") && domain3.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_owner_4() {
        String sql = "create database abc with owner = zyc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain4 = (RdbCatalogDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.CREATE_CATALOG && domain4.getAuditKind() == SecQueryKind.CREATE;
            assert domain4.getCatalog().equals("abc") && domain4.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_template_1() {
        String sql = "create database abc template template1";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_CATALOG && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog().equals("abc") && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_template_2() {
        String sql = "create database abc template = template1";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain2 = (RdbCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_CATALOG && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog().equals("abc") && domain2.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_template_3() {
        String sql = "create database abc with template template1";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain3 = (RdbCatalogDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_CATALOG && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog().equals("abc") && domain3.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_template_4() {
        String sql = "create database abc with template = template1";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain4 = (RdbCatalogDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.CREATE_CATALOG && domain4.getAuditKind() == SecQueryKind.CREATE;
            assert domain4.getCatalog().equals("abc") && domain4.getOptions().isEmpty();
        }
    }

    //CREATE DATABASE name
    //    [ WITH ]
    //           [ ENCODING [=] encoding ]
    //           [ STRATEGY [=] strategy ]
    //           [ LOCALE [=] locale ]
    //           [ LC_COLLATE [=] lc_collate ]
    //           [ LC_CTYPE [=] lc_ctype ]
    //           [ BUILTIN_LOCALE [=] builtin_locale ]
    //           [ ICU_LOCALE [=] icu_locale ]
    //           [ ICU_RULES [=] icu_rules ]
    //           [ LOCALE_PROVIDER [=] locale_provider ]
    //           [ COLLATION_VERSION = collation_version ]
    //           [ TABLESPACE [=] tablespace_name ]
    //           [ ALLOW_CONNECTIONS [=] allowconn ]
    //           [ CONNECTION LIMIT [=] connlimit ]
    //           [ IS_TEMPLATE [=] istemplate ]
    //           [ OID [=] oid ]

    @Test
    public void alterDataBase_options_allow_connections_1() {
        String sql = "alter database abc allow_connections false";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_CATALOG && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("abc") && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void alterDataBase_options_allow_connections_2() {
        String sql = "alter database abc with allow_connections true";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain2 = (RdbCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_CATALOG && domain2.getAuditKind() == SecQueryKind.ALTER;
            assert domain2.getCatalog().equals("abc") && domain2.getOptions().isEmpty();
        }
    }

    //@Test
    public void alterDataBase_options_connection_1() {
        //        String sql = "alter database abc connection limit -1";
        //
        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 1;
        //        {
        //            assert resList.get(0).getType() == TargetType.Table &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
        //        }
        //
        //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 1;
        //        {
        //            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
        //            assert domain1.getSqlType() == SecQueryType.CREATE_CATALOG && domain1.getSqlKind() == SqlKind.CREATE;
        //            assert domain1.getCatalog().equals("abc") && domain1.getOptions().isEmpty();
        //        }
    }

    //@Test
    public void alterDataBase_options_connection_2() {
        //        String sql = "alter database abc with connection limit -1";
        //
        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 1;
        //        {
        //            assert resList.get(0).getType() == TargetType.Table &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
        //        }
        //
        //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 1;
        //        {
        //            RdbCatalogDomain domain2 = (RdbCatalogDomain) domainList.get(0);
        //            assert domain2.getSqlType() == SecQueryType.CREATE_CATALOG && domain2.getSqlKind() == SqlKind.CREATE;
        //            assert domain2.getCatalog().equals("abc") && domain2.getOptions().isEmpty();
        //        }
    }

    @Test
    public void alterDataBase_options_is_template_1() {
        String sql = "alter database abc is_template true";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_CATALOG && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("abc") && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void alterDataBase_options_is_template_2() {
        String sql = "alter database abc with is_template true";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain2 = (RdbCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_CATALOG && domain2.getAuditKind() == SecQueryKind.ALTER;
            assert domain2.getCatalog().equals("abc") && domain2.getOptions().isEmpty();
        }
    }

    @Test
    public void alterDataBase_owner_1() {
        String sql = "alter database abc owner to abc2";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_CATALOG && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("abc") && domain1.getOptions().get(OPT_CATALOG_OWNER).equals("abc2");
        }
    }

    @Test
    public void alterDataBase_owner_2() {
        String sql = "alter database abc owner to CURRENT_ROLE";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain2 = (RdbCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_CATALOG && domain2.getAuditKind() == SecQueryKind.ALTER;
            assert domain2.getCatalog().equals("abc") && domain2.getOptions().get(OPT_CATALOG_OWNER).equals("CURRENT_ROLE");
        }
    }

    @Test
    public void alterDataBase_owner_3() {
        String sql = "alter database abc owner to CURRENT_USER";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain3 = (RdbCatalogDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.ALTER_CATALOG && domain3.getAuditKind() == SecQueryKind.ALTER;
            assert domain3.getCatalog().equals("abc") && domain3.getOptions().get(OPT_CATALOG_OWNER).equals("CURRENT_USER");
        }
    }

    @Test
    public void alterDataBase_owner_4() {
        String sql = "alter database abc owner to SESSION_USER";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain4 = (RdbCatalogDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.ALTER_CATALOG && domain4.getAuditKind() == SecQueryKind.ALTER;
            assert domain4.getCatalog().equals("abc") && domain4.getOptions().get(OPT_CATALOG_OWNER).equals("SESSION_USER");
        }
    }

    @Test
    public void alterDataBase_tablespace() {
        String sql = "alter database abc set tablespace myspace";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_CATALOG && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("abc") && domain1.getOptions().get(OPT_CATALOG_TABLESPACE).equals("myspace");
        }
    }

    @Test
    public void alterDataBase_refresh_collation_version() {
        String sql = "alter database abc refresh collation version";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_CATALOG && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("abc") && domain1.getOptions().get(OPT_CATALOG_RCV).equals("true");
        }
    }

    @Test
    public void alterDataBase_reset_1() {
        String sql = "alter database abc reset all";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_CATALOG && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("abc") && domain1.getOptions().get(OPT_CATALOG_RESET).equals("all");
        }
    }

    @Test
    public void alterDataBase_reset_2() {
        String sql = "alter database abc reset all";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain2 = (RdbCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_CATALOG && domain2.getAuditKind() == SecQueryKind.ALTER;
            assert domain2.getCatalog().equals("abc") && domain2.getOptions().get(OPT_CATALOG_RESET).equals("all");
        }
    }

    @Test
    public void alterDataBase_reset_3() {
        String sql = "alter database abc reset datestyle";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain3 = (RdbCatalogDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.ALTER_CATALOG && domain3.getAuditKind() == SecQueryKind.ALTER;
            assert domain3.getCatalog().equals("abc") && domain3.getOptions().get(OPT_CATALOG_RESET).equals("datestyle");
        }
    }

    //@Test
    public void alterDataBase_set() {
        //        String sql = "alter database abc set datestyle from current";
        //
        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 1;
        //        {
        //            assert resList.get(0).getType() == TargetType.Catalog &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        //        }
        //
        //        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(this.dataSourceType, sql);
        //        assert domainList.size() == 1;
        //        {
        //            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
        //            assert domain1.getSqlType() == SecQueryType.ALTER_CATALOG && domain1.getSqlKind() == SqlKind.ALTER;
        //            assert domain1.getCatalog().equals("abc") && domain1.getOptions().get(OPT_CATALOG_CONF_SET).equals("datestyle");
        //        }
    }

    @Test
    public void alterDataBase_configParameter_1() {
        String sql = "alter database abc set datestyle = 'iso, mdy';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_CATALOG && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("abc") &&//
                   domain1.getOptions().get(OPT_CATALOG_CONF_NAME).equals("datestyle") &&//
                   domain1.getOptions().get(OPT_CATALOG_CONF_VALUE).equals("iso, mdy");
        }
    }

    @Test
    public void alterDataBase_configParameter_2() {
        String sql = "alter database abc set datestyle to 'iso, mdy';";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain2 = (RdbCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_CATALOG && domain2.getAuditKind() == SecQueryKind.ALTER;
            assert domain2.getCatalog().equals("abc") &&//
                   domain2.getOptions().get(OPT_CATALOG_CONF_NAME).equals("datestyle") &&//
                   domain2.getOptions().get(OPT_CATALOG_CONF_VALUE).equals("iso, mdy");
        }
    }

    @Test
    public void alterDataBase_configParameter_3() {
        String sql = "alter database abc set datestyle to default;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain3 = (RdbCatalogDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.ALTER_CATALOG && domain3.getAuditKind() == SecQueryKind.ALTER;
            assert domain3.getCatalog().equals("abc") &&//
                   domain3.getOptions().get(OPT_CATALOG_CONF_NAME).equals("datestyle") &&//
                   domain3.getOptions().get(OPT_CATALOG_CONF_VALUE).equalsIgnoreCase("default");
        }
    }

    @Test
    public void alterDataBase_configParameter_4() {
        String sql = "alter database abc set datestyle = default;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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
            RdbCatalogDomain domain4 = (RdbCatalogDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.ALTER_CATALOG && domain4.getAuditKind() == SecQueryKind.ALTER;
            assert domain4.getCatalog().equals("abc") &&//
                   domain4.getOptions().get(OPT_CATALOG_CONF_NAME).equals("datestyle") &&//
                   domain4.getOptions().get(OPT_CATALOG_CONF_VALUE).equalsIgnoreCase("default");
        }
    }

    @Test
    public void alterDataBase_rename() {
        String sql = "alter database abc rename to abc2";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.RENAME_CATALOG;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbCatalogDomain domain1 = (RdbCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.RENAME_CATALOG && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("abc") && domain1.getNewName().equals("abc2") && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void dropDataBase_1_1() {
        String sql = "drop database abc;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_CATALOG;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgCatalogDomain domain1 = (PgCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_CATALOG && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog().equals("abc");
            assert !domain1.isIfExists();
            assert !domain1.isUsingForce();
        }
    }

    @Test
    public void dropDataBase_1_2() {
        String sql = "drop database if exists abc;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_CATALOG;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgCatalogDomain domain2 = (PgCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.DROP_CATALOG && domain2.getAuditKind() == SecQueryKind.DROP;
            assert domain2.getCatalog().equals("abc");
            assert domain2.isIfExists();
            assert !domain2.isUsingForce();
        }
    }

    @Test
    public void dropDataBase_2_1() {
        String sql = "drop database if exists abc with (force);";

        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 1;
        //        {
        //            assert resList.get(0).getType() == TargetType.Catalog &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        //        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_CATALOG;
        }
        //
        //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 1;
        //        {
        //            PgCatalogDomain domain3 = (PgCatalogDomain) domainList.get(0);
        //            assert domain3.getSqlType() == SecQueryType.DROP_CATALOG && domain3.getSqlKind() == SqlKind.DROP;
        //            assert domain3.getCatalog().equals("abc");
        //            assert domain3.isIfExists();
        //            assert domain3.isUsingForce();
        //        }
    }

    @Test
    public void dropDataBase_2_2() {
        String sql = "drop database if exists abc (force);";

        //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
        //        assert resList.size() == 1;
        //        {
        //            assert resList.get(0).getType() == TargetType.Catalog &&//
        //                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        //        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_CATALOG;
        }

        //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
        //        assert domainList.size() == 1;
        //        {
        //            PgCatalogDomain domain4 = (PgCatalogDomain) domainList.get(0);
        //            assert domain4.getSqlType() == SecQueryType.DROP_CATALOG && domain4.getSqlKind() == SqlKind.DROP;
        //            assert domain4.getCatalog().equals("abc");
        //            assert domain4.isIfExists();
        //            assert domain4.isUsingForce();
        //        }
    }
}
