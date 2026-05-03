package com.clougence.clouddm.ds.secdomain.special.ob4my;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.ObResAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.ObSecDomainResolveSpi;
import com.clougence.clouddm.ds.oceanbase.analysis.obformysql.ObSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyColumnDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyTableDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ObSecDomainResolve4TableTest extends MySecDomainTestSupport {

    public ObSecDomainResolve4TableTest(){
        this.analysisSpi = new ObResAnalysisSpi(null);
        this.resolveSpi = new ObSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ObSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.OceanBase;
    }

    @Test
    public void renameTable_1() {
        String sql = "rename table test1 to t2,t2 to t3";

        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        MyTableDomain domain = (MyTableDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.RENAME_TABLE && domain.getAuditKind() == SecQueryKind.ALTER;
        assert domain.getCatalog() == null && domain.getTable().equals("test1");
        assert domain.getNewName().equals("t2");
        MyTableDomain domain1 = (MyTableDomain) list1.get(1);
        assert domain1.getSqlType() == SecQueryType.RENAME_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
        assert domain1.getCatalog() == null && domain1.getTable().equals("t2");
        assert domain1.getNewName().equals("t3");

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.RENAME_TABLE;
        }
    }

    @Test
    public void alertTableAddColumn_1() {
        String sql1 = "alter table test.abc add column aa int";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MyColumnDomain domain = (MyColumnDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc");
        assert domain.getTypeName().equals("int");

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        String sql2 = "alter table test.abc add column aa int default 3 auto_increment not null unique key comment '32'";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (MyColumnDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc");
        assert domain.getTypeName().equals("int");
        assert domain.getDefaultValue().equals("3");
        assert domain.getComment().equals("32");
        assert domain.isUnique();

        splitScripts = this.splitAnalysisSpi.splitScript(sql2, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql2);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (MyColumnDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc");
        assert domain.getTypeName().equals("int");
        assert domain.getDefaultValue().equals("3");
        assert domain.getComment().equals("32");
        assert domain.isUnique();

        splitScripts = this.splitAnalysisSpi.splitScript(sql2, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql2);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        String sql3 = "alter table test.abc add column aa int after te";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql3), contextInfo());
        domain = (MyColumnDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("abc");
        assert domain.getTypeName().equals("int");

        splitScripts = this.splitAnalysisSpi.splitScript(sql3, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql3);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

    }

    @Test
    public void alertTableAddConstraint_1() {
        String sql1 = "alter table test.abc add constraint unique (testtt)";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        RdbConstraintDomain domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test");
        assert domain.getColumns().equals(Arrays.asList("testtt"));

        String sql2 = "alter table test.abc add constraint unique index (testtt)";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test");
        assert domain.getColumns().equals(Arrays.asList("testtt"));

        String sql3 = "alter table test.abc add constraint unique key (testtt)";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql3), contextInfo());
        domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test");
        assert domain.getColumns().equals(Arrays.asList("testtt"));

        String sql4 = "alter table test.abc add constraint cn unique key uk (testt1t)";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql4), contextInfo());
        domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test");
        assert domain.getColumns().equals(Arrays.asList("testt1t"));

        String sql5 = "alter table test.abc add constraint unique key testIndex (testtt)";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql5), contextInfo());
        domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test");
        assert domain.getColumns().equals(Arrays.asList("testtt"));
        assert domain.getName().equals("testIndex");
    }

    @Test
    public void alertTableAddConstraint_2() {
        String sql1 = "ALTER TABLE test.employees ADD CONSTRAINT FK_dept_id FOREIGN KEY (dept_id) REFERENCES departments(id);";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        RdbConstraintDomain domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test") && domain.getTableName().equals("employees");
        assert domain.getName().equals("FK_dept_id");
        assert domain.getType().equals(SqlConstraintType.ForeignKey);

        String sql2 = "ALTER TABLE test.employees ADD CONSTRAINT FK_dept_id FOREIGN KEY (dept_id) REFERENCES departments(id) match simple;";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test") && domain.getTableName().equals("employees");
        assert domain.getName().equals("FK_dept_id");
        assert domain.getType().equals(SqlConstraintType.ForeignKey);

        String sql3 = "ALTER TABLE test.employees ADD CONSTRAINT FK_dept_id FOREIGN KEY (dept_id) REFERENCES departments(id) match full;";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql3), contextInfo());
        domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test") && domain.getTableName().equals("employees");
        assert domain.getName().equals("FK_dept_id");
        assert domain.getType().equals(SqlConstraintType.ForeignKey);

        String sql4 = "ALTER TABLE test.employees ADD CONSTRAINT FK_dept_id FOREIGN KEY (dept_id) REFERENCES departments(id) match partial;";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql4), contextInfo());
        domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test") && domain.getTableName().equals("employees");
        assert domain.getName().equals("FK_dept_id");
        assert domain.getType().equals(SqlConstraintType.ForeignKey);

        String sql5 = "ALTER TABLE test.employees ADD CONSTRAINT FK_dept_id FOREIGN KEY (dept_id) REFERENCES departments(id) match partial on delete cascade;";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql5), contextInfo());
        domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test") && domain.getTableName().equals("employees");
        assert domain.getName().equals("FK_dept_id");
        assert domain.getType().equals(SqlConstraintType.ForeignKey);
    }

    @Test
    public void alertTableAlterColumn_2() {
        String sql1 = "ALTER TABLE test.employees alter column column1 set default 1;";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MyColumnDomain domain = (MyColumnDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
        assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("employees");
        assert domain.getDefaultValue().equals("1");

        String sql2 = "ALTER TABLE test.employees alter column column1 drop default;";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (MyColumnDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
        assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("employees");
        assert domain.getDefaultValue() == null;
    }

    @Test
    public void alertTableDropColumn_1() {
        String sql1 = "ALTER TABLE test.employees drop column column1;";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MyColumnDomain domain = (MyColumnDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_DROP_COLUMN && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("employees");
        assert domain.getColumn().equals("column1");

        String sql2 = "ALTER TABLE test.employees drop column1;";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (MyColumnDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_DROP_COLUMN && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("employees");
        assert domain.getColumn().equals("column1");
    }

    @Test
    public void alertTableDropForeignKey_1() {
        String sql1 = "ALTER TABLE test.employees DROP FOREIGN KEY fk_name;";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        RdbConstraintDomain domain = (RdbConstraintDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_DROP_CONSTRAINT && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getCatalog() == null && domain.getTableSchema().equals("test") && domain.getTableName().equals("employees");
        assert domain.getName().equals("fk_name");
        assert domain.getType().equals(SqlConstraintType.ForeignKey);
    }

    @Test
    public void alertTableRename_1() {
        String sql1 = "ALTER TABLE test.employees rename to qq;";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MyTableDomain domain = (MyTableDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_RENAME && domain.getAuditKind() == SecQueryKind.ALTER;
        assert domain.getNewName().equals("qq");

        String sql2 = "ALTER TABLE test.employees rename index old1 to new1;";
        List<RuleDomain> list2 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        RdbIndexDomain domain2 = (RdbIndexDomain) list2.get(0);
        assert domain2.getSqlType() == SecQueryType.ALTER_INDEX && domain2.getAuditKind() == SecQueryKind.ALTER;
        assert domain2.getNewName().equals("new1");
    }

    @Test
    public void createIndex_1() {
        String sql1 = "CREATE INDEX test_index ON test (c1, c2 ASC) comment 'sss';";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        RdbIndexDomain domain = (RdbIndexDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.CREATE_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
        assert domain.getName().equals("test_index");
        assert domain.getType().equals("index");
        assert domain.getComment().equals("sss");

        String sql2 = "CREATE unique INDEX test_index ON test (c1, c2 ASC) comment 'sss';";
        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql2, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql2);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        String sql3 = "CREATE unique INDEX test_index ON test (c1, c2 ASC) comment 'sss' VISIBLE;";
        List<RuleDomain> list2 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql3), contextInfo());
        RdbIndexDomain domain2 = (RdbIndexDomain) list2.get(0);
        assert domain2.getSqlType() == SecQueryType.CREATE_INDEX && domain2.getAuditKind() == SecQueryKind.CREATE;
        assert domain2.getName().equals("test_index");
        //        assert domain2.getType().equals("index");
        assert domain2.getComment().equals("sss");

        splitScripts = this.splitAnalysisSpi.splitScript(sql3, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql3);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        String sql4 = "CREATE unique INDEX test_index ON test (c1, c2 ASC) GLOBAL;";
        list2 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql4), contextInfo());
        domain2 = (RdbIndexDomain) list2.get(0);
        assert domain2.getSqlType() == SecQueryType.CREATE_INDEX && domain2.getAuditKind() == SecQueryKind.CREATE;
        assert domain2.getName().equals("test_index");
        assert domain2.getType().equals("unique");

        splitScripts = this.splitAnalysisSpi.splitScript(sql4, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql4);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        String sql5 = "CREATE unique INDEX test_index ON test (c1, c2 ASC) LOCAL;";
        list2 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql5), contextInfo());
        domain2 = (RdbIndexDomain) list2.get(0);
        assert domain2.getSqlType() == SecQueryType.CREATE_INDEX && domain2.getAuditKind() == SecQueryKind.CREATE;
        assert domain2.getName().equals("test_index");
        assert domain2.getType().equals("unique");

        splitScripts = this.splitAnalysisSpi.splitScript(sql5, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql5);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }
    }

    @Test
    public void dropIndex_1() {
        String sql = "DROP INDEX idx_test ON tbl_test;";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        RdbIndexDomain domain = (RdbIndexDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.DROP_INDEX && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getName().equals("idx_test");
        assert domain.getTableName().equals("tbl_test");

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_INDEX;
        }
    }

    @Test
    public void dropTable_1() {
        String sql1 = "DROP table t1;";
        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql1), contextInfo());
        MyTableDomain domain = (MyTableDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.DROP_TABLE && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getTable().equals("t1");
        assert !domain.isIfExists();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql1, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql1);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        String sql2 = "DROP table if exists t1";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql2), contextInfo());
        domain = (MyTableDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.DROP_TABLE && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getTable().equals("t1");
        assert domain.isIfExists();

        splitScripts = this.splitAnalysisSpi.splitScript(sql2, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql2);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        String sql3 = "DROP table if exists t1 cascade;";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql3), contextInfo());
        domain = (MyTableDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.DROP_TABLE && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getTable().equals("t1");
        assert domain.isIfExists();

        splitScripts = this.splitAnalysisSpi.splitScript(sql3, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql3);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        String sql4 = "DROP table if exists t1 restrict";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql4), contextInfo());
        domain = (MyTableDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.DROP_TABLE && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getTable().equals("t1");
        assert domain.isIfExists();

        String sql5 = "DROP tables if exists t1 restrict;";
        splitScripts = this.splitAnalysisSpi.splitScript(sql5, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql5);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }

        String sql6 = "DROP tables if exists t1,t2,t3,t4,t5 restrict;";
        list1 = resolveSpi.resolveDomain(this.dataSourceType, codeInfo(sql6), contextInfo());
        domain = (MyTableDomain) list1.get(0);
        assert domain.getSqlType() == SecQueryType.DROP_TABLE && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getTable().equals("t1");
        assert domain.isIfExists();
        domain = (MyTableDomain) list1.get(4);
        assert domain.getSqlType() == SecQueryType.DROP_TABLE && domain.getAuditKind() == SecQueryKind.DROP;
        assert domain.getTable().equals("t5");
        assert domain.isIfExists();

        splitScripts = this.splitAnalysisSpi.splitScript(sql6, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql6);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_TABLE;
        }
    }
}
