package com.clougence.clouddm.ds.rules.rdb.using_db2.table;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleTableNameCaseTypeTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-name-case-type.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("caseType", "Upper case");
    private final Map<String, String> p2             = CollectionUtils.asMap("caseType", "Lower case");
    private final Map<String, String> p3             = CollectionUtils.asMap("caseType", "Lower camel case");
    private final Map<String, String> p4             = CollectionUtils.asMap("caseType", "Upper camel case");

    @Test
    public void createTableNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "create table test.abc_test(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.ABC_TEST(id int not null primary key, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abcTest(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.AbcTest(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void createTableNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "create table test.abc_test(id int not null primary key, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.ABC_TEST(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test.abcTest(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test.AbcTest(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void createTableNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "create table test.abc_test(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p3);
        sql = "create table test.ABC_TEST(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p3);
        sql = "create table test.abcTest(id int not null primary key, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p3);
        sql = "create table test.AbcTest(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void createTableNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "create table test.abc_test(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test.ABC_TEST(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test.abcTest(id int not null primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test.AbcTest(id int not null primary key, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p4);
    }

    @Test
    public void renameTableNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "rename table test.ABC_TEST to abc_test";
        assert !runScript(scriptResource, sql, p1);
        sql = "rename table test.ABC_TEST to ABC_TEST";
        assert runScript(scriptResource, sql, p1);
        sql = "rename table test.ABC_TEST to abcTest";
        assert !runScript(scriptResource, sql, p1);
        sql = "rename table test.ABC_TEST to AbcTest";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void renameTableNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "rename table test.ABC_TEST to abc_test";
        assert runScript(scriptResource, sql, p2);
        sql = "rename table test.ABC_TEST to ABC_TEST";
        assert !runScript(scriptResource, sql, p2);
        sql = "rename table test.ABC_TEST to abcTest";
        assert !runScript(scriptResource, sql, p2);
        sql = "rename table test.ABC_TEST to AbcTest";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void renameTableNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "rename table test.ABC_TEST to abc_test";
        assert !runScript(scriptResource, sql, p3);
        sql = "rename table test.ABC_TEST to ABC_TEST";
        assert !runScript(scriptResource, sql, p3);
        sql = "rename table test.ABC_TEST to abcTest";
        assert runScript(scriptResource, sql, p3);
        sql = "rename table test.ABC_TEST to AbcTest";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void renameTableNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "rename table test.ABC_TEST to abc_test";
        assert !runScript(scriptResource, sql, p4);
        sql = "rename table test.ABC_TEST to ABC_TEST";
        assert !runScript(scriptResource, sql, p4);
        sql = "rename table test.ABC_TEST to abcTest";
        assert !runScript(scriptResource, sql, p4);
        sql = "rename table test.ABC_TEST to AbcTest";
        assert runScript(scriptResource, sql, p4);
    }
}
