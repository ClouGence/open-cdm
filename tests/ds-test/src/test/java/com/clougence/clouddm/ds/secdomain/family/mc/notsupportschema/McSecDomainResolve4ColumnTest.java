package com.clougence.clouddm.ds.secdomain.family.mc.notsupportschema;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.maxcompute.analysis.McResAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainResolveSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSplitAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McColumnDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class McSecDomainResolve4ColumnTest extends McSecDomainTestSupport {

    public McSecDomainResolve4ColumnTest(){
        this.analysisSpi = new McResAnalysisSpi(null);
        this.resolveSpi = new McSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new McSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MaxCompute;
    }

    @Test
    public void alterTableAddColumn_1() {
        String sql = "alter table `test`.`user_table` add columns (`address` varchar(255));";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/user_table/address/");
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
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("test") && domain.getSchema().equals("default") && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAddColumn_2() {
        String sql = "alter table `test`.`user_table` add columns (`address` varchar(255) comment '21213', qq int);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test/default/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test/default/user_table/address/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = this.resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog().equals("test") && domain.getSchema().equals("default") && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)");
            assert domain.getComment().equals("21213");
        }
    }

    @Test
    public void alterTableDropColumn_1() {
        String sql = "alter table user_table drop columns address;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/user_table/address/");
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
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_DROP_COLUMN && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableDropColumn_2() {
        String sql = "alter table user_table drop columns address,id;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/user_table/address/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/default/user_table/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_DROP_COLUMN && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAlterColumn_1() {
        String sql = "alter table test change a1 a123456 char(10);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/test/a1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc().equals("char(10)");
            assert domain.getComment() == null;
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_RENAME_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName().equals("a123456") && domain.getTypeDesc() == null;
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAlterColumn_5() {
        String sql = "alter table a.test change a1 a123456 char(10) comment '22';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/a/default/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/a/default/test/a1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog().equals("a") && domain.getSchema().equals("default") && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc().equals("char(10)");
            assert domain.getComment().equals("22");
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_RENAME_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog().equals("a") && domain.getSchema().equals("default") && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName().equals("a123456") && domain.getTypeDesc() == null;
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAlterColumn_2() {
        String sql = "alter table test change a1 rename to a123456;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/test/a1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc() == null;
            assert domain.getComment() == null;
        }
        {
            McColumnDomain domain = (McColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_RENAME_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName().equals("a123456") && domain.getTypeDesc() == null;
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAlterColumn_3() {
        String sql = "alter table test change a1 comment '2';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/test/a1/");
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
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc() == null;
            assert domain.getComment().equals("2");
        }

    }

    @Test
    public void alterTableAlterColumn_4() {
        String sql = "alter table test change a1 null;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/default/test/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/default/test/a1/");
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
            McColumnDomain domain = (McColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema().equals("default") && domain.getTable().equals("test") &&//
                   domain.getColumn().equals("a1") && domain.getNewName() == null && domain.getTypeDesc() == null;
            assert domain.getComment() == null && domain.isNullable();
        }

    }
}
