package com.clougence.clouddm.ds.secdomain.family.mc.notsupportschema;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.maxcompute.analysis.McResAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainResolveSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSplitAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McColumnDomain;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McSelectDomain;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class McSecDomainResolve4TableTest extends McSecDomainTestSupport {

    public McSecDomainResolve4TableTest(){
        this.analysisSpi = new McResAnalysisSpi(null);
        this.resolveSpi = new McSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new McSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MaxCompute;
    }

    @Test
    public void createTable_1() {
        String sql = "create table abc(id int)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/abc/id/");
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
            McTableDomain domain = (McTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists();
            assert !domain.isTemporary();
            assert domain.getComment() == null;
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTable_2() {
        String sql = "create table `abc`(`id` int) COMMENT 'desc'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/abc/id/");
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
            McTableDomain domain = (McTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists();
            assert !domain.isTemporary();
            assert domain.getComment().equalsIgnoreCase("desc");
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableIfNotExists_1() {
        String sql = "create table if not exists a.abc(id int) PARTITIONED BY (sale_date STRING, region STRING)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/a/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/a/default/abc/id/");
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
            McTableDomain domain = (McTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("a") && domain.getSchema().equals("default") && domain.getTable().equals("abc") &&//
                   domain.getOptions().isEmpty();
            assert domain.isIfNotExists();
            assert !domain.isTemporary();
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("a") && domain.getSchema().equals("default") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableIfNotExists_2() {
        String sql = "create table if not exists abc(id int)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/abc/id/");
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
            McTableDomain domain = (McTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("abc") &&//
                   domain.getOptions().isEmpty();
            assert domain.isIfNotExists();
            assert !domain.isTemporary();
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableOptions_1() {
        String sql = "create table test.abc(id int, name varchar(25) not null) \n" + "TBLPROPERTIES(\"ingestion_time_partition\"='true','write.bucket.num'='4');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/default/abc/name/");
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
            McTableDomain domain = (McTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("test") && domain.getSchema().equals("default") && domain.getTable().equals("abc");
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("test") && domain.getSchema().equals("default") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("test") && domain.getSchema().equals("default") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("name") && domain.getTypeDesc().equals("varchar(25)") && domain.getTypeName().equals("varchar");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTablePrimaryKey_1() {
        String sql = "create table test.abc(id int not null primary, name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/default/abc/name/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc");
            assert domain1.isHasPrimary();
        }
        {
            McColumnDomain domain1_2 = (McColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog().equals("test") && domain1_2.getSchema().equals("default") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("id") && domain1_2.getTypeName().equals("int");
            assert domain1_2.isPrimary();
        }
        {
            McColumnDomain domain1_2 = (McColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog().equals("test") && domain1_2.getSchema().equals("default") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("name") && domain1_2.getTypeDesc().equals("varchar(25)") && domain1_2.getTypeName().equals("varchar");
            assert !domain1_2.isPrimary();
        }
    }

    @Test
    public void createTablePrimaryKey_2() {
        String sql = "create table test.abc(id int not null, name varchar(25) not null,primary key (id) )";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/default/abc/name/");
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
            McTableDomain domain2_1 = (McTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.CREATE_TABLE && domain2_1.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_1.getCatalog().equals("test") && domain2_1.getSchema().equals("default") && domain2_1.getTable().equals("abc");
            assert domain2_1.isHasPrimary();
        }
        {
            McColumnDomain domain2_2 = (McColumnDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog().equals("test") && domain2_2.getSchema().equals("default") && domain2_2.getTable().equals("abc") &&//
                   domain2_2.getColumn().equals("id");
            assert domain2_2.isPrimary();
        }
        {
            RdbConstraintDomain domain2_2 = (RdbConstraintDomain) domainList.get(3);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog().equals("test") && domain2_2.getSchema().equals("default") && domain2_2.getColumns().equals(Collections.singletonList("id"));
            assert domain2_2.getType().equals(SqlConstraintType.Primary);
        }
        {
            McColumnDomain domain2_2 = (McColumnDomain) domainList.get(2);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog().equals("test") && domain2_2.getSchema().equals("default") && domain2_2.getTable().equals("abc") &&//
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
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/default/abc/name/");
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
            McTableDomain domain3 = (McTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog().equals("test") && domain3.getSchema().equals("default") && domain3.getTable().equals("abc");
            assert domain3.isHasPrimary();
        }
        {
            McColumnDomain domain3_2 = (McColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog().equals("test") && domain3_2.getSchema().equals("default") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("id");
            assert domain3_2.isPrimary();
        }
        {
            McColumnDomain domain3_2 = (McColumnDomain) domainList.get(2);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog().equals("test") && domain3_2.getSchema().equals("default") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("name");
            assert !domain3_2.isPrimary();
        }
        {
            RdbConstraintDomain domain3_3 = (RdbConstraintDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog().equals("test") && domain3_3.getTableSchema().equals("default") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id");
            assert domain3_3.getType() == SqlConstraintType.Primary;
        }
    }

    @Test
    public void createTableUniqueKey_1() {
        String sql = "create table test.abc(id double default 1.1, name varchar(25) not null default 'gweg')";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/default/abc/name/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc");
            assert !domain1.isHasUnique();
        }
        {
            McColumnDomain domain1_2 = (McColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog().equals("test") && domain1_2.getSchema().equals("default") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("id") && domain1_2.getTypeDesc().equals("double") && domain1_2.getTypeName().equals("double");
            assert !domain1_2.isUnique() && domain1_2.getDefaultValue().equals("1.1");
        }
        {
            McColumnDomain domain1_2 = (McColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog().equals("test") && domain1_2.getSchema().equals("default") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("name");
            assert !domain1_2.isUnique() && domain1_2.getDefaultValue().equals("gweg");
        }
    }

    @Test
    public void createTableColumns_1() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test/default/abc/name/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("name");
        }
    }

    @Test
    public void createTableComplex_1() {
        String sql = "CREATE TABLE test_default( \n" + "tinyint_name tinyint NOT NULL default 1,\n" + "smallint_name SMALLINT NOT NULL DEFAULT 1,\n"
                     + "int_name INT NOT NULL DEFAULT 1,\n" + "bigint_name BIGINT NOT NULL DEFAULT 1,\n" + "binary_name BINARY ,\n" + "float_name FLOAT ,\n"
                     + "double_name DOUBLE NOT NULL DEFAULT 0.1,\n" + "decimal_name DECIMAL(2, 1) NOT NULL DEFAULT 0.0,\n" + "varchar_name VARCHAR(10) ,\n"
                     + "char_name CHAR(2) ,\n" + "string_name STRING NOT NULL DEFAULT 'N',\n" + "boolean_name BOOLEAN NOT NULL DEFAULT TRUE\n"
                     + ") partitioned by (aaa string) range clustered by (int_name) sorted by (double_name) into 100 buckets;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 13;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/test_default/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 13;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") && domain1.getTable().equals("test_default");

        }
    }

    @Test
    public void createTableLike_1() {
        String sql = "create table test.abc_copy like test.abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc_copy/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE_LIKE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc_copy");
            assert domain1.getSourceSchema().equals("default") && domain1.getSourceTable().equals("abc");
        }
    }

    @Test
    public void createTableLike_2() {
        String sql = "create table abc_copy like abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc_copy/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE_LIKE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") && domain1.getTable().equals("abc_copy");
            assert domain1.getSourceSchema().equals("default") && domain1.getSourceTable().equals("abc");
        }
    }

    @Test
    public void createTableSelect_1() {
        String sql = "create table test.abc_select select * from test.abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc_select/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/abc/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE_SELECT;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_SELECT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc_select");
        }
        {
            McSelectDomain domain1_2 = (McSelectDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.SELECT && domain1_2.getAuditKind() == SecQueryKind.QUERY && domain1_2.getMode() == RdbQueryMode.CREATE;
            assert domain1_2.getSelectColumns().isEmpty() &&//
                   domain1_2.getSelectVariables().isEmpty() &&//
                   domain1_2.getSelectFunc().isEmpty() &&//
                   domain1_2.getWhereColumns().isEmpty() && //
                   domain1_2.getJoinTypes().isEmpty();
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
            assert !domain1_2.isHasLimit();
        }
    }

    @Test
    public void alterTableOption_1() {
        String sql = "alter table test.abc touch ";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableTouch_1() {
        String sql = "alter table test.abc touch PARTITION (sale_date='201312', region='hangzhou')";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableOption_3() {
        String sql = "alter table test.abc clustered by (int_name) sorted by (double_name) into 100 buckets; ";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableOption_4() {
        String sql = "alter table test.abc not clustered;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableOption_2() {
        String sql = "alter table test.abc set comment 'abc' ";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/abc/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog().equals("test") && domain1.getSchema().equals("default") && domain1.getTable().equals("abc");
            assert domain1.getComment().equals("abc");
        }
    }

    @Test
    public void alterTableRename_1() {
        String sql = "alter table abc rename to cba";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/cba/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_RENAME && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") &&//
                   domain1.getTable().equals("abc") && domain1.getNewName().equals("cba");
        }
    }

    @Test
    public void alterTableAddPartition_1() {
        String sql = "alter table abc ADD IF NOT EXISTS PARTITION (sale_date='201312', region='hangzhou');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableAddPartition_2() {
        String sql = "alter table abc PARTITION (sale_date='201312', region='hangzhou') rename TO PARTITION (sale_date = '201310', region = 'beijing');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableAlterPartition_2() {
        String sql = "alter table abc MERGE PARTITION(hh='00') overwrite PARTITION(ds='20181101', hh='00', mm='00');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableAlterPartition_3() {
        String sql = "ALTER TABLE abc DROP IF EXISTS PARTITION(sale_date='201312',region='hangzhou'),PARTITION(sale_date='201312',region='shanghai');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableAddProp_1() {
        String sql = "alter table abc SET tblproperties(\"write.bucket.num\"=\"64\");";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableMajor_1() {
        String sql = "alter table abc partition(dd='01', hh='01') compact major;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("default") &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void dropTable_1() {
        String sql = "drop table abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void truncateTable_1() {
        String sql = "truncate table abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.TRUNCATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void truncateTable_2() {
        String sql = "truncate table abc PARTITION(sale_date LIKE '2013%' AND region='hangzhou');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/abc/");
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
            McTableDomain domain1 = (McTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.TRUNCATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }
}
