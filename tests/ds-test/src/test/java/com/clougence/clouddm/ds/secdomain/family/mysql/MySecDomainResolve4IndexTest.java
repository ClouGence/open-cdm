package com.clougence.clouddm.ds.secdomain.family.mysql;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyIndexDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MySecDomainResolve4IndexTest extends MySecDomainTestSupport {

    public MySecDomainResolve4IndexTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
    }

    @Test
    public void addIndex() {
        String sql = "CREATE INDEX test_index ON test (c1, c2 ASC) comment 'sss';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyIndexDomain domain = (MyIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getName().equals("test_index");
            assert domain.getType().equals("index");
            assert domain.getComment().equals("sss");
        }
    }

    @Test
    public void addUniqueIndex() {
        String sql = "CREATE unique INDEX test_index ON test (c1, c2 ASC) comment 'sss';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyIndexDomain domain = (MyIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getName().equals("test_index");
            assert domain.getType().equals("unique");
            assert domain.getColumns().size() == 2 && domain.getColumns().containsAll(Arrays.asList("c1", "c2"));
            assert domain.getComment().equals("sss");
        }
    }

    @Test
    public void alterTableAddIndex() {
        String sql = "ALTER TABLE test ADD KEY www (c1(33),c2(33)) comment 'sss';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
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
            MyIndexDomain domain = (MyIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getName().equals("www");
            assert domain.getType().equals("index");
            assert domain.getColumns().size() == 2 && domain.getColumns().containsAll(Arrays.asList("c1", "c2"));
            assert domain.getComment().equals("sss");
        }
    }

    @Test
    public void dropIndex() {
        String sql = "DROP INDEX idx_test ON test;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_INDEX && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getName().equals("idx_test");
            assert domain.getTableName().equals("test");
        }
    }

    @Test
    public void alterDropIndex() {
        String sql = "alter table test DROP INDEX idx_test;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
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
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_INDEX && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getName().equals("idx_test");
            assert domain.getTableName().equals("test");
        }
    }

    @Test
    public void dropIndex2() {
        String sql = "DROP INDEX `idx_test` ON `test_schema1`.test;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema1/test/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.DROP_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.DROP_INDEX && domain.getAuditKind() == SecQueryKind.DROP;
            assert domain.getName().equals("idx_test");
            assert domain.getTableName().equals("test") && domain.getTableSchema().equals("test_schema1");
        }
    }

    @Test
    public void renameIndex() {
        String sql = "ALTER TABLE test rename index old1 to new1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test/");
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
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_INDEX && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getName().equals("old1") && domain.getNewName().equals("new1");
            assert domain.getTableName().equals("test") && domain.getTableSchema() == null;
        }
    }
}
