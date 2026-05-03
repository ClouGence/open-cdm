package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsColumnDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4TableTest extends MsSecDomainTestSupport {

    public MsSecDomainResolve4TableTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void createTable_basic_1() {
        String sql = "create table abc(id int);";

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
            MsTableDomain domain1 = (MsTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
    }

    @Test
    public void createTable_basic_4() {
        String sql = "create table sqlserver.abc(id int);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/sqlserver/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/sqlserver/abc/id/");
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
            MsTableDomain domain2 = (MsTableDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.CREATE_TABLE && domain2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("sqlserver") && domain2.getTable().equals("abc") &&//
                   !domain2.isIfNotExists() && domain2.getOptions().isEmpty();
        }
    }

    @Test
    public void createTable_basic_5() {
        String sql = "create table abc.[public].abc(id int);";

        List<ResObject> resList = parserRes(sql);
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
            MsTableDomain domain3 = (MsTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog().equals("abc") && domain3.getSchema().equals("public") && domain3.getTable().equals("abc") &&//
                   !domain3.isIfNotExists() && domain3.getOptions().isEmpty();
        }
    }

    //        @Test
    //        public void createTableNode_1_1() {
    //            String sql = "create table abc(id int) as node";
    //
    //            List<ResObject> resList = this.analysisSpi.analysisResource(dataSourceType, sql, ctx);
    //            assert resList.size() == 2;
    //            {
    //                assert resList.get(0).getType() == TargetType.Table &&//
    //                       resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
    //                assert resList.get(1).getType() == TargetType.Column &&//
    //                       resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/id/");
    //            }
    //
    //            List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, sql);
    ////            assert domainList.size() == 2;
    ////            {
    ////                PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
    ////                assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getSqlKind() == SqlKind.CREATE;
    ////                assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
    ////                       domain1.isInherits() && domain1.getInheritTables().contains("users");
    ////            }
    //        }

    @Test
    public void createTablePrimaryKey_1() {
        String sql = "create table test.abc(id int, name varchar(25) not null)";

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
        assert domainList.size() == 3;
        {
            MsTableDomain domain1 = (MsTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasPrimary();
        }
        {
            MsColumnDomain domain1_2 = (MsColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("id");
            assert !domain1_2.isPrimary();
        }
    }

    @Test
    public void createTablePrimaryKey_2() {
        String sql = "create table test.abc(id int, name varchar(25) not null,primary key (id))";

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
            MsTableDomain domain2_1 = (MsTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.CREATE_TABLE && domain2_1.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema().equals("test") && domain2_1.getTable().equals("abc");
            assert domain2_1.isHasPrimary();
        }
        {
            MsColumnDomain domain2_2 = (MsColumnDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") && domain2_2.getColumn().equals("id");
            assert domain2_2.isPrimary();
        }
        {
            MsColumnDomain domain2_3 = (MsColumnDomain) domainList.get(2);
            assert domain2_3.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_3.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_3.getCatalog() == null && domain2_3.getSchema().equals("test") && domain2_3.getTable().equals("abc") && domain2_3.getColumn().equals("name");
            assert !domain2_3.isPrimary();
        }
        {
            RdbConstraintDomain domain2_4 = (RdbConstraintDomain) domainList.get(3);
            assert domain2_4.getSqlType() == SecQueryType.CREATE_TABLE_ADD_CONSTRAINT && domain2_4.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_4.getCatalog() == null && domain2_4.getSchema().equals("test");
            assert domain2_4.getType() == SqlConstraintType.Primary;
        }
    }

    @Test
    public void createTablePrimaryKey_3() {
        String sql = "create table test.abc(id int primary key, name varchar(25) not null)";

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
        assert domainList.size() == 3;
        {
            MsTableDomain domain3 = (MsTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasPrimary();
        }
        {
            MsColumnDomain domain3_2 = (MsColumnDomain) domainList.get(1);
            assert domain3_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain3_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain3_2.getCatalog() == null && domain3_2.getSchema().equals("test") && domain3_2.getTable().equals("abc") && domain3_2.getColumn().equals("id");
            assert domain3_2.isPrimary();
        }
    }

    @Test
    public void createTableUniqueKey_1() {
        String sql = "create table test.abc(id int, name varchar(25) not null)";

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
        assert domainList.size() == 3;
        {
            MsTableDomain domain1 = (MsTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasUnique();
        }
        {
            MsColumnDomain domain1_2 = (MsColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("id");
            assert !domain1_2.isUnique();
        }
    }

    @Test
    public void createTableUniqueKey_2() {
        String sql = "create table test.abc(id int unique, name varchar(25) not null)";

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
        assert domainList.size() == 3;
        {
            MsTableDomain domain2_1 = (MsTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.CREATE_TABLE && domain2_1.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema().equals("test") && domain2_1.getTable().equals("abc");
            assert domain2_1.isHasUnique();
        }
        {
            MsColumnDomain domain2_2 = (MsColumnDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain2_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema().equals("test") && domain2_2.getTable().equals("abc") && domain2_2.getColumn().equals("id");
            assert domain2_2.isUnique();
        }
    }

    @Test
    public void createTableUniqueKey_3() {
        String sql = "create table test.abc(id int, name varchar(25) not null,unique (id))";

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
            MsTableDomain domain3 = (MsTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasUnique();
        }
        {
            MsColumnDomain domain3_2 = (MsColumnDomain) domainList.get(1);
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
        assert domainList.size() == 3;
        {
            MsTableDomain domain1 = (MsTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert !domain1.isHasForeignKey();
        }
        {
            MsColumnDomain domain1_2 = (MsColumnDomain) domainList.get(1);
            assert domain1_2.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1_2.getAuditKind() == SecQueryKind.CREATE;
            assert domain1_2.getCatalog() == null && domain1_2.getSchema().equals("test") && domain1_2.getTable().equals("abc") && domain1_2.getColumn().equals("id");
            assert !domain1_2.isForeign();
        }
    }

    @Test
    public void createTableForeignKey_2() {
        String sql = "create table test.abc(id int, name varchar(25) not null,constraint ptr foreign key (id) references test.abc2(id2))";

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
            MsTableDomain domain3 = (MsTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.CREATE_TABLE && domain3.getAuditKind() == SecQueryKind.CREATE;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test") && domain3.getTable().equals("abc");
            assert domain3.isHasForeignKey();
        }
        {
            MsColumnDomain domain3_2 = (MsColumnDomain) domainList.get(1);
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
        assert domainList.size() == 3;
        {
            MsTableDomain domain1 = (MsTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert domain1.getColumns().contains("id") && domain1.getColumns().contains("name");
        }
        {
            MsColumnDomain domain1 = (MsColumnDomain) domainList.get(1);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert domain1.getColumn().equals("id") && domain1.getTypeName().contains("int");
        }
        {
            MsColumnDomain domain1 = (MsColumnDomain) domainList.get(2);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test") && domain1.getTable().equals("abc");
            assert domain1.getColumn().equals("name") && domain1.getTypeName().equals("varchar") && domain1.getLength().equals("25");
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
        {
            MsTableDomain domain1 = (MsTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.DROP_TABLE && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   !domain1.isIfExists();
        }
    }

    @Test
    public void dropTable_2() {
        String sql = "drop table abc,cba";

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
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            MsTableDomain domain2_1 = (MsTableDomain) domainList.get(0);
            assert domain2_1.getSqlType() == SecQueryType.DROP_TABLE && domain2_1.getAuditKind() == SecQueryKind.DROP;
            assert domain2_1.getCatalog() == null && domain2_1.getSchema() == null && domain2_1.getTable().equals("abc") &&//
                   !domain2_1.isIfExists();
        }
        {
            MsTableDomain domain2_2 = (MsTableDomain) domainList.get(1);
            assert domain2_2.getSqlType() == SecQueryType.DROP_TABLE && domain2_2.getAuditKind() == SecQueryKind.DROP;
            assert domain2_2.getCatalog() == null && domain2_2.getSchema() == null && domain2_2.getTable().equals("cba") &&//
                   !domain2_2.isIfExists();
        }
    }

    @Test
    public void dropTable_3() {
        String sql = "drop table if exists abc";

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
        {
            MsTableDomain domain3 = (MsTableDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.DROP_TABLE && domain3.getAuditKind() == SecQueryKind.DROP;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("abc") && //
                   domain3.isIfExists();
        }
    }

    @Test
    public void dropTable_4() {
        String sql = "drop table if exists abc,cba";

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
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            MsTableDomain domain4_1 = (MsTableDomain) domainList.get(0);
            assert domain4_1.getSqlType() == SecQueryType.DROP_TABLE && domain4_1.getAuditKind() == SecQueryKind.DROP;
            assert domain4_1.getCatalog() == null && domain4_1.getSchema() == null && domain4_1.getTable().equals("abc") &&//
                   domain4_1.isIfExists();
        }
        {
            MsTableDomain domain4_2 = (MsTableDomain) domainList.get(1);
            assert domain4_2.getSqlType() == SecQueryType.DROP_TABLE && domain4_2.getAuditKind() == SecQueryKind.DROP;
            assert domain4_2.getCatalog() == null && domain4_2.getSchema() == null && domain4_2.getTable().equals("cba") &&//
                   domain4_2.isIfExists();
        }
    }
}
