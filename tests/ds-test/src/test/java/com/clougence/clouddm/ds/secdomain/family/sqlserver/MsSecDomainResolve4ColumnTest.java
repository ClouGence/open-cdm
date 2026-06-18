package com.clougence.clouddm.ds.secdomain.family.sqlserver;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlResAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSecDomainResolveSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSqlSplitAnalysisSpi;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsColumnDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MsSecDomainResolve4ColumnTest extends MsSecDomainTestSupport {

    public MsSecDomainResolve4ColumnTest(){
        this.analysisSpi = new MsSqlResAnalysisSpi();
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
        this.splitAnalysisSpi = new MsSqlSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.SQLServer;
    }

    @Test
    public void alterTableAddColumn_1() {
        String sql = "alter table user_table add address varchar(255);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/address/");
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
            MsColumnDomain domain = (MsColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAddColumn_PK_1() {
        String sql = "alter table user_table add address varchar(255) primary key;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/address/");
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
            MsColumnDomain domain = (MsColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)") &&//
                   domain.isPrimary();
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAddColumn_UK_1() {
        String sql = "alter table user_table add address varchar(255) unique;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/address/");
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
            MsColumnDomain domain = (MsColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)") &&//
                   domain.isUnique();
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAddColumn_FK_1() {
        String sql = "alter table user_table add address varchar(255) references abc(id2);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/address/");
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
            MsColumnDomain domain = (MsColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)") &&//
                   domain.isForeign();
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAddColumn_FK_DELETE_1() {
        String sql = "alter table user_table add address varchar(255) references abc(id2) ON DELETE NO ACTION;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/address/");
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
            MsColumnDomain domain = (MsColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)") &&//
                   domain.isForeign();
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableDropColumn_2() {
        String sql = "alter table abc drop column address;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/address/");
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
            MsColumnDomain domain2 = (MsColumnDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.ALTER_TABLE_DROP_COLUMN && domain2.getAuditKind() == SecQueryKind.DROP;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("abc") &&//
                   domain2.getColumn().equals("address");
            assert domain2.getComment() == null;
        }
    }

    @Test
    public void alterTableAlterColumn_1() {
        String sql = "alter table test alter column a1 char(10);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test/a1/");
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
            MsColumnDomain domain = (MsColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc().equals("char(10)");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAlterColumnDefaultValue_1() {
        String sql = "alter table test alter column a1 char(10) default ('312');";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/test/a1/");
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
            MsColumnDomain domain = (MsColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc().equals("char(10)");
            assert domain.getComment() == null;
            assert domain.getDefaultValue() != null;
        }
    }
}
