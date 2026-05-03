package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4DatabaseTest extends MsSecDomainTestSupport {

    public MsSecDomainResolve4DatabaseTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void createDataBase_1() {
        String sql = "create database abc";

        List<ResObject> resList = parserRes(sql);
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
    public void alterDataBase_rename() {
        String sql = "exec sp_rename abc, abc2, 'DATABASE';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Catalog &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
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

        List<ResObject> resList = parserRes(sql);
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
            MsCatalogDomain domain1 = (MsCatalogDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_CATALOG && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog().equals("abc");
            assert !domain1.isIfExists();
            assert domain1.getComment() == null;
            assert domain1.getCollate() == null;
        }
    }

    @Test
    public void dropDataBase_1_2() {
        String sql = "drop database if exists abc;";

        List<ResObject> resList = parserRes(sql);
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
            MsCatalogDomain domain2 = (MsCatalogDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.DROP_CATALOG && domain2.getAuditKind() == SecQueryKind.DROP;
            assert domain2.getCatalog().equals("abc");
            assert domain2.isIfExists();
            assert domain2.getComment() == null;
            assert domain2.getCollate() == null;
        }
    }
}
