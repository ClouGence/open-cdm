package com.clougence.clouddm.ds.secdomain.family.postgres;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgSchemaDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgSecDomainResolve4SchemaTest extends PgSecDomainTestSupport {

    public PgSecDomainResolve4SchemaTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PostgreSQL;
    }

    @Test
    public void createSchema_1() {
        String sql = "create schema abc";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") && domain1.getOwner() == null &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_2() {
        String sql = "create schema abc authorization aac";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain2 = (PgSchemaDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_SCHEMA && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("abc") && domain2.getOwner().equals("aac") &&//
                   !domain2.isIfNotExists() && domain2.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_includeElement_1() {
        String sql = "create schema abc create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/abc/test_user/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/abc/test_user/id/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_db/abc/test_user/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 5;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            PgTableDomain domain = (PgTableDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") &&//
                   domain.getOptions().isEmpty() &&//
                   domain.getColumns().size() == 2;
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") && domain.getColumn().equals("id");//

        }
    }

    @Test
    public void createSchema_includeElement_2() {
        String sql = "create schema abc authorization aac create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/abc/test_user/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/abc/test_user/id/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_db/abc/test_user/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 5;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            PgTableDomain domain = (PgTableDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") &&//
                   domain.getOptions().isEmpty() &&//
                   domain.getColumns().size() == 2;
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") && domain.getColumn().equals("id");//

        }
    }

    @Test
    public void createSchema_includeElement_3() {
        String sql = "create schema authorization aac create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/aac/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/aac/test_user/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/aac/test_user/id/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_db/aac/test_user/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 5;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("aac") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            PgTableDomain domain = (PgTableDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("aac") && domain.getTable().equals("test_user") &&//
                   domain.getOptions().isEmpty() &&//
                   domain.getColumns().size() == 2;
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("aac") && domain.getTable().equals("test_user") && domain.getColumn().equals("id");//

        }
    }

    @Test
    public void createSchema_ifNotExists_1_1() {
        String sql1 = "create schema authorization aac create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = parserRes(sql1);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/aac/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/aac/test_user/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/aac/test_user/id/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_db/aac/test_user/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        String sql2 = "create schema if not exists abc";
        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql2), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") && domain1.getOwner() == null &&//
                   domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_ifNotExists_1_2() {
        String sql = "create schema if not exists abc authorization aac";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain2 = (PgSchemaDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_SCHEMA && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("abc") && domain2.getOwner().equalsIgnoreCase("aac") &&//
                   domain2.isIfNotExists() && domain2.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_ifNotExists_1_3() {
        String sql = "create schema if not exists abc authorization current_user";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain3 = (PgSchemaDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_SCHEMA && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("abc") && domain3.getOwner().equals("current_user") &&//
                   domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_ifNotExists_1_4() {
        String sql = "create schema if not exists abc authorization current_role";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain4 = (PgSchemaDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.CREATE_SCHEMA && domain4.getAuditKind() == SecQueryKind.CREATE;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("abc") && domain4.getOwner().equals("current_role") &&//
                   domain4.isIfNotExists() && domain4.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_ifNotExists_1_5() {
        String sql = "create schema if not exists abc authorization session_user";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain5 = (PgSchemaDomain) domainList.get(0);
            assert domain5.getSqlType() == SecQueryType.CREATE_SCHEMA && domain5.getAuditKind() == SecQueryKind.CREATE;
            assert domain5.getCatalog() == null && domain5.getSchema().equals("abc") && domain5.getOwner().equals("session_user") &&//
                   domain5.isIfNotExists() && domain5.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_ifNotExists_2_1() {
        String sql = "create schema if not exists authorization aac";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/aac/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain2 = (PgSchemaDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_SCHEMA && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("aac") && domain2.getOwner().equalsIgnoreCase("aac") &&//
                   domain2.isIfNotExists() && domain2.getOptions().isEmpty();
        }
    }

    //@Test  // TODO 暂不支持因为 current_user 表示的是一个动态值
    public void createSchema_ifNotExists_2_2() {
        String sql = "create schema if not exists authorization current_user";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/current_user/");
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain3 = (PgSchemaDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_SCHEMA && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("current_user") && domain3.getOwner().equals("current_user") &&//
                   domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
    }

    //@Test  // TODO 暂不支持因为 current_role 表示的是一个动态值
    public void createSchema_ifNotExists_2_3() {
        String sql = "create schema if not exists authorization current_role";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/current_role/");
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain4 = (PgSchemaDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.CREATE_SCHEMA && domain4.getAuditKind() == SecQueryKind.CREATE;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("current_role") && domain4.getOwner().equals("current_role") &&//
                   domain4.isIfNotExists() && domain4.getOptions().isEmpty();
        }
    }

    //@Test  // TODO 暂不支持因为 session_user 表示的是一个动态值
    public void createSchema_ifNotExists_2_4() {
        String sql = "create schema if not exists authorization session_user";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/session_user/");
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain5 = (PgSchemaDomain) domainList.get(0);
            assert domain5.getSqlType() == SecQueryType.CREATE_SCHEMA && domain5.getAuditKind() == SecQueryKind.CREATE;
            assert domain5.getCatalog() == null && domain5.getSchema().equals("session_user") && domain5.getOwner().equals("session_user") &&//
                   domain5.isIfNotExists() && domain5.getOptions().isEmpty();
        }
    }

    @Test
    public void renameSchema_1() {
        String sql = "alter schema abc rename to aac";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.RENAME_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.RENAME_SCHEMA && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") && domain1.getNewName().equals("aac");
        }
    }

    @Test
    public void renameSchema_2() {
        String sql = "alter schema test.abc rename to aac";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.RENAME_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.RENAME_SCHEMA && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("abc") && domain1.getNewName().equals("aac");
        }
    }

    @Test
    public void alterSchema_1() {
        String sql = "alter schema abc owner to aac";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_SCHEMA && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") && domain1.getOwner().equals("aac") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void alterSchema_2() {
        String sql = "alter schema abc owner to current_role";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain2 = (PgSchemaDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_SCHEMA && domain2.getAuditKind() == SecQueryKind.ALTER;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("abc") && domain2.getOwner().equals("current_role") &&//
                   !domain2.isIfNotExists() && domain2.getOptions().isEmpty();
        }
    }

    @Test
    public void alterSchema_3() {
        String sql = "alter schema abc owner to current_user";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain3 = (PgSchemaDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.ALTER_SCHEMA && domain3.getAuditKind() == SecQueryKind.ALTER;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("abc") && domain3.getOwner().equals("current_user") &&//
                   !domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
    }

    @Test
    public void alterSchema_4() {
        String sql = "alter schema abc owner to session_user";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain4 = (PgSchemaDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.ALTER_SCHEMA && domain4.getAuditKind() == SecQueryKind.ALTER;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("abc") && domain4.getOwner().equals("session_user") &&//
                   !domain4.isIfNotExists() && domain4.getOptions().isEmpty();
        }
    }

    @Test
    public void dropSchema_1() {
        String sql = "drop schema abc";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain1 = (PgSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") &&//
                   !domain1.isIfExists() && !domain1.isCascade() && !domain1.isRestrict();
        }
    }

    @Test
    public void dropSchema_2() {
        String sql = "drop schema abc cascade";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain2 = (PgSchemaDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.DROP_SCHEMA && domain2.getAuditKind() == SecQueryKind.DROP;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("abc") &&//
                   !domain2.isIfExists() && domain2.isCascade() && !domain2.isRestrict();
        }
    }

    @Test
    public void dropSchema_3() {
        String sql = "drop schema abc restrict";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain3 = (PgSchemaDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.DROP_SCHEMA && domain3.getAuditKind() == SecQueryKind.DROP;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("abc") &&//
                   !domain3.isIfExists() && !domain3.isCascade() && domain3.isRestrict();
        }
    }

    @Test
    public void dropSchema_4() {
        String sql = "drop schema if exists abc";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgSchemaDomain domain4 = (PgSchemaDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.DROP_SCHEMA && domain4.getAuditKind() == SecQueryKind.DROP;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("abc") &&//
                   domain4.isIfExists() && !domain4.isCascade() && !domain4.isRestrict();
        }
    }

    @Test
    public void dropSchema_5() {
        String sql = "drop schema if exists abc cascade";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            PgSchemaDomain domain5 = (PgSchemaDomain) domainList.get(0);
            assert domain5.getSqlType() == SecQueryType.DROP_SCHEMA && domain5.getAuditKind() == SecQueryKind.DROP;
            assert domain5.getCatalog() == null && domain5.getSchema().equals("abc") &&//
                   domain5.isIfExists() && domain5.isCascade() && !domain5.isRestrict();
        }
    }

    @Test
    public void dropSchema_6() {
        String sql = "drop schema if exists abc restrict";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            PgSchemaDomain domain6 = (PgSchemaDomain) domainList.get(0);
            assert domain6.getSqlType() == SecQueryType.DROP_SCHEMA && domain6.getAuditKind() == SecQueryKind.DROP;
            assert domain6.getCatalog() == null && domain6.getSchema().equals("abc") &&//
                   domain6.isIfExists() && !domain6.isCascade() && domain6.isRestrict();
        }
    }

    @Test
    public void dropSchema_7() {
        String sql = "drop schema abc.public;";

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
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/public/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 1;
        {
            PgSchemaDomain domain6 = (PgSchemaDomain) domainList.get(0);
            assert domain6.getSqlType() == SecQueryType.DROP_SCHEMA && domain6.getAuditKind() == SecQueryKind.DROP;
            assert domain6.getCatalog().equals("abc") && domain6.getSchema().equals("public") &&//
                   !domain6.isIfExists() && !domain6.isCascade() && !domain6.isRestrict();
        }
    }

    @Test
    public void dropSchema_multiple_1() {
        String sql = "drop schema abc,aac";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
            assert resList.get(1).getType() == TargetType.Schema &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/aac/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 2;
        {
            PgSchemaDomain domain1_1 = (PgSchemaDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_1.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_1.getCatalog() == null && domain1_1.getSchema().equals("abc") &&//
                   !domain1_1.isIfExists() && !domain1_1.isCascade() && !domain1_1.isRestrict();
        }
        {
            PgSchemaDomain domain1_2 = (PgSchemaDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_2.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_2.getCatalog() == null && domain1_2.getSchema().equals("aac") &&//
                   !domain1_2.isIfExists() && !domain1_2.isCascade() && !domain1_2.isRestrict();
        }
    }

    @Test
    public void dropSchema_multiple_2() {
        String sql = "drop schema abc,aac cascade";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
            assert resList.get(1).getType() == TargetType.Schema &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/aac/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 2;
        {
            PgSchemaDomain domain1_1 = (PgSchemaDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_1.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_1.getCatalog() == null && domain1_1.getSchema().equals("abc") &&//
                   !domain1_1.isIfExists() && domain1_1.isCascade() && !domain1_1.isRestrict();
        }
        {
            PgSchemaDomain domain1_2 = (PgSchemaDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_2.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_2.getCatalog() == null && domain1_2.getSchema().equals("aac") &&//
                   !domain1_2.isIfExists() && domain1_2.isCascade() && !domain1_2.isRestrict();
        }
    }

    @Test
    public void dropSchema_multiple_3() {
        String sql = "drop schema abc,aac restrict";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
            assert resList.get(1).getType() == TargetType.Schema &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/aac/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 2;
        {
            PgSchemaDomain domain1_1 = (PgSchemaDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_1.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_1.getCatalog() == null && domain1_1.getSchema().equals("abc") &&//
                   !domain1_1.isIfExists() && !domain1_1.isCascade() && domain1_1.isRestrict();
        }
        {
            PgSchemaDomain domain1_2 = (PgSchemaDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_2.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_2.getCatalog() == null && domain1_2.getSchema().equals("aac") &&//
                   !domain1_2.isIfExists() && !domain1_2.isCascade() && domain1_2.isRestrict();
        }
    }

    @Test
    public void dropSchema_multiple_4() {
        String sql = "drop schema if exists abc,aac";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
            assert resList.get(1).getType() == TargetType.Schema &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/aac/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 2;
        {
            PgSchemaDomain domain1_1 = (PgSchemaDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_1.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_1.getCatalog() == null && domain1_1.getSchema().equals("abc") &&//
                   domain1_1.isIfExists() && !domain1_1.isCascade() && !domain1_1.isRestrict();
        }
        {
            PgSchemaDomain domain1_2 = (PgSchemaDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_2.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_2.getCatalog() == null && domain1_2.getSchema().equals("aac") &&//
                   domain1_2.isIfExists() && !domain1_2.isCascade() && !domain1_2.isRestrict();
        }
    }

    @Test
    public void dropSchema_multiple_5() {
        String sql = "drop schema if exists abc,aac cascade";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
            assert resList.get(1).getType() == TargetType.Schema &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/aac/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 2;
        {
            PgSchemaDomain domain1_1 = (PgSchemaDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_1.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_1.getCatalog() == null && domain1_1.getSchema().equals("abc") &&//
                   domain1_1.isIfExists() && domain1_1.isCascade() && !domain1_1.isRestrict();
        }
        {
            PgSchemaDomain domain1_2 = (PgSchemaDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_2.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_2.getCatalog() == null && domain1_2.getSchema().equals("aac") &&//
                   domain1_2.isIfExists() && domain1_2.isCascade() && !domain1_2.isRestrict();
        }
    }

    @Test
    public void dropSchema_multiple_6() {
        String sql = "drop schema if exists abc,aac restrict";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/abc/");
            assert resList.get(1).getType() == TargetType.Schema &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/aac/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 2;
        {
            PgSchemaDomain domain1_1 = (PgSchemaDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_1.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_1.getCatalog() == null && domain1_1.getSchema().equals("abc") &&//
                   domain1_1.isIfExists() && !domain1_1.isCascade() && domain1_1.isRestrict();
        }
        {
            PgSchemaDomain domain1_2 = (PgSchemaDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_2.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_2.getCatalog() == null && domain1_2.getSchema().equals("aac") &&//
                   domain1_2.isIfExists() && !domain1_2.isCascade() && domain1_2.isRestrict();
        }
    }

    @Test
    public void dropSchema_multiple_7() {
        String sql = "drop schema pub.abc, test.aac, ccc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/pub/abc/");
            assert resList.get(1).getType() == TargetType.Schema &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/aac/");
            assert resList.get(2).getType() == TargetType.Schema &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/ccc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_SCHEMA;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert resList.size() == 3;
        {
            PgSchemaDomain domain1_1 = (PgSchemaDomain) domainList.get(0);
            assert domain1_1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_1.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_1.getCatalog().equals("pub") && domain1_1.getSchema().equals("abc") &&//
                   !domain1_1.isIfExists() && !domain1_1.isCascade() && !domain1_1.isRestrict();
        }
        {
            PgSchemaDomain domain1_2 = (PgSchemaDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_2.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_2.getCatalog().equals("test") && domain1_2.getSchema().equals("aac") &&//
                   !domain1_2.isIfExists() && !domain1_2.isCascade() && !domain1_2.isRestrict();
        }
        {
            PgSchemaDomain domain1_3 = (PgSchemaDomain) domainList.get(2);
            assert domain1_3.getSqlType() == SecQueryType.DROP_SCHEMA && domain1_3.getAuditKind() == SecQueryKind.DROP &&//
                   domain1_3.getCatalog() == null && domain1_3.getSchema().equals("ccc") &&//
                   !domain1_3.isIfExists() && !domain1_3.isCascade() && !domain1_3.isRestrict();
        }
    }
}
