package com.clougence.clouddm.ds.secdomain.special.tidb;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.ds.tidb.analysis.TiResAnalysisSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
import com.clougence.clouddm.ds.tidb.analysis.TiSplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyShowDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyShowType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class TiSecDomainResolve4ShowTest extends MySecDomainTestSupport {

    public TiSecDomainResolve4ShowTest(){
        this.analysisSpi = new TiResAnalysisSpi(null);
        this.resolveSpi = new TiSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new TiSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.TiDB;
    }

    @Test
    public void showCollation_1() {
        String sql = "show collation";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.COLLATION && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showCollation_2() {
        String sql = "show collation like 'abc%'";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.COLLATION && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showCollation_3() {
        String sql = "show collation where id = 64";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain3 = (MyShowDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SHOW && domain3.getAuditKind() == SecQueryKind.QUERY;
            assert domain3.getShowType() == MyShowType.COLLATION && domain3.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showColumns_1() {
        String sql = "SHOW FIELDS FROM test_db.test_table;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.COLUMNS && domain.getTarget() == TargetType.Column;
            assert domain.getCatalog() == null && domain.getSchema().equals("test_db") && domain.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_2() {
        String sql = "SHOW full FIELDS FROM test_db.test_table;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.COLUMNS && domain.getTarget() == TargetType.Column;
            assert domain.getCatalog() == null && domain.getSchema().equals("test_db") && domain.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_3() {
        String sql = "SHOW columns FROM test_db.test_table;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.COLUMNS && domain.getTarget() == TargetType.Column;
            assert domain.getCatalog() == null && domain.getSchema().equals("test_db") && domain.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_4() {
        String sql = "SHOW full columns FROM test_db.test_table;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.COLUMNS && domain.getTarget() == TargetType.Column;
            assert domain.getCatalog() == null && domain.getSchema().equals("test_db") && domain.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_5() {
        String sql = "SHOW columns in test_db.test_table;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.COLUMNS && domain.getTarget() == TargetType.Column;
            assert domain.getCatalog() == null && domain.getSchema().equals("test_db") && domain.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_6() {
        String sql = "SHOW full columns in test_db.test_table;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.COLUMNS && domain.getTarget() == TargetType.Column;
            assert domain.getCatalog() == null && domain.getSchema().equals("test_db") && domain.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_7() {
        String sql = "SHOW full columns in test_db.test_table where id = 2;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.COLUMNS && domain.getTarget() == TargetType.Column;
            assert domain.getCatalog() == null && domain.getSchema().equals("test_db") && domain.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_8() {
        String sql = "SHOW full columns in test_db.test_table where id like '%a';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.COLUMNS && domain.getTarget() == TargetType.Column;
            assert domain.getCatalog() == null && domain.getSchema().equals("test_db") && domain.getTable().equals("test_table");
        }
    }

    @Test
    public void showCreateDatabase_1() {
        String sql = "show create database test_db1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.CREATE_DATABASE && domain1.getTarget() == TargetType.Schema;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db1") && domain1.getTable() == null;
        }
    }

    @Test
    public void showCreateDatabase_2() {
        String sql = "show create schema test_db1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.CREATE_DATABASE && domain2.getTarget() == TargetType.Schema;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db1") && domain2.getTable() == null;
        }
    }

    @Test
    public void showCreateDatabase_3() {
        String sql = "show create schema if not exists test_db1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain3 = (MyShowDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SHOW && domain3.getAuditKind() == SecQueryKind.QUERY;
            assert domain3.getShowType() == MyShowType.CREATE_DATABASE && domain3.getTarget() == TargetType.Schema;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test_db1") && domain3.getTable() == null;
        }
    }

    @Test
    public void showDatabase_1() {
        String sql = "show databases";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.DATABASES && domain.getTarget() == TargetType.Schema;
        }
    }

    @Test
    public void showDatabase_2() {
        String sql = "show databases where `Database` = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.DATABASES && domain.getTarget() == TargetType.Schema;
        }
    }

    @Test
    public void showDatabase_3() {
        String sql = "show databases where `Database` like '%q'";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain = (MyShowDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.SHOW && domain.getAuditKind() == SecQueryKind.QUERY;
            assert domain.getShowType() == MyShowType.DATABASES && domain.getTarget() == TargetType.Schema;
        }
    }

    // can't parse
    //    @Test
    //    public void showErrors_1() {
    //        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, "show errors where `Level` = 'Error'");
    //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
    //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
    //        assert domain1.getShowType() == MyShowType.ERRORS && domain1.getTarget() == TargetType.Environment;
    //
    //        List<RuleDomain> list2 = resolveSpi.resolveDomain(this.dataSourceType, "show errors where Code like '1%'");
    //        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
    //        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
    //        assert domain2.getShowType() == MyShowType.ERRORS && domain2.getTarget() == TargetType.Environment;
    //    }

    @Test
    public void showVariables_1() {
        String sql = "SHOW GLOBAL VARIABLES LIKE 'tidb%';";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.VARIABLES && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showVariables_2() {
        String sql = "SHOW SESSION VARIABLES where a = 'tidb%';";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.VARIABLES && domain2.getTarget() == TargetType.Environment;
        }
    }

    //@Test
    public void showEngines_1() {
        // can't parse
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, "show engines like 'I%'");
        //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
        //        assert domain1.getShowType() == MyShowType.ENGINES && domain1.getTarget() == TargetType.Environment;
    }

    @Test
    public void showIndexes_1() {
        String sql = "show index from test_table from test_db";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.INDEX && domain1.getTarget() == TargetType.Index;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db") && domain1.getTable().equals("test_table");
        }
    }

    @Test
    public void showIndexes_2() {
        String sql = "show indexes from test_db.test_table";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.INDEX && domain2.getTarget() == TargetType.Index;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db") && domain2.getTable().equals("test_table");
        }
    }

    @Test
    public void showIndexes_3() {
        String sql = "show keys from test_table";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain3 = (MyShowDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SHOW && domain3.getAuditKind() == SecQueryKind.QUERY;
            assert domain3.getShowType() == MyShowType.INDEX && domain3.getTarget() == TargetType.Index;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("test_table");
        }
    }

    @Test
    public void showIndexes_4() {
        String sql = "show keys from test_table where a =3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain4 = (MyShowDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.SHOW && domain4.getAuditKind() == SecQueryKind.QUERY;
            assert domain4.getShowType() == MyShowType.INDEX && domain4.getTarget() == TargetType.Index;
            assert domain4.getCatalog() == null && domain4.getSchema() == null && domain4.getTable().equals("test_table");
        }
    }

    @Test
    public void showIndexes_5() {
        String sql = "show keys from test_table where a like '_3'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain5 = (MyShowDomain) domainList.get(0);
            assert domain5.getSqlType() == SecQueryType.SHOW && domain5.getAuditKind() == SecQueryKind.QUERY;
            assert domain5.getShowType() == MyShowType.INDEX && domain5.getTarget() == TargetType.Index;
            assert domain5.getCatalog() == null && domain5.getSchema() == null && domain5.getTable().equals("test_table");
        }
    }

    @Test
    public void showIndexes_6() {
        String sql = "show keys from test_table from tt where a like '_3'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/tt/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain6 = (MyShowDomain) domainList.get(0);
            assert domain6.getSqlType() == SecQueryType.SHOW && domain6.getAuditKind() == SecQueryKind.QUERY;
            assert domain6.getShowType() == MyShowType.INDEX && domain6.getTarget() == TargetType.Index;
            assert domain6.getCatalog() == null && domain6.getSchema().equals("tt") && domain6.getTable().equals("test_table");
        }
    }

    @Test
    public void showIndexes_7() {
        String sql = "show keys from test_table in tt where a like '_3'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/tt/test_table/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain7 = (MyShowDomain) domainList.get(0);
            assert domain7.getSqlType() == SecQueryType.SHOW && domain7.getAuditKind() == SecQueryKind.QUERY;
            assert domain7.getShowType() == MyShowType.INDEX && domain7.getTarget() == TargetType.Index;
            assert domain7.getCatalog() == null && domain7.getSchema().equals("tt") && domain7.getTable().equals("test_table");
        }
    }

    @Test
    public void showTableStatus_1() {
        String sql = "show table status";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.TABLE_STATUS && domain1.getTarget() == TargetType.Table;
            assert domain1.getCatalog() == null && domain1.getSchema() == null;
        }
    }

    @Test
    public void showTableStatus_2() {
        String sql = "show table status from test_db";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.TABLE_STATUS && domain2.getTarget() == TargetType.Table;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db");
        }
    }

    @Test
    public void showTableStatus_3() {
        String sql = "show table status in test_db1 where id =3";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain3 = (MyShowDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SHOW && domain3.getAuditKind() == SecQueryKind.QUERY;
            assert domain3.getShowType() == MyShowType.TABLE_STATUS && domain3.getTarget() == TargetType.Table;
            assert domain3.getCatalog() == null && domain3.getSchema().equals("test_db1");
        }
    }

    @Test
    public void showStatus_1() {
        String sql = "show global status where id = 1";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.STATUS && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showStatus_2() {
        String sql = "show global status where id like 'q%'";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.STATUS && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showStatus_3() {
        String sql = "show session status";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyShowDomain domain3 = (MyShowDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SHOW && domain3.getAuditKind() == SecQueryKind.QUERY;
            assert domain3.getShowType() == MyShowType.STATUS && domain3.getTarget() == TargetType.Environment;
        }
    }

    // can't parse
    //@Test
    public void showPlugins_1() {
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(this.dataSourceType, "show plugins like 'A%'");
        //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
        //        assert domain1.getShowType() == MyShowType.PLUGINS && domain1.getTarget() == TargetType.Environment;
    }

    @Test
    public void showGrants_1() {
        String sql = "show grants";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.GRANTS && domain1.getTarget() == TargetType.UserOrRole;
            assert domain1.getCatalog() == null && domain1.getUserOrRole() == null;
        }
    }

    @Test
    public void showGrants_2() {
        String sql = "show grants for user1";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.GRANTS && domain2.getTarget() == TargetType.UserOrRole;
            assert domain2.getCatalog() == null && domain2.getUserOrRole().equals("user1");
        }
    }

    @Test
    public void showGrants_3() {
        String sql = "show grants for CURRENT_USER";

        List<ResObject> resList = parserRes(sql);
        assert resList.isEmpty();

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.SHOW;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        {
            MyShowDomain domain3 = (MyShowDomain) domainList.get(0);
            assert domain3.getSqlType() == SecQueryType.SHOW && domain3.getAuditKind() == SecQueryKind.QUERY;
            assert domain3.getShowType() == MyShowType.GRANTS && domain3.getTarget() == TargetType.UserOrRole;
            assert domain3.getCatalog() == null && domain3.getUserOrRole().equals("CURRENT_USER");
        }
    }
}
