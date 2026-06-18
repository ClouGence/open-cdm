package com.clougence.clouddm.ds.secdomain.family.oracle;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraResAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSecDomainResolveSpi;
import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraColumnDomain;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraSelectDomain;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class OraSecDomainResolve4TableTest extends OraSecDomainTestSupport {

    public OraSecDomainResolve4TableTest(){
        this.analysisSpi = new OraResAnalysisSpi(null);
        this.resolveSpi = new OraSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Oracle;
    }

    @Test
    public void createTable_default_1() {
        String sql = "create table public.abc(id int default 1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/public/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/public/abc/id/");
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
            OraTableDomain domain3 = (OraTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("public") && domain3.getTable().equals("abc") &&//
                   domain3.getOptions().isEmpty();
        }
        {
            OraColumnDomain domain = (OraColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("public") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getDefaultValue().equals("1");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_default_2() {
        String sql = "create table public.abc(id int default test(1,2));";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/public/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/public/abc/id/");
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
            OraTableDomain domain3 = (OraTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("public") && domain3.getTable().equals("abc") &&//
                   domain3.getOptions().isEmpty();
        }
        {
            OraColumnDomain domain = (OraColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("public") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getDefaultValue().equals("test(1,2)");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_1() {
        String sql = "create table abc(id int)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/abc/id/");
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
            OraTableDomain domain = (OraTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isTemporary();
            assert domain.getComment() == null;
        }
        {
            OraColumnDomain domain = (OraColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableAutoColumn_1() {
        String sql = "create table abc(id int GENERATED ALWAYS AS IDENTITY)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/abc/id/");
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
            OraTableDomain domain = (OraTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isTemporary();
            assert domain.getComment() == null;
        }
        {
            OraColumnDomain domain = (OraColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int");
            assert domain.getComment() == null && domain.isAuto();
        }
    }

    @Test
    public void createTableCustomType_1() {
        String sql = "create table abc(id mytype)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/abc/id/");
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
            OraTableDomain domain = (OraTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isTemporary();
            assert domain.getComment() == null;
        }
        {
            OraColumnDomain domain = (OraColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("mytype") && domain.getTypeName().equals("mytype");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableTemporary() {
        // todo
        String sql = "create global temporary table test.abc(id int)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
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
            OraTableDomain domain = (OraTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert domain.isTemporary();
        }
        {
            OraColumnDomain domain = (OraColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTablePrimaryKey_1() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasPrimary();
        }
        {
            OraColumnDomain domain1_2 = (OraColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("id");
            assert !domain1_2.isPrimary();
        }
        {
            OraColumnDomain domain1_2 = (OraColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("name");
            assert !domain1_2.isPrimary();
        }
    }

    @Test
    public void createTablePrimaryKey_2() {
        String sql = "create table test.abc(id int(4) primary key, name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain2_1 = (OraTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.CREATE_TABLE && domain2_1.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema().equals("test") && domain2_1.getTable().equals("abc");
            assert domain2_1.isHasPrimary();
        }
        {
            OraColumnDomain domain2_2 = (OraColumnDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") &&//
                   domain2_2.getColumn().equals("id");
            assert domain2_2.isPrimary();
        }
        {
            RdbConstraintDomain domain3_3 = (RdbConstraintDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id");
            assert domain3_3.getType() == SqlConstraintType.Primary;

        }
        {
            OraColumnDomain domain2_2 = (OraColumnDomain) domainList.get(2);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") &&//
                   domain2_2.getColumn().equals("name");
            assert !domain2_2.isPrimary();
        }
    }

    @Test
    public void createTablePrimaryKey_3() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null,primary key (id))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain3 = (OraTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasPrimary();
        }
        {
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("id");
            assert domain3_2.isPrimary();
        }
        {
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(2);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("name");
            assert !domain3_2.isPrimary();
        }
        {
            RdbConstraintDomain domain3_3 = (RdbConstraintDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id");
            assert domain3_3.getType() == SqlConstraintType.Primary;
        }
    }

    @Test
    public void createTablePrimaryKey_4() {
        String sql = "create table test.abc(id int(4)  constraint kk primary key , name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain3 = (OraTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasPrimary();
        }
        {
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("id");
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
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(2);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("name");
            assert !domain3_2.isPrimary();
        }
    }

    @Test
    public void createTableUniqueKey_1() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasUnique();
        }
        {
            OraColumnDomain domain1_2 = (OraColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("id");
            assert !domain1_2.isUnique();
        }
        {
            OraColumnDomain domain1_2 = (OraColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("name");
            assert !domain1_2.isUnique();
        }
    }

    @Test
    public void createTableUniqueKey_2() {
        String sql = "create table test.abc(id int(4) unique, name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain2_1 = (OraTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.CREATE_TABLE && domain2_1.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema().equals("test") && domain2_1.getTable().equals("abc");
            assert domain2_1.isHasUnique();
        }
        {
            OraColumnDomain domain2_2 = (OraColumnDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") &&//
                   domain2_2.getColumn().equals("id");
            assert domain2_2.isUnique();
        }
        {
            OraColumnDomain domain2_2 = (OraColumnDomain) domainList.get(2);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") &&//
                   domain2_2.getColumn().equals("name");
            assert !domain2_2.isUnique();
        }
    }

    @Test
    public void createTableUniqueKey_3() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null, constraint testkey unique (id))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain3 = (OraTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasUnique();
        }
        {
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("id");
            assert domain3_2.isUnique();
        }
        {
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(2);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("name");
            assert !domain3_2.isUnique();
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
        String sql = "create table test.abc(id int(4), name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasForeignKey();
        }
        {
            OraColumnDomain domain1_2 = (OraColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("id");
            assert !domain1_2.isForeign();
        }
        {
            OraColumnDomain domain1_2 = (OraColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("name");
            assert !domain1_2.isForeign();
        }
    }

    @Test
    public void createTableForeignKey_2() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null,constraint fg foreign key (id) references abc2(id2))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain3 = (OraTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasForeignKey();
        }
        {
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("id");
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
    public void createTableForeignKey_3() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null constraint ptr  references test.abc2)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain3 = (OraTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasForeignKey();
        }
        {
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("id");
            assert !domain3_2.isForeign();
        }
        {
            OraColumnDomain domain3_2 = (OraColumnDomain) domainList.get(2);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("name");
            assert domain3_2.isForeign();
        }
        {
            RdbConstraintDomain domain3_3 = (RdbConstraintDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("name");
            assert domain3_3.getType() == SqlConstraintType.ForeignKey;
        }
    }

    @Test
    public void createTableIndex_1() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasIndex();
        }
        {
            OraColumnDomain domain1_2 = (OraColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("id");
            assert !domain1_2.isIndex();
        }
    }

    @Test
    public void createTableColumns_1() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/abc/name/");
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
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("name");
        }
    }

    @Test
    public void createTableSelect_1() {
        String sql = "create table test.abc_select as select * from test.abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc_select/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc/");
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
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_SELECT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc_select");
        }
        {
            OraSelectDomain domain1_2 = (OraSelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.CREATE;
            assert domain1_2.getSelectColumns().size() == 0 &&//
                   domain1_2.getSelectVariables().size() == 0 &&//
                   domain1_2.getSelectFunc().size() == 0 &&//
                   domain1_2.getWhereColumns().size() == 0;
            assert !domain1_2.isHasAs() &&         //
                   domain1_2.isHasSelectAll() &&   //
                   !domain1_2.isSelectInSelect() &&//
                   !domain1_2.isFuncInSelect() &&  //
                   !domain1_2.isSelectInFrom() &&  //
                   !domain1_2.isEmptyFrom();
            assert !domain1_2.isHasWhere() &&      //
                   !domain1_2.isSelectInWhere() && //
                   !domain1_2.isHasUnion() &&      //
                   !domain1_2.isHasWith();
        }
    }

    @Test
    public void commentTable_1() {
        String sql = "comment on table test.abc is 'ccc'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void commentTable_2() {
        String sql = "comment on table abc is 'ccc'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void commentTable_3() {
        String sql = "comment on table \"test\".\"abc\" is 'ccc'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void commentTable_4() {
        String sql = "comment on table \"abc\" is 'ccc'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableRename_1() {
        String sql = "rename abc to cba";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/cba/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.RENAME_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_RENAME && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc") && domain1.getNewName().equals("cba");
        }
    }

    @Test
    public void alterTableRenameColumn_1() {
        String sql = "ALTER TABLE JUNYU_ORCL.T_WBZLB RENAME COLUMN JLLX TO RECORD_TYPE;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Column &&//
                   resList.get(0).toDsResPath().getResPath().equals("/JUNYU_ORCL/T_WBZLB/JLLX/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraColumnDomain domain1 = (OraColumnDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_RENAME_COLUMN && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("JUNYU_ORCL") &&//
                   domain1.getTable().equals("T_WBZLB") && domain1.getNewName().equals("RECORD_TYPE") && domain1.getColumn().equals("JLLX");
        }
    }

    @Test
    public void dropTable_1() {
        String sql = "drop table abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void dropTableIfExists_2() {
        String sql = "drop table abc cascade constraints";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void truncate() {
        String sql = "truncate table abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.TRUNCATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            OraTableDomain domain1 = (OraTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.TRUNCATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }
}
