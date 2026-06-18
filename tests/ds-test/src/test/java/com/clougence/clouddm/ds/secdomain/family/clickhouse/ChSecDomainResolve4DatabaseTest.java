package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChSchemaDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4DatabaseTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4DatabaseTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void createDataBase_1() {
        String sql = "create database `abc`";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChSchemaDomain domain = (ChSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_2() {
        String sql = "create database \"abc\"";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }
        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            ChSchemaDomain domain = (ChSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_3() {
        String sql = "create database `abc`";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            ChSchemaDomain domain = (ChSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;

            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBaseUsingIfNotExists_1() {
        String sql = "create database abc engine = memory";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChSchemaDomain domain = (ChSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert !domain.isIfNotExists();
        }
    }

    @Test
    public void createDataBaseUsingIfNotExists_2() {
        String sql = "create database if not exists abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChSchemaDomain domain = (ChSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.isIfNotExists();
        }
    }

    @Test
    public void dropDataBaseUsingIfExists_0() {
        String sql = "drop database abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChSchemaDomain domain = (ChSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert !domain.isIfExists();
        }
    }

    @Test
    public void dropDataBaseUsingIfExists_1() {
        String sql = "drop database if exists abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChSchemaDomain domain = (ChSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.isIfExists();
        }
    }

    @Test
    public void dropDataBaseUsingIfExists_2() {
        String sql = "drop database if exists abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChSchemaDomain domain = (ChSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.isIfExists();
        }
    }

}
