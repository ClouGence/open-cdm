package com.clougence.clouddm.ds.secdomain.family.mysql;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.mysql.analysis.MyResAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyShowDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyShowType;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

// https://dev.mysql.com/doc/refman/8.4/en/show.html
public class MySecDomainResolve4ShowTest extends MySecDomainTestSupport {

    public MySecDomainResolve4ShowTest(){
        this.analysisSpi = new MyResAnalysisSpi(null);
        this.resolveSpi = new MySecDomainResolveSpi(null);
        this.splitAnalysisSpi = new MySplitAnalysisSpi();
        this.dataSourceType = DataSourceType.MySQL;
    }

    @Test
    public void showBinaryLogStatus_1() {
        // todo can't parse
        //        List<RuleDomain> list = resolveSpi.resolveDomain(dataSourceType, "show binary log status");
        //        MyShowDomain domain = (MyShowDomain) list.get(0);
        //        assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getSqlKind() == SqlKind.CREATE;
        //        assert domain.getCatalog()== null && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
    }

    @Test
    public void showBinaryLogs_1() {
        String sql = "show binary logs";

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
            assert domain.getShowType() == MyShowType.BINARY_LOGS && domain.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showBinaryEvents_1() {
        String sql = "show binlog events";

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
            assert domain1.getShowType() == MyShowType.BINLOG_EVENTS && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showBinaryEvents_2() {
        String sql = "show binlog events in 'log_name'";

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
            assert domain2.getShowType() == MyShowType.BINLOG_EVENTS && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showCharacterSet_1() {
        String sql = "show character set";

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
            assert domain1.getShowType() == MyShowType.CHARACTER_SET && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showCharacterSet_2() {
        String sql = "show character set like 'abc%'";

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
            assert domain2.getShowType() == MyShowType.CHARACTER_SET && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showCharacterSet_3() {
        String sql = "show charset";

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
            assert domain3.getShowType() == MyShowType.CHARACTER_SET && domain3.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showCharacterSet_4() {
        String sql = "show charset like 'abc%'";

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
            MyShowDomain domain4 = (MyShowDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.SHOW && domain4.getAuditKind() == SecQueryKind.QUERY;
            assert domain4.getShowType() == MyShowType.CHARACTER_SET && domain4.getTarget() == TargetType.Environment;
        }
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
    public void showColumns_1() {
        String sql = "show columns from test_table from test_db1";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db1/test_table/");
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
            assert domain1.getShowType() == MyShowType.COLUMNS && domain1.getTarget() == TargetType.Column;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db1") && domain1.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_2() {
        String sql = "show columns from test_db1.test_table";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db1/test_table/");
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
            assert domain2.getShowType() == MyShowType.COLUMNS && domain2.getTarget() == TargetType.Column;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db1") && domain2.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_3() {
        String sql = "show columns from test_table";

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
            assert domain3.getShowType() == MyShowType.COLUMNS && domain3.getTarget() == TargetType.Column;
            assert domain3.getCatalog() == null && domain3.getSchema() == null && domain3.getTable().equals("test_table");
        }
    }

    @Test
    public void showColumns_4() {
        String sql = "show full columns from test_table from test_db like 'abc%'";

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
            MyShowDomain domain4 = (MyShowDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.SHOW && domain4.getAuditKind() == SecQueryKind.QUERY;
            assert domain4.getShowType() == MyShowType.COLUMNS && domain4.getTarget() == TargetType.Column;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("test_db") && domain4.getTable().equals("test_table");
        }
    }

    @Test
    public void desc_1() {
        String sql = "desc test_table";

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
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.COLUMNS && domain1.getTarget() == TargetType.Column;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("test_table");
        }
    }

    @Test
    public void desc_2() {
        String sql = "desc test_db.test_table";

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
            assert domain2.getShowType() == MyShowType.COLUMNS && domain2.getTarget() == TargetType.Column;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db") && domain2.getTable().equals("test_table");
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
    public void showCreateEvent_1() {
        String sql = "show create event test_db.test_event";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Event &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_event/");
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
            assert domain1.getShowType() == MyShowType.CREATE_EVENT && domain1.getTarget() == TargetType.Event;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db") && domain1.getEvent().equals("test_event");
        }
    }

    @Test
    public void showCreateEvent_2() {
        String sql = "show create event test_event";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Event &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_event/");
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
            assert domain2.getShowType() == MyShowType.CREATE_EVENT && domain2.getTarget() == TargetType.Event;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getEvent().equals("test_event");
        }
    }

    @Test
    public void showCreateFunction_1() {
        String sql = "show create function test_db.test_func";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_func/");
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
            assert domain1.getShowType() == MyShowType.CREATE_FUNCTION && domain1.getTarget() == TargetType.Function;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db") && domain1.getFunc().equals("test_func");
        }
    }

    @Test
    public void showCreateFunction_2() {
        String sql = "show create function test_func";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_func/");
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
            assert domain2.getShowType() == MyShowType.CREATE_FUNCTION && domain2.getTarget() == TargetType.Function;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getFunc().equals("test_func");
        }
    }

    @Test
    public void showCreateProcedure_1() {
        String sql = "show create procedure test_db.test_proc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_proc/");
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
            assert domain1.getShowType() == MyShowType.CREATE_PROCEDURE && domain1.getTarget() == TargetType.Procedure;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db") && domain1.getProc().equals("test_proc");
        }
    }

