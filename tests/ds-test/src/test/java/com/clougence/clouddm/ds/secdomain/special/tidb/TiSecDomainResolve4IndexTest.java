package com.clougence.clouddm.ds.secdomain.special.tidb;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.ds.tidb.analysis.TiResAnalysisSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyIndexDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class TiSecDomainResolve4IndexTest extends MySecDomainTestSupport {

    public TiSecDomainResolve4IndexTest(){
        this.analysisSpi = new TiResAnalysisSpi(null);
        this.resolveSpi = new TiSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new TiSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.TiDB;
    }

    @Test
    public void indexVisible_1() {
        String sql = "alter table test ALTER INDEX aa VISIBLE";

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
            assert domain.getSqlType() == SecQueryType.ALTER_INDEX && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("test") && domain.getVisible();
        }
    }

    @Test
    public void indexVisible_2() {
        String sql = "alter table test ALTER INDEX aa INVISIBLE";

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
            assert domain.getSqlType() == SecQueryType.ALTER_INDEX && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("test") && !domain.getVisible();
        }
    }

    @Test
    public void indexRename_1() {
        String sql = "ALTER TABLE t1 RENAME INDEX col1 TO c1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getSqlType() == SecQueryType.ALTER_INDEX && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getNewName().equals("c1");
            assert domain.getName().equals("col1");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void indexRename_2() {
        String sql = "ALTER TABLE t1 RENAME KEY col1 TO c1;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyIndexDomain domain = (MyIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_INDEX && domain.getAuditKind() == SecQueryKind.ALTER;
            assert domain.getNewName().equals("c1");
            assert domain.getName().equals("col1");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void indexAdd_1() {
        String sql = "ALTER TABLE t1 ADD INDEX key1 (c1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getNewName() == null;
            assert domain.getName().equals("key1");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void indexAdd_2() {
        String sql = "ALTER TABLE t1 ADD INDEX (c1);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getNewName() == null;
            assert domain.getName() == null;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void indexAdd_3() {
        String sql = "ALTER TABLE t1 ADD INDEX (c1) comment 'wegwgwgwgwe';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getNewName() == null;
            assert domain.getName() == null;
            assert domain.getComment().equals("wegwgwgwgwe");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void indexAdd_4() {
        String sql = "ALTER TABLE t1 ADD KEY www (c1(33)) comment 'wegwgwgwgwe';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getNewName() == null;
            assert domain.getName().equals("www");
            assert domain.getComment().equals("wegwgwgwgwe");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void fulltextKeyAdd_1() {
        String sql = "ALTER TABLE t1 ADD FULLTEXT KEY kkk (c1,c2);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void fulltextKeyAdd_2() {
        String sql = "ALTER TABLE t1 ADD FULLTEXT KEY kkk (c1,c2) comment '999';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getComment().equals("999");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void fulltextKeyAdd_3() {
        String sql = "ALTER TABLE t1 ADD FULLTEXT INDEX kkk (c1,c2);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void fulltextKeyAdd_4() {
        String sql = "ALTER TABLE t1 ADD FULLTEXT INDEX kkk (c1,c2) comment '999';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getComment().equals("999");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    @Test
    public void fulltextKeyAdd_5() {
        String sql = "ALTER TABLE t1 ADD FULLTEXT  kkk (c1,c2);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t1/");
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
            assert domain.getColumns().equals(Arrays.asList("c1", "c2"));
            assert domain.getName().equals("kkk");
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t1");
        }
    }

    // todo can't parse
    //    @Test
    //    public void columnNotExists_1() {
    //        List<RuleDomain> list = resolveSpi.resolveDomain(DataSourceType.MySQL, "alter ignore table table_name add column if not exists ww int");
    //        OraColumnDomain domain = (OraColumnDomain) list.get(0);
    //        assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getSqlKind() == SqlKind.CREATE;
    //
    //        assert domain.getCatalog()==null && domain.getSchema() == null && domain.getTable().equals("table_name") && domain.getOptions().isEmpty();
    //    }
}
