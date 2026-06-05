//package com.clougence.clouddm.dsfamily.postgres.secdomain;
//
//import java.util.List;
//
//import org.junit.Test;
//
//import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainResolveSpi;
//import com.clougence.clouddm.sdk.model.analysis.TargetType;
//import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
//import com.clougence.clouddm.sdk.security.auth.SecQueryType;
//import com.clougence.clouddm.sdk.model.analysis.secrules.SqlKind;
//import com.clougence.clouddm.sdk.model.analysis.secrules.rdb.mysql.MyShowDomain;
//import com.clougence.clouddm.sdk.model.analysis.secrules.rdb.mysql.MyShowType;
//import com.clougence.clouddm.base.metadata.ds.DataSourceType;
//
//// https://dev.mysql.com/doc/refman/8.4/en/show.html
//public class PgSecDomainResolve4ShowTest {
//
//    private PgSecDomainResolveSpi resolveSpi     = new PgSecDomainResolveSpi();
//    private DataSourceType        dataSourceType = DataSourceType.PostgreSQL;
//    // todo can't parse
//    //    @Test
//    //    public void showBinaryLogStatus_1() {
//    //        List<RuleDomain> list = resolveSpi.resolveDomain(dataSourceType, "show binary log status");
//    //        MyShowDomain domain = (MyShowDomain) list.get(0);
//    //        assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getSqlKind() == SqlKind.CREATE;
//    //        assert domain.getCatalog().equals("def") && domain.getSchema() == null && domain.getTable().equals("abc") && domain.getOptions().isEmpty();
//    //    }
//
//    @Test
//    public void showBinaryLogs_1() {
//        List<RuleDomain> list = resolveSpi.resolveDomain(dataSourceType, "show binary logs");
//        MyShowDomain domain = (MyShowDomain) list.get(0);
//        assert domain.getSqlType() == SecQueryType.MYSQL_SHOW && domain.getSqlKind() == SqlKind.QUERY;
//        assert domain.getShowType() == MyShowType.BINARY_LOGS && domain.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showBinaryEvents_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show binlog events");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.BINLOG_EVENTS && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show binlog events in 'log_name'");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.BINLOG_EVENTS && domain2.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showCharacterSet_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show character set");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.CHARACTER_SET && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show character set like 'abc%'");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.CHARACTER_SET && domain2.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show charset");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.CHARACTER_SET && domain3.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list4 = resolveSpi.resolveDomain(dataSourceType, "show charset like 'abc%'");
//        MyShowDomain domain4 = (MyShowDomain) list4.get(0);
//        assert domain4.getSqlType() == SecQueryType.MYSQL_SHOW && domain4.getSqlKind() == SqlKind.QUERY;
//        assert domain4.getShowType() == MyShowType.CHARACTER_SET && domain4.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showCollation_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show collation");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.COLLATION && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show collation like 'abc%'");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.COLLATION && domain2.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showColumns_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show columns from test_table from test_db");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.COLUMNS && domain1.getTarget() == TargetType.Column;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db") && domain1.getTable().equals("test_table");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show columns from test_db.test_table");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.COLUMNS && domain2.getTarget() == TargetType.Column;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db") && domain2.getTable().equals("test_table");
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show columns from test_table");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.COLUMNS && domain3.getTarget() == TargetType.Column;
//        assert domain3.getCatalog().equals("def") && domain3.getSchema() == null && domain3.getTable().equals("test_table");
//
//        List<RuleDomain> list4 = resolveSpi.resolveDomain(dataSourceType, "show full columns from test_table from test_db like 'abc%'");
//        MyShowDomain domain4 = (MyShowDomain) list4.get(0);
//        assert domain4.getSqlType() == SecQueryType.MYSQL_SHOW && domain4.getSqlKind() == SqlKind.QUERY;
//        assert domain4.getShowType() == MyShowType.COLUMNS && domain4.getTarget() == TargetType.Column;
//        assert domain4.getCatalog().equals("def") && domain4.getSchema().equals("test_db") && domain4.getTable().equals("test_table");
//    }
//
//    @Test
//    public void desc_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "desc test_table");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.COLUMNS && domain1.getTarget() == TargetType.Column;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null && domain1.getTable().equals("test_table");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "desc test_db.test_table");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.COLUMNS && domain2.getTarget() == TargetType.Column;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db") && domain2.getTable().equals("test_table");
//    }
//
//    @Test
//    public void showCreateDatabase_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create database test_db1.test_db2");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.CREATE_DATABASE && domain1.getTarget() == TargetType.Schema;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db1") && domain1.getTable() == null;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show create database test_db1");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.CREATE_DATABASE && domain2.getTarget() == TargetType.Schema;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db1") && domain2.getTable() == null;
//    }
//
//    @Test
//    public void showCreateEvent_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create event test_db.test_event");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.CREATE_EVENT && domain1.getTarget() == TargetType.Event;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db") && domain1.getEvent().equals("test_event");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show create event test_event");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.CREATE_EVENT && domain2.getTarget() == TargetType.Event;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema() == null && domain2.getEvent().equals("test_event");
//    }
//
//    @Test
//    public void showCreateFunction_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create function test_db.test_func");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.CREATE_FUNCTION && domain1.getTarget() == TargetType.Function;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db") && domain1.getEvent().equals("test_func");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show create function test_func");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.CREATE_FUNCTION && domain2.getTarget() == TargetType.Function;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema() == null && domain2.getEvent().equals("test_func");
//    }
//
//    @Test
//    public void showCreateProcedure_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create procedure test_db.test_proc");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.CREATE_PROCEDURE && domain1.getTarget() == TargetType.Procedure;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db") && domain1.getProc().equals("test_proc");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show create procedure test_proc");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.CREATE_PROCEDURE && domain2.getTarget() == TargetType.Procedure;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema() == null && domain2.getProc().equals("test_proc");
//    }
//
//    @Test
//    public void showCreateTable_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create table test_db.test_table");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.CREATE_TABLE && domain1.getTarget() == TargetType.Table;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db") && domain1.getTable().equals("test_table");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show create table test_table");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.CREATE_TABLE && domain2.getTarget() == TargetType.Table;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema() == null && domain2.getTable().equals("test_table");
//    }
//
//    @Test
//    public void showCreateView_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create view test_db.test_view");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.CREATE_VIEW && domain1.getTarget() == TargetType.View;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db") && domain1.getView().equals("test_view");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show create view test_view");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.CREATE_VIEW && domain2.getTarget() == TargetType.View;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema() == null && domain2.getView().equals("test_view");
//    }
//
//    @Test
//    public void showCreateTrigger_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create trigger test_db.test_trigger");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.CREATE_TRIGGER && domain1.getTarget() == TargetType.Trigger;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db") && domain1.getTrigger().equals("test_trigger");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show create trigger test_trigger");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.CREATE_TRIGGER && domain2.getTarget() == TargetType.Trigger;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema() == null && domain2.getTrigger().equals("test_trigger");
//    }
//    // todo can't parse
//    //    @Test
//    //    public void showCreateUser_1() {
//    //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show create user user1");
//    //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//    //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//    //        assert domain1.getShowType() == MyShowType.CREATE_USER && domain1.getTarget() == TargetType.UserOrRole;
//    //        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null && domain1.getUserOrRole().equals("user1");
//    //    }
//
//    @Test
//    public void showDatabases_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show databases");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.DATABASES && domain1.getTarget() == TargetType.Schema;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show schemas");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.DATABASES && domain2.getTarget() == TargetType.Schema;
//    }
//
//    @Test
//    public void showTriggers_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show triggers");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.TRIGGERS && domain1.getTarget() == TargetType.Trigger;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show triggers in test_db");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.TRIGGERS && domain2.getTarget() == TargetType.Trigger;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db");
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show triggers in test_db1.test_db2");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.TRIGGERS && domain3.getTarget() == TargetType.Trigger;
//        assert domain3.getCatalog().equals("def") && domain3.getSchema().equals("test_db1");
//    }
//
//    @Test
//    public void showEvents_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show events");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.EVENTS && domain1.getTarget() == TargetType.Event;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show events in test_db");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.EVENTS && domain2.getTarget() == TargetType.Event;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db");
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show events in test_db1.test_db2");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.EVENTS && domain3.getTarget() == TargetType.Event;
//        assert domain3.getCatalog().equals("def") && domain3.getSchema().equals("test_db1");
//    }
//
//    @Test
//    public void showWarnings_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show warnings");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.WARNINGS && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show count(*) warnings");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.WARNINGS && domain2.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showErrors_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show errors");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.ERRORS && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show count(*) errors");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.ERRORS && domain2.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showVariables_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show variables");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.VARIABLES && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show global variables");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.VARIABLES && domain2.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show session variables");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.VARIABLES && domain3.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list4 = resolveSpi.resolveDomain(dataSourceType, "show variables like 'abc%'");
//        MyShowDomain domain4 = (MyShowDomain) list4.get(0);
//        assert domain4.getSqlType() == SecQueryType.MYSQL_SHOW && domain4.getSqlKind() == SqlKind.QUERY;
//        assert domain4.getShowType() == MyShowType.VARIABLES && domain4.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showTables_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show tables");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.TABLES && domain1.getTarget() == TargetType.Table;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show tables in test_db");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.TABLES && domain2.getTarget() == TargetType.Table;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db");
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show tables in test_db1.test_db2");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.TABLES && domain3.getTarget() == TargetType.Table;
//        assert domain3.getCatalog().equals("def") && domain3.getSchema().equals("test_db1");
//
//        List<RuleDomain> list4 = resolveSpi.resolveDomain(dataSourceType, "show full tables in test_db1.test_db2");
//        MyShowDomain domain4 = (MyShowDomain) list4.get(0);
//        assert domain4.getSqlType() == SecQueryType.MYSQL_SHOW && domain4.getSqlKind() == SqlKind.QUERY;
//        assert domain4.getShowType() == MyShowType.TABLES && domain4.getTarget() == TargetType.Table;
//        assert domain4.getCatalog().equals("def") && domain4.getSchema().equals("test_db1");
//
//        List<RuleDomain> list5 = resolveSpi.resolveDomain(dataSourceType, "show full tables from test_db1.test_db2");
//        MyShowDomain domain5 = (MyShowDomain) list5.get(0);
//        assert domain5.getSqlType() == SecQueryType.MYSQL_SHOW && domain5.getSqlKind() == SqlKind.QUERY;
//        assert domain5.getShowType() == MyShowType.TABLES && domain5.getTarget() == TargetType.Table;
//        assert domain5.getCatalog().equals("def") && domain5.getSchema().equals("test_db1");
//
//        List<RuleDomain> list6 = resolveSpi.resolveDomain(dataSourceType, "show full tables from test_db1.test_db2 where Table_type = 'BASE TABLE'");
//        MyShowDomain domain6 = (MyShowDomain) list6.get(0);
//        assert domain6.getSqlType() == SecQueryType.MYSQL_SHOW && domain6.getSqlKind() == SqlKind.QUERY;
//        assert domain6.getShowType() == MyShowType.TABLES && domain6.getTarget() == TargetType.Table;
//        assert domain6.getCatalog().equals("def") && domain6.getSchema().equals("test_db1");
//
//        List<RuleDomain> list7 = resolveSpi.resolveDomain(dataSourceType, "show full tables from test_db1.test_db2 like 'BASE TABLE'");
//        MyShowDomain domain7 = (MyShowDomain) list7.get(0);
//        assert domain7.getSqlType() == SecQueryType.MYSQL_SHOW && domain7.getSqlKind() == SqlKind.QUERY;
//        assert domain7.getShowType() == MyShowType.TABLES && domain7.getTarget() == TargetType.Table;
//        assert domain7.getCatalog().equals("def") && domain7.getSchema().equals("test_db1");
//    }
//
//    @Test
//    public void showEngines_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show engines");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.ENGINES && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show storage engines");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.ENGINES && domain2.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showEngine_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show engine xxx status");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.ENGINE && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show engine xxx mutex");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.ENGINE && domain2.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showIndexes_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show indexes from test_table from test_db");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.INDEX && domain1.getTarget() == TargetType.Index;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema().equals("test_db") && domain1.getTable().equals("test_table");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show indexes from test_db.test_table");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.INDEX && domain2.getTarget() == TargetType.Index;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db") && domain2.getTable().equals("test_table");
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show indexes from test_table");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.INDEX && domain3.getTarget() == TargetType.Index;
//        assert domain3.getCatalog().equals("def") && domain3.getSchema() == null && domain3.getTable().equals("test_table");
//    }
//
//    @Test
//    public void showFuncCode_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show function code test_func");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.FUNCTION_CODE && domain1.getTarget() == TargetType.Function;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null && domain1.getFunc().equals("test_func");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show function code test_db.test_func");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.FUNCTION_CODE && domain2.getTarget() == TargetType.Function;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db") && domain2.getFunc().equals("test_func");
//    }
//
//    @Test
//    public void showFuncStatus_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show function status");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.FUNCTION_STATUS && domain1.getTarget() == TargetType.Function;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show function status like 'abc%'");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.FUNCTION_STATUS && domain2.getTarget() == TargetType.Function;
//    }
//
//    @Test
//    public void showProcCode_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show procedure code test_proc");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.PROCEDURE_CODE && domain1.getTarget() == TargetType.Procedure;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null && domain1.getProc().equals("test_proc");
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show procedure code test_db.test_proc");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.PROCEDURE_CODE && domain2.getTarget() == TargetType.Procedure;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db") && domain2.getProc().equals("test_proc");
//    }
//
//    @Test
//    public void showProcStatus_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show procedure status");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.PROCEDURE_STATUS && domain1.getTarget() == TargetType.Procedure;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show procedure status like 'abc%'");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.PROCEDURE_STATUS && domain2.getTarget() == TargetType.Procedure;
//    }
//
//    @Test
//    public void showTableStatus_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show table status");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.TABLE_STATUS && domain1.getTarget() == TargetType.Table;
//        assert domain1.getCatalog().equals("def") && domain1.getSchema() == null;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show table status in test_db");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.TABLE_STATUS && domain2.getTarget() == TargetType.Table;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db");
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show table status in test_db1.test_db2");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.TABLE_STATUS && domain3.getTarget() == TargetType.Table;
//        assert domain3.getCatalog().equals("def") && domain3.getSchema().equals("test_db1");
//    }
//
//    @Test
//    public void showStatus_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show status");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.STATUS && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show global status");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.STATUS && domain2.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show session status");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.STATUS && domain3.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showMasterStatus_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show master status");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.MASTER_STATUS && domain1.getTarget() == TargetType.Environment;
//    }
//    //todo can't parse
//    //    @Test
//    //    public void showReplicaStatus_1() {
//    //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show replica status");
//    //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//    //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//    //        assert domain1.getShowType() == MyShowType.REPLICA_STATUS && domain1.getTarget() == TargetType.Environment;
//    //
//    //        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show replica status for channel 'channel_1'");
//    //        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//    //        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//    //        assert domain2.getShowType() == MyShowType.REPLICA_STATUS && domain2.getTarget() == TargetType.Environment;
//    //    }
//    //
//    //    @Test
//    //    public void showReplicas_1() {
//    //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show replicas");
//    //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//    //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//    //        assert domain1.getShowType() == MyShowType.REPLICAS && domain1.getTarget() == TargetType.Environment;
//    //    }
//
//    @Test
//    public void showRelaylogEvents_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show relaylog events");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.RELAYLOG_EVENTS && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show relaylog events in 'binlog.0001'");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.RELAYLOG_EVENTS && domain2.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showProcessList_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show processlist");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.PROCESSLIST && domain1.getTarget() == TargetType.Environment;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show full processlist");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.PROCESSLIST && domain2.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showProfile_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show profile");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.PROFILE && domain1.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showProfiles_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show profiles");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.PROFILES && domain1.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showOpenTables_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show open tables");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.OPEN_TABLES && domain1.getTarget() == TargetType.Table;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show open tables in test_db");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.OPEN_TABLES && domain2.getTarget() == TargetType.Table;
//        assert domain2.getCatalog().equals("def") && domain2.getSchema().equals("test_db");
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show open tables in test_db1.test_db2");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.OPEN_TABLES && domain3.getTarget() == TargetType.Table;
//        assert domain3.getCatalog().equals("def") && domain3.getSchema().equals("test_db1");
//    }
//    //todo can't parse
//    //    @Test
//    //    public void showParseTree_1() {
//    //        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show parse_tree select * from t3 where o_id > 2");
//    //        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//    //        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//    //        assert domain1.getShowType() == MyShowType.PARSE_TREE && domain1.getTarget() == TargetType.Query;
//    //    }
//
//    @Test
//    public void showPlugins_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show plugins");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.PLUGINS && domain1.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showPrivileges_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show privileges");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.PRIVILEGES && domain1.getTarget() == TargetType.Environment;
//    }
//
//    @Test
//    public void showGrants_1() {
//        List<RuleDomain> list1 = resolveSpi.resolveDomain(dataSourceType, "show grants");
//        MyShowDomain domain1 = (MyShowDomain) list1.get(0);
//        assert domain1.getSqlType() == SecQueryType.MYSQL_SHOW && domain1.getSqlKind() == SqlKind.QUERY;
//        assert domain1.getShowType() == MyShowType.GRANTS && domain1.getTarget() == TargetType.UserOrRole;
//        assert domain1.getCatalog().equals("def") && domain1.getUserOrRole() == null;
//
//        List<RuleDomain> list2 = resolveSpi.resolveDomain(dataSourceType, "show grants for user1");
//        MyShowDomain domain2 = (MyShowDomain) list2.get(0);
//        assert domain2.getSqlType() == SecQueryType.MYSQL_SHOW && domain2.getSqlKind() == SqlKind.QUERY;
//        assert domain2.getShowType() == MyShowType.GRANTS && domain2.getTarget() == TargetType.UserOrRole;
//        assert domain2.getCatalog().equals("def") && domain2.getUserOrRole().equals("user1");
//
//        List<RuleDomain> list3 = resolveSpi.resolveDomain(dataSourceType, "show grants for 'jeffrey'@'localhost'");
//        MyShowDomain domain3 = (MyShowDomain) list3.get(0);
//        assert domain3.getSqlType() == SecQueryType.MYSQL_SHOW && domain3.getSqlKind() == SqlKind.QUERY;
//        assert domain3.getShowType() == MyShowType.GRANTS && domain3.getTarget() == TargetType.UserOrRole;
//        assert domain3.getCatalog().equals("def") && domain3.getUserOrRole().equals("'jeffrey'@'localhost'");
//
//        // todo can't parse
//        //        List<RuleDomain> list4 = resolveSpi.resolveDomain(dataSourceType, "show grants for root using 'r1'");
//        //        MyShowDomain domain4 = (MyShowDomain) list4.get(0);
//        //        assert domain4.getSqlType() == SecQueryType.MYSQL_SHOW && domain4.getSqlKind() == SqlKind.QUERY;
//        //        assert domain4.getShowType() == MyShowType.GRANTS && domain4.getTarget() == TargetType.UserOrRole;
//        //        assert domain4.getCatalog().equals("def") && domain4.getUserOrRole().equals("user1");
//    }
//}
