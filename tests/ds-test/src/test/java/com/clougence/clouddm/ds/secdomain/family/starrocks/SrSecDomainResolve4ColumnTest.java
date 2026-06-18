package com.clougence.clouddm.ds.secdomain.family.starrocks;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.starrocks.analysis.SrResAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSecDomainResolveSpi;
import com.clougence.clouddm.ds.starrocks.analysis.SrSplitAnalysisSpi;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrColumnDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class SrSecDomainResolve4ColumnTest extends SrSecDomainTestSupport {

    public SrSecDomainResolve4ColumnTest(){
        this.analysisSpi = new SrResAnalysisSpi(null);
        this.resolveSpi = new SrSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new SrSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.StarRocks;
    }

    @Test
    public void alterTableAddColumn_1() {
        String sql = "alter table `test`.`user_table` add column `address` varchar(255);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/user_table/address/");
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
            SrColumnDomain domain = (SrColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableAddColumn_2() {
        String sql = "alter table `test`.`user_table` add column (`address` varchar(255), aa int);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test/user_table/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test/user_table/address/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_db/test/user_table/aa/");
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
            SrColumnDomain domain = (SrColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getTypeDesc().equals("varchar(255)");
            assert domain.getComment() == null;
        }
        {
            SrColumnDomain domain = (SrColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema().equals("test") && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("aa") && domain.getTypeDesc().equals("int");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableDropColumn_1() {
        String sql = "alter table user_table drop column address;";

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
            SrColumnDomain domain = (SrColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_DROP_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableModifyColumn_1() {
        String sql = "alter table user_table modify column address varchar(25) default '1';";

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
            SrColumnDomain domain = (SrColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getDefaultValue().equals("1");
            assert domain.getComment() == null;
        }
    }

    @Test
    public void alterTableModifyColumn_2() {
        String sql = "alter table user_table modify column address varchar(25) default '1' comment '222';";

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
            SrColumnDomain domain = (SrColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ALTER_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("address") && domain.getDefaultValue().equals("1");
            assert domain.getComment().equals("222");
        }
    }

    @Test
    public void alterTableRenameColumn_1() {
        String sql = "alter table user_table rename column c1 to c2;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Column &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/user_table/c1/");
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
            SrColumnDomain domain = (SrColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_RENAME_COLUMN && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_table") &&//
                   domain.getColumn().equals("c1") && domain.getNewName().equals("c2");
            assert domain.getComment() == null;
        }
    }

}
