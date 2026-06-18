package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4ConstraintTest extends MsSecDomainTestSupport {

    public MsSecDomainResolve4ConstraintTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void alterTableAddPrimaryKey_1() {
        String sql = "alter table abc add primary key (id);";

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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.Primary && domain1.getName() == null &&//
                   domain1.getTableName().equals("abc") && domain1.getColumns().contains("id");
        }
    }

    @Test
    public void alterTableDropPrimaryKey_1() {
        String sql = "alter table abc drop constraint abc_pk";

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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_DROP_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.ByName &&//
                   domain1.getTableName().equals("abc") && domain1.getName().equals("abc_pk");
        }
    }

    @Test
    public void alterTableAddUnique_1() {
        String sql = "alter table abc add unique(name);";

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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.Unique && domain1.getName() == null &&//
                   domain1.getTableName().equals("abc") && domain1.getColumns().contains("name");
        }
    }

    @Test
    public void alterTableAddUniqueWithName_1() {
        String sql = "alter table abc add constraint cname unique(name);";

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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_ADD_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.Unique && domain1.getName().equals("cname") &&//
                   domain1.getTableName().equals("abc") && domain1.getColumns().contains("name");
        }
    }

    @Test
    public void alterTableDropUnique_1() {
        String sql = "alter table abc drop constraint abc_uk";

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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_DROP_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.ByName &&//
                   domain1.getTableName().equals("abc") && domain1.getName().equals("abc_uk");
        }
    }

    @Test
    public void alterTableAddForeign_1() {
        String sql = "alter table abc add constraint ptr foreign key (id) references abc2 (id2)";

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
            RdbConstraintDomain domain1 = (RdbConstraintDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_TABLE_DROP_CONSTRAINT && domain1.getAuditKind() == SecQueryKind.DROP;
            assert domain1.getTableCatalog() == null && domain1.getTableSchema() == null &&//
                   domain1.getType() == SqlConstraintType.ByName &&//
                   domain1.getTableName().equals("abc") && domain1.getName().equals("abc_fk");
        }
    }
}
