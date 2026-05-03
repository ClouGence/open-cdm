package com.clougence.clouddm.ds.secdomain.family.starrocks;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.starrocks.analysis.SrResAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSecDomainResolveSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSplitAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrColumnDomain;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrSelectDomain;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class SrSecDomainResolve4TableTest extends SrSecDomainTestSupport {

    public SrSecDomainResolve4TableTest(){
        this.analysisSpi = new SrResAnalysisSpi(null);
        this.resolveSpi = new SrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.StarRocks;
    }

    @Test
    public void createTable_1() {
        String sql = "create table abc(id int auto_increment) DISTRIBUTED BY HASH(store_id) BUCKETS 10 properties(\"x\"=\"z\")";

        List<ResObject> resList = parserRes(sql);
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
            SrTableDomain domain = (SrTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc");
            assert !domain.isIfNotExists();
            assert !domain.isTemporary();
            assert domain.getComment() == null;
            assert domain.getOptions().size() == 1 && domain.getOptions().get("x").equals("z");
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && domain.isAuto();
        }
    }

    @Test
    public void createTableEngine_1() {
        String sql = "create table abc(id int,    k2 DECIMAL(10, 2) DEFAULT \"10.5\") engine = olap DUPLICATE KEY(id)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test_schema/abc/k2/");
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
            SrTableDomain domain = (SrTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists() && domain.getEngine().equals("olap");
            assert !domain.isTemporary() && domain.getTableModel().equals("DUPLICATE");
            assert domain.getComment() == null;
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(2);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("k2") && domain.getTypeDesc().equals("DECIMAL(10, 2)") && domain.getTypeName().equals("DECIMAL");
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
            SrTableDomain domain = (SrTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists();
            assert !domain.isTemporary();
            assert domain.getComment().equalsIgnoreCase("desc");
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableExternal_1() {
        String sql = "create external table abc(id int) engine = mysql";

        List<ResObject> resList = parserRes(sql);
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
            SrTableDomain domain = (SrTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists() && domain.getEngine().equals("mysql");
            assert !domain.isTemporary() && domain.isExternal();
            assert domain.getComment() == null;
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableIfNotExists_1() {
        String sql = "create table if not exists test.abc(id int)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
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
            SrTableDomain domain = (SrTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getOptions().isEmpty();
            assert domain.isIfNotExists();
            assert !domain.isTemporary();
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
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
            SrTableDomain domain = (SrTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getOptions().isEmpty();
            assert domain.isIfNotExists();
            assert !domain.isTemporary();
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void createTableTemporary() {
        String sql = "create temporary table if not exists test.abc(id int)";

        List<ResObject> resList = parserRes(sql);

        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
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
            SrTableDomain domain = (SrTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert domain.isIfNotExists();
            assert domain.isTemporary();
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && domain.getAggrType() == null;
        }
    }

    @Test
    public void createTableAggrColumn_1() {
        String sql = "create table abc(id int sum)";

        List<ResObject> resList = parserRes(sql);
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
            SrTableDomain domain = (SrTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
            assert !domain.isIfNotExists();
            assert !domain.isTemporary();
            assert domain.getComment() == null;
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null && domain.getAggrType().equals("sum");
        }
    }

    @Test
    public void createTableIndex_1() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null,INDEX k1 (id) comment '2322')";

        List<ResObject> resList = parserRes(sql);
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
            SrTableDomain domain3 = (SrTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasIndex();
        }
        {
            SrColumnDomain domain3_2 = (SrColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") && domain3_2.getColumn().equals("id");
            assert domain3_2.isIndex();
        }
        {
            RdbIndexDomain domain3_3 = (RdbIndexDomain) domainList.get(3);
            assert domain3_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_INDEX && domain3_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_3.getCatalog() == null && domain3_3.getTableSchema().equals("test") && domain3_3.getTableName().equals("abc") &&//
                   domain3_3.getColumns().contains("id");
            assert domain3_3.getType().equals("index") && domain3_3.getComment().equals("2322");
        }
    }

    @Test
    public void createTableColumns_1() {
        String sql = "create table test.abc(id int(4), name varchar(25) not null, ar ARRAY<INT(11)>)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/abc/name/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_db/test/abc/ar/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("name");
        }
        {
            SrColumnDomain domain1_2 = (SrColumnDomain) domainList.get(3);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getColumn().equals("ar") && domain1_2.getTypeName().equals("ARRAY") && domain1_2.getTypeDesc().equals("ARRAY<INT(11)>");
        }
    }

    @Test
    public void createTableLike_1() {
        String sql = "create table test.abc_copy like test.abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/abc_copy/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc_copy");
            assert domain1.getSourceSchema().equals("test") && domain1.getSourceTable().equals("abc");
        }
    }

    @Test
    public void createTableLike_3() {
        String sql = "create temporary table abc_copy like abc;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc_copy/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_LIKE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc_copy");
            assert domain1.getSourceSchema() == null && domain1.getSourceTable().equals("abc") && domain1.isTemporary();
        }
    }

    @Test
    public void createTableSelect_1() {
        String sql = "create table test.abc_select as select * from test.abc;";

        List<ResObject> resList = parserRes(sql);
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
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE_LIKE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_SELECT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc_select");
        }
        {
            SrSelectDomain domain1_2 = (SrSelectDomain) domainList.get(1);
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
    public void alterTableRename_1() {
        String sql = "ALTER table abc rename cba";

        List<ResObject> resList = parserRes(sql);
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
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_RENAME && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc") && domain1.getNewName().equals("cba");
        }
    }

    @Test
    public void alterTableRename_2() {
        String sql = "ALTER table a.abc rename cba";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/a/abc/");
            assert resList.get(1).getType() == TargetType.Table &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/a/cba/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_RENAME && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("a") &&//
                   domain1.getTable().equals("abc") && domain1.getNewName().equals("cba");
        }
    }

    @Test
    public void alterTableComment_1() {
        String sql = "ALTER table abc  comment  = \"new comment\"";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc") && domain1.getNewName() == null && domain1.getComment().equals("new comment");
        }
    }

    @Test
    public void alterTableRollup_1() {
        String sql = "ALTER table abc add rollup r1 (k1,v1)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc") && domain1.getNewName() == null && domain1.getComment() == null;
        }
    }

    @Test
    public void alterTableOptimize() {
        String sql = "ALTER TABLE abc DISTRIBUTED BY RANDOM;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc") && domain1.getNewName() == null && domain1.getComment() == null;
        }
    }

    @Test
    public void alterTableProperties_1() {
        String sql = "ALTER table abc set (\"a\"=\"b\",\"c\" = \"d\")";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc") && domain1.getNewName() == null && domain1.getComment() == null;
        }
    }

    @Test
    public void alterTableRollup_2() {
        String sql = "ALTER table abc drop rollup r1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc") && domain1.getNewName() == null && domain1.getComment() == null;
        }
    }

    @Test
    public void dropTable_1() {
        String sql = "drop table abc";

        List<ResObject> resList = parserRes(sql);
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
        assert domainList.size() == 1;
        {
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void dropTable_2() {
        String sql = "drop table t1.abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/t1/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("t1") &&//
                   domain1.getTable().equals("abc");
        }
    }

    @Test
    public void cancelAlterTable1() {
        String sql = "CANCEL ALTER TABLE COLUMN FROM db_name.table_name";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/db_name/table_name/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("db_name") &&//
                   domain1.getTable().equals("table_name");
        }
    }

    @Test
    public void cancelAlterTable2() {
        String sql = "CANCEL ALTER TABLE COLUMN FROM table_name";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/table_name/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("table_name");
        }
    }

    @Test
    public void truncateTable1() {
        String sql = "truncate table t1.abc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/t1/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
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
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
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
            SrTableDomain domain1 = (SrTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.TRUNCATE && domain1.getAuditKind() == SecQueryKind.DML;
            assert domain1.getCatalog() == null && domain1.getSchema() == null &&//
                   domain1.getTable().equals("abc");
        }
    }
}
