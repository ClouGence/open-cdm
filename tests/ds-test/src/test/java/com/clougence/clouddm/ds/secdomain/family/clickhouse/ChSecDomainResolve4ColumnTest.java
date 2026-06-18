package com.clougence.clouddm.ds.secdomain.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.clickhouse.analysis.ChResAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSecDomainResolveSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.ChSplitAnalysisSpi;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChColumnDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class ChSecDomainResolve4ColumnTest extends ChSecDomainTestSupport {

    public ChSecDomainResolve4ColumnTest(){
        this.analysisSpi = new ChResAnalysisSpi(null);
        this.resolveSpi = new ChSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new ChSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.ClickHouse;
    }

    @Test
    public void alterTableAddColumn_1() {
        String sql = "alter table `test`.`user_table` add column `address` varchar(255)";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/user_table/address/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableDropColumn_1() {
        String sql = "alter table user_table drop column address";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/user_table/address/");
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
            ChColumnDomain domain = (ChColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_DROP_COLUMN && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAlterColumn_1() {
        String sql = "alter table test modify column a1 char(10);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test/a1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc().equals("char(10)");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAlterColumn_2() {
        String sql = "alter table test alter column a1 type char default '32' comment '333';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test/a1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            ChColumnDomain domain = (ChColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc().equals("char");
            assert domain.getComment().equals("333") && domain.getDefaultValue().equals("32");
        }
    }

    @Test
    public void alterTableRenameColumn_1() {
        String sql = "alter table test rename column a1 to a2";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Column &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/a1/");
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
            ChColumnDomain domain = (ChColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_RENAME_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName().equals("a2");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableCommentColumn_1() {
        String sql = "alter table test comment column a1 'ybbb'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Column &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/a1/");
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
            ChColumnDomain domain = (ChColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.COMMENT_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getComment().equals("ybbb");
        }
    }
}
