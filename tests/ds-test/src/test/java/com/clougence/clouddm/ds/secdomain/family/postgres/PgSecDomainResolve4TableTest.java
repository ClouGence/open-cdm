package com.clougence.clouddm.ds.secdomain.family.postgres;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgResAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgSecDomainResolve4TableTest extends PgSecDomainTestSupport {

    public PgSecDomainResolve4TableTest(){
        this.analysisSpi = new PgResAnalysisSpi(null);
        this.resolveSpi = new PgSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PgSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PostgreSQL;
    }

    @Test
    public void createTable_basic_1() {
        String sql = "create table abc(id int)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_basic_2() {
        String sql = "create table if not exists abc(id int)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain2 = (PgTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("abc") &&//
                   domain2.isIfNotExists() && domain2.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_basic_3() {
        String sql = "create table schema1.abc(id int)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/schema1/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/schema1/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("schema1") && domain1.getTable().equals("abc") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("schema1") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_basic_4() {
        String sql = "create table postgres.abc(id int);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/postgres/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/postgres/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain2 = (PgTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("postgres") && domain2.getTable().equals("abc") &&//
                   !domain2.isIfNotExists() && domain2.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("postgres") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_basic_5() {
        String sql = "create table abc.public.abc(id int);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/public/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/abc/public/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog().equals("abc") && domain3.getSchema().equals("public") && domain3.getTable().equals("abc") &&//
                   !domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("abc") && domain.getSchema().equals("public") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_basic_6() {
        String sql = "create table abc.public.abc(id int[]);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/public/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/abc/public/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog().equals("abc") && domain3.getSchema().equals("public") && domain3.getTable().equals("abc") &&//
                   !domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("abc") && domain.getSchema().equals("public") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int[]") && domain.getTypeName().equals("int") && domain.isArray();
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_basic_7() {
        String sql = "create table abc(column1 testType.tt, column2 varchar(25),column3 bit,column4 json,column5 interval year,column6 TIMESTAMP);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 7;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column1/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column2/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column3/");
            assert resList.get(4).getType() == TargetType.Column &&//
                   resList.get(4).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column4/");
            assert resList.get(5).getType() == TargetType.Column &&//
                   resList.get(5).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column5/");
            assert resList.get(6).getType() == TargetType.Column &&//
                   resList.get(6).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column6/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 7;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("abc") &&//
                   !domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("column1") && domain.getTypeDesc().equals("testType.tt") && domain.getTypeName().equals("testType") && !domain.isArray();
            assert domain.getComment() == null;
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("column2") && domain.getTypeDesc().equals("varchar(25)") && domain.getTypeName().equals("varchar") && !domain.isArray();
            assert domain.getComment() == null;
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(3);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("column3") && domain.getTypeDesc().equals("bit") && domain.getTypeName().equals("bit") && !domain.isArray();
            assert domain.getComment() == null;
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(4);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("column4") && domain.getTypeDesc().equals("json") && domain.getTypeName().equals("json") && !domain.isArray();
            assert domain.getComment() == null;
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(5);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("column5") && domain.getTypeDesc().equals("interval year") && domain.getTypeName().equals("interval") && !domain.isArray();
            assert domain.getComment() == null;
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(6);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("column6") && domain.getTypeDesc().equals("TIMESTAMP") && domain.getTypeName().equals("TIMESTAMP") && !domain.isArray();
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_default_1() {
        String sql = "create table abc.public.abc(id int default 1);";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/public/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/abc/public/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog().equals("abc") && domain3.getSchema().equals("public") && domain3.getTable().equals("abc") &&//
                   !domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("abc") && domain.getSchema().equals("public") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int") && domain.getDefaultValue().equals("1");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_default_2() {
        String sql = "create table abc.public.abc(id int default test(1,2));";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/abc/public/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/abc/public/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog().equals("abc") && domain3.getSchema().equals("public") && domain3.getTable().equals("abc") &&//
                   !domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("abc") && domain.getSchema().equals("public") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int") && domain.getDefaultValue().equals("test(1,2)");
            assert domain.getComment() == null;
        }
    }

    //@Test
    public void createTable_unlogged_1() {
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "create unlogged table abc(id int)");
        //        PgTableDomain domain1 = (PgTableDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getSqlKind() == SqlKind.CREATE;
        //        assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
        //               !domain1.isIfNotExists() && domain1.isUnlogged() && domain1.getOptions().isEmpty();
    }

    //@Test
    public void createTable_unlogged_2() {
        //        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "create unlogged table abc(id int)");
        //        PgTableDomain domain2 = (PgTableDomain) list2.get(0);
        //        assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getSqlKind() == SqlKind.CREATE;
        //        assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("abc") &&//
        //               domain2.isIfNotExists() && domain2.isUnlogged() && domain2.getOptions().isEmpty();
    }

    //@Test
    public void createTable_temp_1_1() {
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "create global temp table abc(id int)");
        //        PgTableDomain domain1 = (PgTableDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getSqlKind() == SqlKind.CREATE;
        //        assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
        //               !domain1.isIfNotExists() && domain1.isTemporary() && domain1.getOptions().isEmpty();
    }

    //@Test
    public void createTable_temp_1_2() {
        //        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "create global temporary table abc(id int)");
        //        PgTableDomain domain2 = (PgTableDomain) list2.get(0);
        //        assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getSqlKind() == SqlKind.CREATE;
        //        assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("abc") &&//
        //                !domain2.isIfNotExists() && domain2.isTemporary() && domain2.getOptions().isEmpty();
    }

    //@Test
    public void createTable_temp_1_3() {
        //        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "create local temp table abc(id int)");
        //        PgTableDomain domain3 = (PgTableDomain) list3.get(0);
        //        assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getSqlKind() == SqlKind.CREATE;
        //        assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("abc") &&//
        //                !domain3.isIfNotExists() && domain3.isTemporary() && domain3.getOptions().isEmpty();
    }

    //@Test
    public void createTable_temp_1_4() {
        //        List<RuleDomain> list4 = resolveSpi.resolveDomain(dataSourceType, "create local temporary table abc(id int)");
        //        PgTableDomain domain4 = (PgTableDomain) list4.get(0);
        //        assert domain4.getSqlType() == SecQueryType.CREATE_TABLE && domain4.getSqlKind() == SqlKind.CREATE;
        //        assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("abc") &&//
        //               !domain4.isIfNotExists() && domain4.isTemporary() && domain4.getOptions().isEmpty();
    }

    //@Test
    public void createTable_temp_2_1() {
        //        List<RuleDomain> list5 = resolveSpi.resolveDomain(dataSourceType, "create global temp table if not exists abc(id int)");
        //        PgTableDomain domain5 = (PgTableDomain) list5.get(0);
        //        assert domain5.getSqlType() == SecQueryType.CREATE_TABLE && domain5.getSqlKind() == SqlKind.CREATE;
        //        assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("abc") &&//
        //               !domain5.isIfNotExists() && domain5.isTemporary() && domain5.getOptions().isEmpty();
    }

    //@Test
    public void createTable_temp_2_2() {
        //        List<RuleDomain> list6 = resolveSpi.resolveDomain(dataSourceType, "create global temporary table if not exists abc(id int)");
        //        PgTableDomain domain6 = (PgTableDomain) list6.get(0);
        //        assert domain6.getSqlType() == SecQueryType.CREATE_TABLE && domain6.getSqlKind() == SqlKind.CREATE;
        //        assert domain6.getCatalog() == null && domain6.getSchema() == null && domain6.getTable().equals("abc") &&//
        //               !domain6.isIfNotExists() && domain6.isTemporary() && domain6.getOptions().isEmpty();
    }

    //@Test
    public void createTable_temp_2_3() {
        //        List<RuleDomain> list7 = resolveSpi.resolveDomain(dataSourceType, "create local temp table if not exists abc(id int)");
        //        PgTableDomain domain7 = (PgTableDomain) list7.get(0);
        //        assert domain7.getSqlType() == SecQueryType.CREATE_TABLE && domain7.getSqlKind() == SqlKind.CREATE;
        //        assert domain7.getCatalog() == null && domain7.getSchema() == null && domain7.getTable().equals("abc") &&//
        //               !domain7.isIfNotExists() && domain7.isTemporary() && domain7.getOptions().isEmpty();
    }

    //@Test
    public void createTable_temp_2_4() {
        //        List<RuleDomain> list8 = resolveSpi.resolveDomain(dataSourceType, "create local temporary table if not exists abc(id int)");
        //        PgTableDomain domain8 = (PgTableDomain) list8.get(0);
        //        assert domain8.getSqlType() == SecQueryType.CREATE_TABLE && domain8.getSqlKind() == SqlKind.CREATE;
        //        assert domain8.getCatalog() == null && domain8.getSchema() == null && domain8.getTable().equals("abc") &&//
        //               !domain8.isIfNotExists() && domain8.isTemporary() && domain8.getOptions().isEmpty();
    }

    @Test
    public void createTableInherits_1_1() {
        String sql = "create table abc(id int) inherits (users)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   domain1.isInherits() && domain1.getInheritTables().contains("users");
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableInherits_1_2() {
        String sql = "create table abc(id int) inherits (public.users)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain2 = (PgTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("abc") &&//
                   domain2.isInherits() && domain2.getInheritTables().contains("users");
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableInherits_1_3() {
        String sql = "create table abc(id int) inherits (abc.public.users)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("abc") &&//
                   domain3.isInherits() && domain3.getInheritTables().contains("users");
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int");
            assert domain.getComment() == null;
        }
    }

    //@Test
    public void createTableInherits_2_1() {
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "create table abc(id int) inherits (users1,users2)");
        //        PgTableDomain domain1 = (PgTableDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getSqlKind() == SqlKind.CREATE;
        //        assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
        //               domain1.isInherits() && domain1.getInheritTables().contains("users1") && domain1.getInheritTables().contains("users2");
    }

    //@Test
    public void createTableInherits_2_2() {
        //        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "create table abc(id int) inherits (public.users1,public.users2)");
        //        PgTableDomain domain2 = (PgTableDomain) list2.get(0);
        //        assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getSqlKind() == SqlKind.CREATE;
        //        assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("abc") &&//
        //               domain2.isInherits() && domain2.getInheritTables().contains("users1") && domain2.getInheritTables().contains("users2");
    }

    //@Test
    public void createTableInherits_2_3() {
        //        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "create table abc(id int) inherits (abc.public.users1,abc.public.users2)");
        //        PgTableDomain domain3 = (PgTableDomain) list3.get(0);
        //        assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getSqlKind() == SqlKind.CREATE;
        //        assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("abc") &&//
        //               domain3.isInherits() && domain3.getInheritTables().contains("users1") && domain3.getInheritTables().contains("users2");
    }

    //   CREATE TABLE table_name
    //   (
    //     [
    //        {
    //           column_name data_type [ STORAGE { PLAIN | EXTERNAL | EXTENDED | MAIN | DEFAULT } ] [ COMPRESSION compression_method ] [ COLLATE collation ] [ column_constraint [ ... ] ]
    //           | table_constraint
    //           | LIKE source_table [ like_option ... ]
    //        } [, ... ]
    //     ]
    //   )
    //   [ PARTITION BY { RANGE | LIST | HASH } ( { column_name | ( expression ) } [ COLLATE collation ] [ opclass ] [, ... ] ) ]
    //   [ USING method ]
    //   [ WITH ( storage_parameter [= value] [, ... ] ) | WITHOUT OIDS ] [ ON COMMIT { PRESERVE ROWS | DELETE ROWS | DROP } ]
    //   [ TABLESPACE tablespace_name ]

    @Test
    public void createTablePrimaryKey_1() {
        String sql = "create table test.abc(id int, name varchar(25) not null)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasPrimary();
        }
        {
            PgColumnDomain domain1_2 = (PgColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("id");
            assert !domain1_2.isPrimary();
        }
        {
            PgColumnDomain domain1_2 = (PgColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("name");
            assert domain1_2.getTypeDesc().equals("varchar(25)") && domain1_2.getLength().equals("25");
            assert domain1_2.getTypeName().equals("varchar");
            assert !domain1_2.isPrimary();
        }
    }

    @Test
    public void createTablePrimaryKey_2() {
        String sql = "create table test.abc(id int primary key, name varchar(25) not null)";

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
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            PgTableDomain domain2_1 = (PgTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.CREATE_TABLE && domain2_1.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema().equals("test") && domain2_1.getTable().equals("abc");
            assert domain2_1.isHasPrimary();
        }
        {
            PgColumnDomain domain2_2 = (PgColumnDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") && domain2_2.getColumn().equals("id");
            assert domain2_2.isPrimary();
        }
        {
            PgColumnDomain domain1_2 = (PgColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("name");
            assert domain1_2.getTypeDesc().equals("varchar(25)") && domain1_2.getLength().equals("25");
            assert domain1_2.getTypeName().equals("varchar");
            assert !domain1_2.isPrimary();
        }
    }

    @Test
    public void createTablePrimaryKey_3() {
        String sql = "create table test.abc(id int, name varchar(25) not null,primary key (id))";

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
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasPrimary();
        }
        {
            PgColumnDomain domain3_2 = (PgColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") && domain3_2.getColumn().equals("id");
            assert domain3_2.isPrimary();
        }
        {
            RdbConstraintDomain domain3_3 = (RdbConstraintDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id");
            assert domain3_3.getType() == SqlConstraintType.Primary;
        }
        {
            PgColumnDomain domain1_2 = (PgColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("name");
            assert domain1_2.getTypeDesc().equals("varchar(25)") && domain1_2.getLength().equals("25");
            assert domain1_2.getTypeName().equals("varchar");
            assert !domain1_2.isPrimary();
        }
        {
            RdbConstraintDomain domain1_2 = (RdbConstraintDomain) domainList.get(3);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getTableSchema().equals("test") && domain1_2.getTableName().equals("abc");
            assert domain1_2.getColumns().size() == 1 && domain1_2.getColumns().contains("id");
        }
    }

    @Test
    public void createTableUniqueKey_1() {
        String sql = "create table test.abc(id int, name varchar(25) not null)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasUnique();
        }
        {
            PgColumnDomain domain1_2 = (PgColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("id");
            assert !domain1_2.isUnique();
        }
    }

    @Test
    public void createTableUniqueKey_2() {
        String sql = "create table test.abc(id int unique, name varchar(25) not null)";

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
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            PgTableDomain domain2_1 = (PgTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.CREATE_TABLE && domain2_1.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema().equals("test") && domain2_1.getTable().equals("abc");
            assert domain2_1.isHasUnique();
        }
        {
            PgColumnDomain domain2_2 = (PgColumnDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") && domain2_2.getColumn().equals("id");
            assert domain2_2.isUnique();
        }
    }

    @Test
    public void createTableUniqueKey_3() {
        String sql = "create table test.abc(id int, name varchar(25) not null,unique (id))";

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
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasUnique();
        }
        {
            PgColumnDomain domain3_2 = (PgColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") && domain3_2.getColumn().equals("id");
            assert domain3_2.isUnique();
        }
        {
            RdbConstraintDomain domain3_3 = (RdbConstraintDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id");
            assert domain3_3.getType() == SqlConstraintType.Unique;
        }
    }

    @Test
    public void createTableForeignKey_1() {
        String sql = "create table test.abc(id int, name varchar(25) not null)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasForeignKey();
        }
        {
            PgColumnDomain domain1_2 = (PgColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("id");
            assert !domain1_2.isForeign();
        }
    }

    @Test
    public void createTableForeignKey_2() {
        String sql = "create table test.abc(id int, name varchar(25) not null,constraint ptr foreign key (id) references test.abc2(id2))";

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
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasForeignKey();
        }
        {
            PgColumnDomain domain3_2 = (PgColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") && domain3_2.getColumn().equals("id");
            assert domain3_2.isForeign();
        }
        {
            RdbConstraintDomain domain3_3 = (RdbConstraintDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id");
            assert domain3_3.getType() == SqlConstraintType.ForeignKey;
        }
    }

    @Test
    public void createTableColumns_1() {
        String sql = "create table test.abc(id int, name varchar(25) not null)";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 3;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("name");
        }
    }

    //@Test
    public void createTableLike_1() {
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "create table test.abc_copy(like abc.public.abc including defaults);");
        //        PgTableDomain domain1 = (PgTableDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getSqlKind() == SqlKind.CREATE;
        //        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test") && domain1.getTable().equals("abc_copy");
        //        assert domain1.getSourceSchema().equals("test") && domain1.getSourceTable().equals("abc");
    }

    //@Test
    public void createTableLike_2() {
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "create table test.abc_copy(like abc);");
        //        PgTableDomain domain1 = (PgTableDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getSqlKind() == SqlKind.CREATE;
        //        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null && domain1.getTable().equals("abc_copy");
        //        assert domain1.getSourceSchema() == null && domain1.getSourceTable().equals("abc");
    }

    @Test
    public void createTableSelect_1() {
        String sql = "create table test.abc_select as select * from test.abc;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc_select/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_SELECT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc_select");
        }
    }

    @Test
    public void createTableAs_1() {
        String sql = "create table test.abc_select as table test.abc;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc_select/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc_select");
        }
    }

    @Test
    public void alterTableRename_1() {
        String sql = "alter table abc rename to cba;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/cba/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.RENAME_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_RENAME && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   !domain1.isIfExists() &&//
                   domain1.getTable().equals("abc") && domain1.getNewName().equals("cba");
        }
    }

    @Test
    public void alterTableRename_2() {
        String sql = "alter table if exists abc rename to cba;";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/cba/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.RENAME_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            PgTableDomain domain2 = (PgTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_TABLE_RENAME && domain2.getAuditKind() == SecQueryKind.ALTER;
            assert domain2.getCatalog() == null && domain2.getSchema() == null &&//
                   domain2.isIfExists() &&//
                   domain2.getTable().equals("abc") && domain2.getNewName().equals("cba");
        }
    }

    @Test
    public void dropTable_1() {
        String sql = "drop table abc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   !domain1.isIfExists();
        }
    }

    @Test
    public void dropTable_2() {
        String sql = "drop table abc,cba";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/cba/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            PgTableDomain domain2_1 = (PgTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.DROP_TABLE && domain2_1.getAuditKind() == SecQueryKind.DROP;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema() == null && domain2_1.getTable().equals("abc") &&//
                   !domain2_1.isIfExists();
        }
        {
            PgTableDomain domain2_2 = (PgTableDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.DROP_TABLE && domain2_2.getAuditKind() == SecQueryKind.DROP;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema() == null && domain2_2.getTable().equals("cba") &&//
                   !domain2_2.isIfExists();
        }
    }

    @Test
    public void dropTable_3() {
        String sql = "drop table if exists abc";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            PgTableDomain domain3 = (PgTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.DROP_TABLE && domain3.getAuditKind() == SecQueryKind.DROP;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("abc") && //
                   domain3.isIfExists();
        }
    }

    @Test
    public void dropTable_4() {
        String sql = "drop table if exists abc,cba";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/cba/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            PgTableDomain domain4_1 = (PgTableDomain) domainList.get(0);
            assert domain4_1.getSqlType() == SecQueryType.DROP_TABLE && domain4_1.getAuditKind() == SecQueryKind.DROP;
            assert domain4_1.getCatalog() == null && domain4_1.getSchema() == null && domain4_1.getTable().equals("abc") &&//
                   domain4_1.isIfExists();
        }
        {
            PgTableDomain domain4_2 = (PgTableDomain) domainList.get(1);
            assert domain4_2.getSqlType() == SecQueryType.DROP_TABLE && domain4_2.getAuditKind() == SecQueryKind.DROP;
            assert domain4_2.getCatalog() == null && domain4_2.getSchema() == null && domain4_2.getTable().equals("cba") &&//
                   domain4_2.isIfExists();
        }
    }

    @Test
    public void truncateTable_4() {
        String sql = "truncate abc,cba";

        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/cba/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.TRUNCATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            PgTableDomain domain4_1 = (PgTableDomain) domainList.get(0);
            assert domain4_1.getSqlType() == SecQueryType.TRUNCATE && domain4_1.getAuditKind() == SecQueryKind.DML;
            assert domain4_1.getCatalog() == null && domain4_1.getSchema() == null && domain4_1.getTable().equals("abc");
        }
        {
            PgTableDomain domain4_2 = (PgTableDomain) domainList.get(1);
            assert domain4_2.getSqlType() == SecQueryType.TRUNCATE && domain4_2.getAuditKind() == SecQueryKind.DML;
            assert domain4_2.getCatalog() == null && domain4_2.getSchema() == null && domain4_2.getTable().equals("cba");
        }
    }

    //    @Test
    //    public void createTableNumericColumn() {
    //        String sql = "create table if not exists abc(column1 int," +
    //                "column2 integer,column3 BIGINT," +
    //                "column4 REAl,column5 float,column6 float(1)," +
    //                "column7 double,column8 DECIMAL,column9 decimal(2,4),column10 decimal(2),column11 boolean" +
    //                ")";
    //
    //        List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //        assert resList.size() == 12;
    //        {
    //            assert resList.get(0).getType() == TargetType.Table &&//
    //                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
    //            assert resList.get(1).getType() == TargetType.Column &&//
    //                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column1/");
    //            assert resList.get(2).getType() == TargetType.Column &&//
    //                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column2/");
    //            assert resList.get(3).getType() == TargetType.Column &&//
    //                   resList.get(3).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column3/");
    //            assert resList.get(4).getType() == TargetType.Column &&//
    //                   resList.get(4).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column4/");
    //            assert resList.get(5).getType() == TargetType.Column &&//
    //                   resList.get(5).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column5/");
    //            assert resList.get(6).getType() == TargetType.Column &&//
    //                   resList.get(6).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column6/");
    //            assert resList.get(7).getType() == TargetType.Column &&//
    //                   resList.get(7).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column7/");
    //            assert resList.get(8).getType() == TargetType.Column &&//
    //                   resList.get(8).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column8/");
    //            assert resList.get(9).getType() == TargetType.Column &&//
    //                   resList.get(9).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column9/");
    //            assert resList.get(10).getType() == TargetType.Column &&//
    //                   resList.get(10).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column10/");
    //            assert resList.get(11).getType() == TargetType.Column &&//
    //                   resList.get(11).toDsResPath().getResPath().equals("/test_db/test_schema/abc/column11/");
    //        }
    //
    //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
    //        assert splitScripts.size() == 1;
    //        {
    //            assert splitScripts.get(0).getScript().equals(sql);
    //            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
    //        }
    //
    //        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
    //        assert domainList.size() == 12;
    //        {
    //            PgTableDomain domain2 = (PgTableDomain) domainList.get(0);
    //            assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getSqlKind() == SqlKind.CREATE;
    //            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("abc") &&//
    //                   domain2.isIfNotExists() && domain2.getOptions().isEmpty();
    //        }
    //        {
    //            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
    //            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getSqlKind() == SqlKind.CREATE;
    //            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
    //                   domain.getColumn().equals("column1") && domain.getTypeDesc().equals("int");
    //            assert domain.getComment() == null;
    //        }
    //    }
}
