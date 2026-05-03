package com.clougence.clouddm.ds.secdomain.family.mysql;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MySchemaDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MySecDomainResolve4DatabaseTest extends MySecDomainTestSupport {

    public MySecDomainResolve4DatabaseTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createDataBaseUsingCharacter_1() {
        String sql = "create database abc default collate utf8mb4_unicode_ci;";

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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet() == null;
        }
    }

    @Test
    public void createDataBaseUsingCharacter_2() {
        String sql = "create database abc default character set utf8mb4;";

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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCollate() == null && domain.getCharacterSet().equals("utf8mb4");
        }
    }

    @Test
    public void createDataBaseUsingCharacter_3() {
        String sql = "create database abc default character set utf8mb4 collate utf8mb4_unicode_ci;";

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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
        }
    }

    @Test
    public void createDataBaseUsingIfNotExists_1() {
        String sql = "create database abc;";

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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_SCHEMA && domain.getAuditKind() == SecQueryKind.CREATE;
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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.isIfExists();
        }
    }

    @Test
    public void dropDataBaseUsingIfExists_2() {
        String sql = "drop schema if exists abc;";

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
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_SCHEMA && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.isIfExists();
        }
    }

    @Test
    public void alterDataBase_0() {
        String sql = "alter schema abc default character set utf8mb4;";

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
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_SCHEMA && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.getCollate() == null && domain.getCharacterSet().equals("utf8mb4");
        }
    }

    @Test
    public void alterDataBase_1() {
        String sql = "alter schema abc default character set utf8mb4 collate utf8mb4_unicode_ci;";

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
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_SCHEMA;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MySchemaDomain domain = (MySchemaDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_SCHEMA && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc");
            assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
        }
    }

    @Test
    public void alterDataBase_2() {
        // todo can't parse
        //        List<RuleDomain> list = resolveSpi
        //            .resolveDomain(dataSourceType, "alter schema abc default character set utf8mb4 collate utf8mb4_unicode_ci encryption = 'n' READ ONLY 1;");
        //        MySchemaDomain domain = (MySchemaDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.ALTER_SCHEMA && domain.getSqlKind() == SqlKind.ALTER;
        //        assert domain.getCatalog()==null && domain.getSchema().equals("abc");
        //        assert domain.getCollate().equals("utf8mb4_unicode_ci") && domain.getCharacterSet().equals("utf8mb4");
    }
}
