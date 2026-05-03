package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChColumnDomain;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChSelectDomain;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChTableDomain;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4TableTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4TableTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void createTable_1() {
        String sql = "create table abc(id int) engine = test order by id";

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
        assert domainList.size() == 3;
        {
            ChTableDomain domain = (ChTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists() && domain.isHasPrimary();
            assert !domain.isTemporary();
            assert domain.getComment() == null;
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable() && domain.isPrimary();
        }
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getTableName().equals("abc") && domain.getColumns().equals(Collections.singletonList("id"));
            assert domain.getType().equals(SqlConstraintType.Primary);
        }
    }

    @Test
    public void createTableFuncPri_1() {
        String sql = "CREATE TABLE abc (event_time DateTime, user_id UInt32, url String)ENGINE = MergeTree\n"
                     + "ORDER BY (toStartOfHour(event_time), user_id) PRIMARY KEY (toStartOfHour(event_time))";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/abc/event_time/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 5;
        {
            ChTableDomain domain = (ChTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists();
            assert !domain.isTemporary();
            assert domain.getComment() == null;
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("event_time") && domain.getTypeDesc().equals("DateTime") && domain.getTypeName().equals("DateTime");
            assert domain.getComment() == null && !domain.isNullable() && domain.isPrimary();
        }
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(4);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getTableName().equals("abc") && domain.getColumns().equals(Collections.singletonList("event_time"));
            assert domain.getType().equals(SqlConstraintType.Primary);
        }
    }

    @Test
    public void createTable_2() {
        String sql = "create table `abc`(`id` int) engine = MEMORY COMMENT 'desc' ORDER BY tuple()";

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
            ChTableDomain domain = (ChTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists();
            assert !domain.isTemporary();
            assert domain.getComment().equalsIgnoreCase("desc");
            assert domain.getEngine().equals("MEMORY");
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable();
        }
    }

    @Test
    public void createTableIfNotExists_1() {
        String sql = "create table if not exists test.abc(id int) engine = MEMORY";

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
            ChTableDomain domain = (ChTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getOptions().isEmpty();
            assert domain.isIfNotExists();
            assert !domain.isTemporary();
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable();
        }
    }

    @Test
    public void createTableIfNotExists_2() {
        String sql = "create table if not exists abc(id int) engine = MEMORY";

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
            ChTableDomain domain = (ChTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getOptions().isEmpty();
            assert domain.isIfNotExists();
            assert !domain.isTemporary();
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable();
        }
    }

    @Test
    public void createTableTemporary() {
        String sql = "create temporary table if not exists test.abc(id int) engine = memory";

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
            ChTableDomain domain = (ChTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert domain.isIfNotExists();
            assert domain.isTemporary();
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable();
        }
    }

    @Test
    public void createTableOptions_1() {
        String sql = "create table test.abc(id int, name varchar,primary key (id)) engine = MergeTree";

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
            ChTableDomain domain = (ChTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc");

        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable();
        }

        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(3);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getColumns().equals(Collections.singletonList("id"));
            assert domain.getType().equals(SqlConstraintType.Primary);
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("name") && domain.getTypeDesc().equals("varchar") && domain.getTypeName().equals("varchar");
            assert domain.getComment() == null && !domain.isNullable();
        }
    }

    @Test
    public void createTableOptions_2_1() {
        String sql = "create table test.abc(id int , name varchar   ,PRIMARY KEY id) engine = MergeTree";

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
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert domain1.getEngine().equalsIgnoreCase("MergeTree");
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable();
        }
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(3);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getColumns().equals(Collections.singletonList("id"));
            assert domain.getType().equals(SqlConstraintType.Primary);
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("name") && domain.getTypeDesc().equals("varchar") && domain.getTypeName().equals("varchar");
            assert domain.getComment() == null && !domain.isNullable();
        }
    }

    @Test
    public void createTableOptions_2_2() {
        String sql = "create table test.abc(id int, name varchar) engine = myisam primary key id";

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
            ChTableDomain domain2 = (ChTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test") && domain2.getTable().equals("abc");
            assert domain2.getEngine().equalsIgnoreCase("myisam");
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable();
        }
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(3);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getColumns().equals(Collections.singletonList("id"));
            assert domain.getType().equals(SqlConstraintType.Primary);
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("name") && domain.getTypeDesc().equals("varchar") && domain.getTypeName().equals("varchar");
            assert domain.getComment() == null && !domain.isNullable();
        }
    }

    @Test
    public void createTableOptions_3() {
        String sql = "create table test.abc(id int, name varchar) engine = egr primary key (id)";

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
            ChTableDomain domain = (ChTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc");
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && !domain.isNullable();
        }
        {
            RdbConstraintDomain domain = (RdbConstraintDomain) domainList.get(3);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getColumns().equals(Collections.singletonList("id"));
            assert domain.getType().equals(SqlConstraintType.Primary);
        }
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("name") && domain.getTypeDesc().equals("varchar") && domain.getTypeName().equals("varchar");
            assert domain.getComment() == null && !domain.isNullable();
        }
    }

    @Test
    public void createTablePrimaryKey_1() {
        String sql = "create table test.abc(id int, name varchar) engine memory";

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
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasPrimary();
        }
        {
            ChColumnDomain domain1_2 = (ChColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("id") && domain1_2.getTypeName().equals("int");
            assert !domain1_2.isPrimary();
        }
        {
            ChColumnDomain domain1_2 = (ChColumnDomain) domainList.get(2);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") &&//
                   domain1_2.getColumn().equals("name") && domain1_2.getTypeDesc().equals("varchar") && domain1_2.getTypeName().equals("varchar");
            assert !domain1_2.isPrimary() && !domain1_2.isNullable();
        }
    }

    @Test
    public void createTablePrimaryKey_2() {
        String sql = "create table test.abc(id int, name varchar) engine = memory  primary key id";

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
            ChTableDomain domain2_1 = (ChTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.CREATE_TABLE && domain2_1.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema().equals("test") && domain2_1.getTable().equals("abc");
            assert domain2_1.isHasPrimary();
        }
        {
            ChColumnDomain domain2_2 = (ChColumnDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") &&//
                   domain2_2.getColumn().equals("id");
            assert domain2_2.isPrimary() && !domain2_2.isNullable();
        }
        {
            RdbConstraintDomain domain2_2 = (RdbConstraintDomain) domainList.get(3);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getColumns().equals(Collections.singletonList("id"));
            assert domain2_2.getType().equals(SqlConstraintType.Primary);
        }
        {
            ChColumnDomain domain2_2 = (ChColumnDomain) domainList.get(2);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") &&//
                   domain2_2.getColumn().equals("name");
            assert !domain2_2.isPrimary() && !domain2_2.isNullable();
        }
    }

    @Test
    public void createTablePrimaryKey_3() {
        String sql = "create table test.abc(id int, name varchar,primary key (id)) engine = memory";

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
            ChTableDomain domain3 = (ChTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasPrimary();
        }
        {
            ChColumnDomain domain3_2 = (ChColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("id");
            assert domain3_2.isPrimary() && !domain3_2.isNullable();
        }
        {
            ChColumnDomain domain3_2 = (ChColumnDomain) domainList.get(2);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("name");
            assert !domain3_2.isPrimary() && !domain3_2.isNullable();
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
        String sql = "create table test.abc(id int, name varchar,primary key (id,name)) engine = memory order by id";

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
            ChTableDomain domain3 = (ChTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasPrimary();
        }
        {
            ChColumnDomain domain3_2 = (ChColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("id");
            assert domain3_2.isPrimary() && !domain3_2.isNullable();
        }
        {
            ChColumnDomain domain3_2 = (ChColumnDomain) domainList.get(2);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") &&//
                   domain3_2.getColumn().equals("name");
            assert domain3_2.isPrimary() && !domain3_2.isNullable();
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
    public void createTableIndex_1() {
        String sql = "create table test.abc(id int(4), name varchar(25)) engine = test";

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
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasIndex();
        }
        {
            ChColumnDomain domain1_2 = (ChColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("id");
            assert !domain1_2.isIndex() && !domain1_2.isNullable();
        }
    }

    @Test
    public void createTableIndex_2() {
        String sql = "create table test.abc(id int(4), name varchar(25),INDEX idx_name id TYPE minmax GRANULARITY 4) engine = test TTL event_time + INTERVAL 30 DAY PARTITION BY toYYYYMMDD(event_time)";

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
            ChTableDomain domain3 = (ChTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasIndex();
        }
        {
            ChColumnDomain domain3_2 = (ChColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") && domain3_2.getColumn().equals("id");
            assert domain3_2.isIndex() && !domain3_2.isNullable();
        }
        {
            RdbIndexDomain domain3_3 = (RdbIndexDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_INDEX && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id");
            assert domain3_3.getType().equals("index") && domain3_3.getName().equals("idx_name");
        }
    }

    @Test
    public void createTableIndex_3() {
        String sql = "create table test.abc(id nullable(int(4)), name varchar(25),INDEX idx_name (id,name) TYPE minmax GRANULARITY 4) engine = test PARTITION BY (toYear(event_time), user_id % 10)";

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
            ChTableDomain domain3 = (ChTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasIndex();
        }
        {
            ChColumnDomain domain3_2 = (ChColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") && domain3_2.getColumn().equals("id");
            assert domain3_2.isIndex() && domain3_2.isNullable();
        }
        {
            RdbIndexDomain domain3_3 = (RdbIndexDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_INDEX && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id") && domain3_3.getColumns().contains("name");
            assert domain3_3.getType().equals("index") && domain3_3.getName().equals("idx_name");
        }
    }

    @Test
    public void createTableIndex_4() {
        String sql = "create table test.abc(id nullable(int(4)), name varchar(25),INDEX idx_name (ss(id,name)) TYPE minmax GRANULARITY 4) engine = test PARTITION BY (toYear(event_time), user_id % 10)";

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
            ChTableDomain domain3 = (ChTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasIndex();
        }
        {
            ChColumnDomain domain3_2 = (ChColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") && domain3_2.getColumn().equals("id");
            assert domain3_2.isIndex() && domain3_2.isNullable();
        }
        {
            RdbIndexDomain domain3_3 = (RdbIndexDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_INDEX && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id") && domain3_3.getColumns().contains("name");
            assert domain3_3.getType().equals("index") && domain3_3.getName().equals("idx_name");
        }
    }

    @Test
    public void createTableLike_1() {
        String sql = "create table test.abc_copy as test.abc engine = ff";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc_copy/");
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
        assert domainList.size() == 1;
        {
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc_copy");
            assert domain1.getSourceSchema().equals("test") && domain1.getSourceTable().equals("abc");
        }
    }

    @Test
    public void createTableLike_2() {
        String sql = "create table abc_copy as abc engine = gwe";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc_copy/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/abc/");
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
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc_copy");
            assert domain1.getSourceSchema() == null && domain1.getSourceTable().equals("abc");
        }
    }

    @Test
    public void createTableSelect_1() {
        String sql = "create table test.abc_select engine = test as select * from test.abc";

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
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_SELECT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc_select");
        }
        {
            ChSelectDomain domain1_2 = (ChSelectDomain) domainList.get(1);
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
    public void renameTable_1() {
        String sql = "rename table test.abc to abc_test";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/abc_test/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChTableDomain domain2 = (ChTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.RENAME_TABLE;
            assert domain2.getCatalog() == null && domain2.getNewSchemaName() == null && domain2.getNewName().equals("abc_test");
        }
    }

    @Test
    public void renameTable_2() {
        String sql = "rename table abc to test.abc_test";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc_test/");
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
            ChTableDomain domain2 = (ChTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.RENAME_TABLE;
            assert domain2.getCatalog() == null && domain2.getNewSchemaName().equals("test") && domain2.getNewName().equals("abc_test");
        }
    }

    @Test
    public void renameTable_3() {
        String sql = "rename table test.abc to test.abc_test";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/abc_test/");
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
            ChTableDomain domain2 = (ChTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.RENAME_TABLE;
            assert domain2.getCatalog() == null && domain2.getNewSchemaName().equals("test") && domain2.getNewName().equals("abc_test");
        }
    }

    @Test
    public void alterTableOption_2() {
        String sql = "ALTER TABLE test.abc MODIFY TTL event_time + INTERVAL 60 DAY";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
        }

        List<SplitScript> splitScripts = splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableDropPartition() {
        String sql = "ALTER TABLE test.abc drop partition 2022";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
        }

        List<SplitScript> splitScripts = splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableUpdate() {
        String sql = "ALTER TABLE test.abc update id = 1 where id = 2 or name = 3 and zz =4";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
        }

        List<SplitScript> splitScripts = splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.UPDATE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChUpdateDomain domain1 = (ChUpdateDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.UPDATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc") &&//
                   domain1.getSetColumns().size() == 1 && domain1.getSetColumns().contains("id") &&//
                   domain1.getWhereColumns().size() == 3 && domain1.getWhereColumns().equals(Arrays.asList("id", "name", "zz"));
            assert !domain1.isSelectInSet() &&  //
                   !domain1.isMultiUpdate();
            assert domain1.isHasWhere() &&      //
                   !domain1.isSelectInWhere() &&//
                   !domain1.isHasUnion() &&     //
                   !domain1.isHasWith();
        }
    }

    @Test
    public void alterTableRemoveTTL() {
        String sql = "ALTER TABLE test.abc remove TTL";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/abc/");
        }

        List<SplitScript> splitScripts = splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
        }
    }

    @Test
    public void alterTableRename_1() {
        String sql = "rename table abc to cba";

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
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.RENAME_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc") && domain1.getNewName().equals("cba");
        }
    }

    @Test
    public void alterTableRename_2() {
        String sql = "rename table schema1.abc to schema2.cba";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/schema1/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/schema2/cba/");
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
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.RENAME_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("schema1") && domain1.getNewSchemaName().equals("schema2") &&//
                   domain1.getTable().equals("abc") && domain1.getNewName().equals("cba");
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
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void dropTable_2() {
        String sql = "drop table if exists test.abc";

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
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChTableDomain domain1 = (ChTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") &&//
                   domain1.getTable().equals("abc");
            assert domain1.isIfExists();
        }
    }

    @Test
    public void truncateTable1() {
        String sql = "truncate table t1.abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/t1/abc/");
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
            RdbTableDomain domain1 = (RdbTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.TRUNCATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("t1") &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void truncateTable2() {
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
            RdbTableDomain domain1 = (RdbTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.TRUNCATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }
}
