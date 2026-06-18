package com.clougence.clouddm.ds.secdomain.family.mc.notsupportschema;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.maxcompute.analysis.McResAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainResolveSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSplitAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McSchemaDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class McSecDomainResolve4DatabaseTest extends McSecDomainTestSupport {

    public McSecDomainResolve4DatabaseTest(){
        this.analysisSpi = new McResAnalysisSpi(null);
        this.resolveSpi = new McSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new McSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MaxCompute;
    }

    @Test
    public void createDataBase_1() {
        String sql = "create schema `abc`";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
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
            McSchemaDomain domain = (McSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_2() {
        String sql = "create schema abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
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
            McSchemaDomain domain = (McSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBase_4() {
        String sql = "create schema `abc`";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
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
            McSchemaDomain domain = (McSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBaseUsingIfNotExists_1() {
        String sql = "create schema abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
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
            McSchemaDomain domain = (McSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert !domain.isIfNotExists() && domain.getCatalog() == null && domain.getSchema().equals("abc");
        }
    }

    @Test
    public void createDataBaseUsingIfNotExists_2() {
        String sql = "create schema if not exists qq.abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/qq/abc/");
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
            McSchemaDomain domain = (McSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getSchema().equals("abc") && domain.getCatalog().equals("qq");
            assert domain.isIfNotExists();
        }
    }

    @Test
    public void dropDataBaseUsingIfExists_0() {
        String sql = "drop database abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
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
            McSchemaDomain domain = (McSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert !domain.isIfExists();
        }
    }

    @Test
    public void dropDataBaseUsingIfExists_1() {
        String sql = "drop database if exists abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
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
            McSchemaDomain domain = (McSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.isIfExists();
        }
    }

    @Test
    public void dropDataBaseUsingIfExists_2() {
        String sql = "drop schema if exists aa.abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/aa/abc/");
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
            McSchemaDomain domain = (McSchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog().equals("aa") && domain.getSchema().equals("abc");
            assert domain.isIfExists();
        }
    }

}
