package com.clougence.clouddm.ds.secdomain.family.mysql;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MySecDomainResolve4ConstraintTest extends MySecDomainTestSupport {

    public MySecDomainResolve4ConstraintTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
    }

    @Test
    public void alterTableAddPrimaryKey_1() {
        String sql = "alter table `user_table` add primary key (id);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_table/");
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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.Primary &&//
                   domain1.getTableName().equals("user_table") && domain1.getColumns().contains("id");
        }
    }

    @Test
    public void alterTableDropPrimaryKey_1() {
        String sql = "alter table user_table drop primary key;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_table/");
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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_DROP_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.Primary &&//
                   domain1.getTableName().equals("user_table");
        }
    }

    @Test
    public void alterTableAddUnique_1() {
        String sql = "alter table user_table add constraint primary key (`id`);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_table/");
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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.Primary &&//
                   domain1.getTableName().equals("user_table") && domain1.getColumns().contains("id");
        }
    }

    @Test
    public void alterTableDropUnique_1() {
        String sql = "alter table user_table drop constraint abc_uk;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_table/");
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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_DROP_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.ByName && domain1.getName().equals("abc_uk") &&//
                   domain1.getTableName().equals("user_table");
        }
    }

    @Test
    public void alterTableAddForeign_1() {
        String sql = "alter table abc add constraint ptr foreign key (id) references abc2 (id2)";

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
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.ForeignKey && domain1.getName().equals("ptr") &&//
                   domain1.getTableName().equals("abc") && domain1.getColumns().contains("id");
        }
    }

    @Test
    public void alterTableDropForeign_1() {
        String sql = "alter table abc drop constraint abc_fk";

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
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }
        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_DROP_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.ByName &&//
                   domain1.getTableName().equals("abc") && domain1.getName().equals("abc_fk");
        }
    }

    @Test
    public void alterTableDropForeign_2() {
        String sql = "alter table abc drop foreign key `abc_fk`;";

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
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbConstraintDomain domain2 = (RdbConstraintDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_TABLE_DROP_CONSTRAINT && domain2.getAuditKind() == SecQueryKind.DROP;
            assert domain2.getTableCatalog() == null && domain2.getTableSchema() == null &&//
                   domain2.getType() == SqlConstraintType.ForeignKey &&//
                   domain2.getTableName().equals("abc") && domain2.getName().equals("abc_fk");
        }
    }
}
