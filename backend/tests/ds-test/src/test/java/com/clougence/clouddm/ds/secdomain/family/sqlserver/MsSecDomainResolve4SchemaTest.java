package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsColumnDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsSchemaDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsTableDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4SchemaTest extends MsSecDomainTestSupport {

    public MsSecDomainResolve4SchemaTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void createSchema_1() {
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

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsSchemaDomain domain1 = (MsSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") && domain1.getOwner() == null &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_2() {
        String sql = "create schema abc authorization aac";

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

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsSchemaDomain domain2 = (MsSchemaDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_SCHEMA && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("abc") && domain2.getOwner().equals("aac") &&//
                   !domain2.isIfNotExists() && domain2.getOptions().isEmpty();
        }
    }

    @Test
    public void createSchema_includeElement_1() {
        String sql = "create schema abc create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 4;
        {
            MsSchemaDomain domain1 = (MsSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            MsTableDomain domain = (MsTableDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") &&//
                   domain.getOptions().isEmpty() &&//
                   domain.getColumns().size() == 2;
        }
        {
            MsColumnDomain domain = (MsColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") && domain.getColumn().equals("id");//

        }
    }

    @Test
    public void createSchema_includeElement_2() {
        String sql = "create schema abc authorization aac create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 4;
        {
            MsSchemaDomain domain1 = (MsSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            MsTableDomain domain = (MsTableDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") &&//
                   domain.getOptions().isEmpty() &&//
                   domain.getColumns().size() == 2;
        }
        {
            MsColumnDomain domain = (MsColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("abc") && domain.getTable().equals("test_user") && domain.getColumn().equals("id");//

        }
    }

    @Test
    public void createSchema_includeElement_3() {
        String sql = "create schema authorization aac create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 4;
        {
            MsSchemaDomain domain1 = (MsSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("aac") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            MsTableDomain domain = (MsTableDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("aac") && domain.getTable().equals("test_user") &&//
                   domain.getOptions().isEmpty() &&//
                   domain.getColumns().size() == 2;
        }
        {
            MsColumnDomain domain = (MsColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("aac") && domain.getTable().equals("test_user") && domain.getColumn().equals("id");//

        }
    }

    @Test
    public void createSchema_Authorization_1_1() {
        String sql = "create schema authorization aac create table test_user (id int primary key,name varchar(50));";

        List<ResObject> resList = parserRes(sql);
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
        {
            MsSchemaDomain domain1 = (MsSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_SCHEMA && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getOwner().equals("aac") && domain1.getSchema().equals("aac");
            assert !domain1.isIfNotExists() && !domain1.isIfExists() && domain1.getOptions().isEmpty();
        }

        {
            MsTableDomain domain = (MsTableDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("aac");
            assert !domain.isIfNotExists() && !domain.isIfExists() && domain.getOptions().isEmpty();
            assert domain.getTable().equals("test_user");
            assert domain.getColumns().size() == 2 && domain.getColumns().get(0).equals("id") && domain.getColumns().get(1).equals("name");
            assert domain.isHasPrimary();
        }
        {
            MsColumnDomain domain = (MsColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("aac");
            assert domain.getTable().equals("test_user");
            assert domain.getColumn().equals("id");
            assert domain.getTypeName().equals("int");
        }
        {
            MsColumnDomain domain = (MsColumnDomain) domainList.get(3);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("aac");
            assert domain.getTable().equals("test_user");
            assert domain.getColumn().equals("name");
            assert domain.getTypeName().equals("varchar");
        }
    }

    @Test
    public void dropSchema_1() {
        String sql = "drop schema abc";

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

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsSchemaDomain domain1 = (MsSchemaDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_SCHEMA && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("abc") &&//
                   !domain1.isIfExists() && !domain1.isCascade() && !domain1.isRestrict();
        }
    }

    @Test
    public void dropSchema_2() {
        String sql = "drop schema abc";

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

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsSchemaDomain domain2 = (MsSchemaDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.DROP_SCHEMA && domain2.getAuditKind() == SecQueryKind.DROP;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("abc") &&//
                   !domain2.isIfExists() && !domain2.isCascade() && !domain2.isRestrict();
        }
    }

    @Test
    public void dropSchema_3() {
        String sql = "drop schema abc";

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

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsSchemaDomain domain3 = (MsSchemaDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.DROP_SCHEMA && domain3.getAuditKind() == SecQueryKind.DROP;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("abc") &&//
                   !domain3.isIfExists() && !domain3.isCascade() && !domain3.isRestrict();
        }
    }

    @Test
    public void dropSchema_4() {
        String sql = "drop schema if exists abc";

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

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MsSchemaDomain domain4 = (MsSchemaDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.DROP_SCHEMA && domain4.getAuditKind() == SecQueryKind.DROP;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("abc") &&//
                   domain4.isIfExists() && !domain4.isCascade() && !domain4.isRestrict();
        }
    }

    @Test
    public void dropSchema_7() {
        String sql = "drop schema [public];";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/public/");
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
            MsSchemaDomain domain6 = (MsSchemaDomain) domainList.get(0);
            assert domain6.getSqlType() == SecQueryType.DROP_SCHEMA && domain6.getAuditKind() == SecQueryKind.DROP;
            assert domain6.getCatalog() == null && domain6.getSchema().equals("public") &&//
                   !domain6.isIfExists() && !domain6.isCascade() && !domain6.isRestrict();
        }
    }

}