    @Test
    public void showCreateProcedure_2() {
        String sql = "show create procedure test_proc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_proc/");
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
            assert domain2.getShowType() == MyShowType.CREATE_PROCEDURE && domain2.getTarget() == TargetType.Procedure;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getProc().equals("test_proc");
        }
    }

    @Test
    public void showCreateTable_1() {
        String sql = "show create table test_db.test_table";

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
            assert domain1.getShowType() == MyShowType.CREATE_TABLE && domain1.getTarget() == TargetType.Table;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db") && domain1.getTable().equals("test_table");
        }
    }

    @Test
    public void showCreateTable_2() {
        String sql = "show create table test_table";

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
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.CREATE_TABLE && domain2.getTarget() == TargetType.Table;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTable().equals("test_table");
        }
    }

    @Test
    public void showCreateView_1() {
        String sql = "show create view test_db.test_view";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_view/");
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
            assert domain1.getShowType() == MyShowType.CREATE_VIEW && domain1.getTarget() == TargetType.View;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db") && domain1.getView().equals("test_view");
        }
    }

    @Test
    public void showCreateView_2() {
        String sql = "show create view test_view";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.View &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_view/");
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
            assert domain2.getShowType() == MyShowType.CREATE_VIEW && domain2.getTarget() == TargetType.View;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getView().equals("test_view");
        }
    }

    @Test
    public void showCreateTrigger_1() {
        String sql = "show create trigger test_db.test_trigger";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Trigger &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_trigger/");
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
            assert domain1.getShowType() == MyShowType.CREATE_TRIGGER && domain1.getTarget() == TargetType.Trigger;
            assert domain1.getCatalog() == null && domain1.getSchema().equals("test_db") && domain1.getTrigger().equals("test_trigger");
        }
    }

    @Test
    public void showCreateTrigger_2() {
        String sql = "show create trigger test_trigger";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Trigger &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_trigger/");
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
            assert domain2.getShowType() == MyShowType.CREATE_TRIGGER && domain2.getTarget() == TargetType.Trigger;
            assert domain2.getCatalog() == null && domain2.getSchema() == null && domain2.getTrigger().equals("test_trigger");
        }
    }

    @Test
    public void showCreateUser_1() {
        // todo can't parse
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create user user1");
        //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
        //        assert domain1.getShowType() == MyShowType.CREATE_USER && domain1.getTarget() == TargetType.UserOrRole;
        //        assert domain1.getCatalog()== null && domain1.getSchema() == null && domain1.getUserOrRole().equals("user1");
    }

    @Test
    public void showDatabases_1() {
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
            MyShowDomain domain1 = (MyShowDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.SHOW && domain1.getAuditKind() == SecQueryKind.QUERY;
            assert domain1.getShowType() == MyShowType.DATABASES && domain1.getTarget() == TargetType.Schema;
        }
    }

    @Test
    public void showDatabases_2() {
        String sql = "show schemas";

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
            assert domain2.getShowType() == MyShowType.DATABASES && domain2.getTarget() == TargetType.Schema;
        }
    }

    @Test
    public void showTriggers_1() {
        String sql = "show triggers";

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
            assert domain1.getShowType() == MyShowType.TRIGGERS && domain1.getTarget() == TargetType.Trigger;
            assert domain1.getCatalog() == null && domain1.getSchema() == null;
        }
    }

    @Test
    public void showTriggers_2() {
        String sql = "show triggers in test_db";

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
            assert domain2.getShowType() == MyShowType.TRIGGERS && domain2.getTarget() == TargetType.Trigger;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db");
        }
    }

    @Test
    public void showEvents_1() {
        String sql = "show events";

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
            assert domain1.getShowType() == MyShowType.EVENTS && domain1.getTarget() == TargetType.Event;
            assert domain1.getCatalog() == null && domain1.getSchema() == null;
        }
    }

    @Test
    public void showEvents_2() {
        String sql = "show events in test_db";

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
            assert domain2.getShowType() == MyShowType.EVENTS && domain2.getTarget() == TargetType.Event;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db");
        }
    }

    @Test
    public void showWarnings_1() {
        String sql = "show warnings";

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
            assert domain1.getShowType() == MyShowType.WARNINGS && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showWarnings_2() {
        String sql = "show count(*) warnings";

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
            assert domain2.getShowType() == MyShowType.WARNINGS && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showErrors_1() {
        String sql = "show errors";

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
            assert domain1.getShowType() == MyShowType.ERRORS && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showErrors_2() {
        String sql = "show count(*) errors";

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
            assert domain2.getShowType() == MyShowType.ERRORS && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showVariables_1() {
        String sql = "show variables";

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
        String sql = "show global variables";

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

    @Test
    public void showVariables_3() {
        String sql = "show session variables";

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
            assert domain3.getShowType() == MyShowType.VARIABLES && domain3.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showVariables_4() {
        String sql = "show variables like 'abc%'";

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
            MyShowDomain domain4 = (MyShowDomain) domainList.get(0);
            assert domain4.getSqlType() == SecQueryType.SHOW && domain4.getAuditKind() == SecQueryKind.QUERY;
            assert domain4.getShowType() == MyShowType.VARIABLES && domain4.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showTables_1() {
        String sql = "show tables";

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
            assert domain1.getShowType() == MyShowType.TABLES && domain1.getTarget() == TargetType.Table;
            assert domain1.getCatalog() == null && domain1.getSchema() == null;
        }
    }

    @Test
    public void showTables_2() {
        String sql = "show tables in test_db";

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
            assert domain2.getShowType() == MyShowType.TABLES && domain2.getTarget() == TargetType.Table;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db");
        }
    }

    @Test
    public void showTables_3() {
        String sql = "show full tables in test_db2";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db2/");
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
            assert domain4.getShowType() == MyShowType.TABLES && domain4.getTarget() == TargetType.Table;
            assert domain4.getCatalog() == null && domain4.getSchema().equals("test_db2");
        }
    }

    @Test
    public void showTables_4() {
        String sql = "show full tables from test_db2";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db2/");
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
            assert domain5.getShowType() == MyShowType.TABLES && domain5.getTarget() == TargetType.Table;
            assert domain5.getCatalog() == null && domain5.getSchema().equals("test_db2");
        }
    }

    @Test
    public void showTables_5() {
        String sql = "show full tables from test_db2 where Table_type = 'BASE TABLE'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db2/");
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
            assert domain6.getShowType() == MyShowType.TABLES && domain6.getTarget() == TargetType.Table;
            assert domain6.getCatalog() == null && domain6.getSchema().equals("test_db2");
        }
    }

    @Test
    public void showTables_6() {
        String sql = "show full tables from test_db2 like 'BASE TABLE'";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Schema &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db2/");
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
            assert domain7.getShowType() == MyShowType.TABLES && domain7.getTarget() == TargetType.Table;
            assert domain7.getCatalog() == null && domain7.getSchema().equals("test_db2");
        }
    }

    @Test
    public void showEngines_1() {
        String sql = "show engines";

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
            assert domain1.getShowType() == MyShowType.ENGINES && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showEngines_2() {
        String sql = "show storage engines";

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
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.ENGINES && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showEngine_1() {
        String sql = "show engine xxx status";

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
            assert domain1.getShowType() == MyShowType.ENGINE && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showEngine_2() {
        String sql = "show engine xxx mutex";

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
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.ENGINE && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showIndexes_1() {
        String sql = "show indexes from test_table from test_db";

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
        String sql = "show indexes from test_table";

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
    public void showFuncCode_1() {
        String sql = "show function code test_func";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_func/");
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
            assert domain1.getShowType() == MyShowType.FUNCTION_CODE && domain1.getTarget() == TargetType.Function;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getFunc().equals("test_func");
        }
    }

    @Test
    public void showFuncCode_2() {
        String sql = "show function code test_db.test_func";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Function &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_func/");
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
            assert domain2.getShowType() == MyShowType.FUNCTION_CODE && domain2.getTarget() == TargetType.Function;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db") && domain2.getFunc().equals("test_func");
        }
    }

    @Test
    public void showFuncStatus_1() {
        String sql = "show function status";

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
            assert domain1.getShowType() == MyShowType.FUNCTION_STATUS && domain1.getTarget() == TargetType.Function;
        }
    }

    @Test
    public void showFuncStatus_2() {
        String sql = "show function status like 'abc%'";

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
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.FUNCTION_STATUS && domain2.getTarget() == TargetType.Function;
        }
    }

    @Test
    public void showProcCode_1() {
        String sql = "show procedure code test_proc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_proc/");
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
            assert domain1.getShowType() == MyShowType.PROCEDURE_CODE && domain1.getTarget() == TargetType.Procedure;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getProc().equals("test_proc");
        }
    }

    @Test
    public void showProcCode_2() {
        String sql = "show procedure code test_db.test_proc";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Procedure &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_proc/");
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
            assert domain2.getShowType() == MyShowType.PROCEDURE_CODE && domain2.getTarget() == TargetType.Procedure;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db") && domain2.getProc().equals("test_proc");
        }
    }

    @Test
    public void showProcStatus_1() {
        String sql = "show procedure status";

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
            assert domain1.getShowType() == MyShowType.PROCEDURE_STATUS && domain1.getTarget() == TargetType.Procedure;
        }
    }

    @Test
    public void showProcStatus_2() {
        String sql = "show procedure status like 'abc%'";

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
            MyShowDomain domain2 = (MyShowDomain) domainList.get(0);
            assert domain2.getSqlType() == SecQueryType.SHOW && domain2.getAuditKind() == SecQueryKind.QUERY;
            assert domain2.getShowType() == MyShowType.PROCEDURE_STATUS && domain2.getTarget() == TargetType.Procedure;
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
        String sql = "show table status in test_db";

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
    public void showStatus_1() {
        String sql = "show status";

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
        String sql = "show global status";

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

    @Test
    public void showMasterStatus_1() {
        String sql = "show master status";

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
            assert domain1.getShowType() == MyShowType.MASTER_STATUS && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showReplicaStatus_1() {
        //todo can't parse
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show replica status");
        //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
        //        assert domain1.getShowType() == MyShowType.REPLICA_STATUS && domain1.getTarget() == TargetType.Environment;
        //
        //        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show replica status for channel 'channel_1'");
        //        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
        //        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
        //        assert domain2.getShowType() == MyShowType.REPLICA_STATUS && domain2.getTarget() == TargetType.Environment;
    }

    @Test
    public void showReplicas_1() {
        //            List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show replicas");
        //            MyShowDomain domain1 = (MyShowDomain) list1.get(0);
        //            assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
        //            assert domain1.getShowType() == MyShowType.REPLICAS && domain1.getTarget() == TargetType.Environment;
    }

    @Test
    public void showRelaylogEvents_1() {
        String sql = "show relaylog events";

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
            assert domain1.getShowType() == MyShowType.RELAYLOG_EVENTS && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showRelaylogEvents_2() {
        String sql = "show relaylog events in 'binlog.0001'";

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
            assert domain2.getShowType() == MyShowType.RELAYLOG_EVENTS && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showProcessList_1() {
        String sql = "show processlist";

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
            assert domain1.getShowType() == MyShowType.PROCESSLIST && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showProcessList_2() {
        String sql = "show full processlist";

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
            assert domain2.getShowType() == MyShowType.PROCESSLIST && domain2.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showProfile_1() {
        String sql = "show profile";

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
            assert domain1.getShowType() == MyShowType.PROFILE && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showProfile_2() {
        String sql = "show profile cpu";

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
            assert domain1.getShowType() == MyShowType.PROFILE && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showProfiles_1() {
        String sql = "show profiles";

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
            assert domain1.getShowType() == MyShowType.PROFILES && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showOpenTables_1() {
        String sql = "show open tables";

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
            assert domain1.getShowType() == MyShowType.OPEN_TABLES && domain1.getTarget() == TargetType.Table;
        }
    }

    @Test
    public void showOpenTables_2() {
        String sql = "show open tables in test_db";

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
            assert domain2.getShowType() == MyShowType.OPEN_TABLES && domain2.getTarget() == TargetType.Table;
            assert domain2.getCatalog() == null && domain2.getSchema().equals("test_db");
        }
    }

    @Test
    public void showParseTree_1() {
        //todo can't parse
        //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show parse_tree select * from t3 where o_id > 2");
        //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
        //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
        //        assert domain1.getShowType() == MyShowType.PARSE_TREE && domain1.getTarget() == TargetType.Query;
    }

    @Test
    public void showPlugins_1() {
        String sql = "show plugins";

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
            assert domain1.getShowType() == MyShowType.PLUGINS && domain1.getTarget() == TargetType.Environment;
        }
    }

    @Test
    public void showPrivileges_1() {
        String sql = "show privileges";

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
            assert domain1.getShowType() == MyShowType.PRIVILEGES && domain1.getTarget() == TargetType.Environment;
        }
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
        String sql = "show grants for 'jeffrey'@'localhost'";

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
            assert domain3.getCatalog() == null && domain3.getUserOrRole().equals("'jeffrey'@'localhost'");
        }
    }

    //@Test
    public void showGrants_4() {
        // todo can't parse
        //        List<RuleDomain> list4 = resolveSpi.resolveDomain(dataSourceType, "show grants for root using 'r1'");
        //        MyShowDomain domain4 = (MyShowDomain) list4.get(0);
        //        assert domain4.getSqlType() == SecQueryType.MYSQL_SHOW && domain4.getSqlKind() == SqlKind.QUERY;
        //        assert domain4.getShowType() == MyShowType.GRANTS && domain4.getTarget() == TargetType.UserOrRole;
        //        assert domain4.getCatalog()== null && domain4.getUserOrRole().equals("user1");
    }
}
