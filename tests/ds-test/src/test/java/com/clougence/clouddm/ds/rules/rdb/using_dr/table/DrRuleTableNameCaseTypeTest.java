package com.clougence.clouddm.ds.rules.rdb.using_dr.table;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_dr.DrAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class DrRuleTableNameCaseTypeTest extends DrAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-name-case-type.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("caseType", "Upper case");
    private final Map<String, String> p2             = CollectionUtils.asMap("caseType", "Lower case");
    private final Map<String, String> p3             = CollectionUtils.asMap("caseType", "Lower camel case");
    private final Map<String, String> p4             = CollectionUtils.asMap("caseType", "Upper camel case");

    @Test
    public void createTableNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "create table test.abc_test(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.ABC_TEST(id int(4) key auto_increment, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abcTest(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.AbcTest(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void createTableNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "create table test.abc_test(id int(4) key auto_increment, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.ABC_TEST(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test.abcTest(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test.AbcTest(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void createTableNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "create table test.abc_test(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p3);
        sql = "create table test.ABC_TEST(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p3);
        sql = "create table test.abcTest(id int(4) key auto_increment, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p3);
        sql = "create table test.AbcTest(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void createTableNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "create table test.abc_test(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test.ABC_TEST(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test.abcTest(id int(4) key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test.AbcTest(id int(4) key auto_increment, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p4);
    }

    @Test
    public void renameTableNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "alter table test.ABC_TEST rename abc_test";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.ABC_TEST rename ABC_TEST";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.ABC_TEST rename abcTest";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.ABC_TEST rename AbcTest";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void renameTableNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "alter table test.ABC_TEST rename abc_test";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.ABC_TEST rename ABC_TEST";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test.ABC_TEST rename abcTest";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test.ABC_TEST rename AbcTest";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void renameTableNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "alter table test.ABC_TEST rename abc_test";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test.ABC_TEST rename ABC_TEST";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test.ABC_TEST rename abcTest";
        assert runScript(scriptResource, sql, p3);
        sql = "alter table test.ABC_TEST rename AbcTest";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void renameTableNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "alter table test.ABC_TEST rename abc_test";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test.ABC_TEST rename ABC_TEST";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test.ABC_TEST rename abcTest";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test.ABC_TEST rename AbcTest";
        assert runScript(scriptResource, sql, p4);
    }

    @Test
    public void alertTableNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        //sql = "alter table test.abc comment 'abc' engine MyISAM, rename abc_test";
        //domain = DomainHelper.create(tableDomainSql(sql));
        //assert runScript(scriptResource, sql, p1);
        //assert !obj;
        sql = "alter table test.ABC_TEST rename ABC_TEST";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.ABC_TEST rename abcTest";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.ABC_TEST rename AbcTest";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void alertTableNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        //        sql = "alter table test.abc comment 'abc' engine MyISAM, rename abc_test";
        //        //        assert runScript(scriptResource, sql, p2);
        //        assert obj;
        sql = "alter table test.ABC_TEST rename ABC_TEST";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test.ABC_TEST rename abcTest";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test.ABC_TEST rename AbcTest";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void alertTableNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        //        sql = "alter table test.abc comment 'abc' engine MyISAM, rename abc_test";
        //        //        assert runScript(scriptResource, sql, p3);
        //        assert !obj;
        sql = "alter table test.ABC_TEST rename ABC_TEST";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test.ABC_TEST rename abcTest";
        assert runScript(scriptResource, sql, p3);
        sql = "alter table test.ABC_TEST rename AbcTest";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void alertTableNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        //        sql = "alter table test.abc comment 'abc' engine MyISAM, rename abc_test";
        //        //        assert runScript(scriptResource, sql, p4);
        //        assert !obj;
        sql = "alter table test.ABC_TEST rename ABC_TEST";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test.ABC_TEST rename abcTest";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test.ABC_TEST rename AbcTest";
        assert runScript(scriptResource, sql, p4);
    }
}
