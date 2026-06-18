package com.clougence.clouddm.ds.rules.rdb.using_ms.column;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ms.MsAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MsRuleColumnTableNameCaseTypeTest extends MsAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-name-case-type.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("caseType", "Upper case");
    private final Map<String, String> p2             = CollectionUtils.asMap("caseType", "Lower case");
    private final Map<String, String> p3             = CollectionUtils.asMap("caseType", "Lower camel case");
    private final Map<String, String> p4             = CollectionUtils.asMap("caseType", "Upper camel case");

    @Test
    public void columnTableNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "alter table test_table alter column abc_test int;";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test_table(abc_test int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table add abc_test int;";
        assert !runScript(scriptResource, sql, p1);
//        sql = "alter table test_table rename column c to abc_test;";
//        assert !runScript(scriptResource, sql, p1);
        sql = "create table test_table(ABC_TEST int);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test_table add ABC_TEST int;";
        assert runScript(scriptResource, sql, p1);
//        sql = "alter table test_table rename column c to ABC_TEST;";
//        assert runScript(scriptResource, sql, p1);
        sql = "create table test_table(abcTest int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table add abcTest int;";
        assert !runScript(scriptResource, sql, p1);
//        sql = "alter table test_table rename column c to abcTest;";
//        assert !runScript(scriptResource, sql, p1);
        sql = "create table test_table(AbcTest int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table add AbcTest int;";
        assert !runScript(scriptResource, sql, p1);
//        sql = "alter table test_table rename column c to AbcTest;";
//        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnTableNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "alter table test_table alter column abc_test int;";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test_table(abc_test int);";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test_table add abc_test int;";
        assert runScript(scriptResource, sql, p2);
//        sql = "alter table test_table rename column c to abc_test;";
//        assert runScript(scriptResource, sql, p2);
        sql = "create table test_table(ABC_TEST int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table add ABC_TEST int;";
        assert !runScript(scriptResource, sql, p2);
//        sql = "alter table test_table rename column c to ABC_TEST;";
//        assert !runScript(scriptResource, sql, p2);
        sql = "create table test_table(abcTest int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table add abcTest int;";
        assert !runScript(scriptResource, sql, p2);
//        sql = "alter table test_table rename column c to abcTest;";
//        assert !runScript(scriptResource, sql, p2);
        sql = "create table test_table(AbcTest int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table add AbcTest int;";
        assert !runScript(scriptResource, sql, p2);
//        sql = "alter table test_table rename column c to AbcTest;";
//        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void columnTableNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "alter table test_table alter column abc_test int;";
        assert runScript(scriptResource, sql, p3);
        sql = "create table test_table(abc_test int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table add abc_test int;";
        assert !runScript(scriptResource, sql, p3);
//        sql = "alter table test_table rename column c to abc_test;";
//        assert !runScript(scriptResource, sql, p3);
        sql = "create table test_table(ABC_TEST int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table add ABC_TEST int;";
        assert !runScript(scriptResource, sql, p3);
//        sql = "alter table test_table rename column c to ABC_TEST;";
//        assert !runScript(scriptResource, sql, p3);
        sql = "create table test_table(abcTest int);";
        assert runScript(scriptResource, sql, p3);
        sql = "alter table test_table add abcTest int;";
        assert runScript(scriptResource, sql, p3);
//        sql = "alter table test_table rename column c to abcTest;";
//        assert runScript(scriptResource, sql, p3);
        sql = "create table test_table(AbcTest int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table add AbcTest int;";
        assert !runScript(scriptResource, sql, p3);
//        sql = "alter table test_table rename column c to AbcTest;";
//        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void columnTableNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "alter table test_table alter column abc_test int;";
        assert runScript(scriptResource, sql, p4);
        sql = "create table test_table(abc_test int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table add abc_test int;";
        assert !runScript(scriptResource, sql, p4);
//        sql = "alter table test_table rename column c to abc_test;";
//        assert !runScript(scriptResource, sql, p4);
        sql = "create table test_table(ABC_TEST int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table add ABC_TEST int;";
        assert !runScript(scriptResource, sql, p4);
//        sql = "alter table test_table rename column c to ABC_TEST;";
//        assert !runScript(scriptResource, sql, p4);
        sql = "create table test_table(abcTest int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table add abcTest int;";
        assert !runScript(scriptResource, sql, p4);
//        sql = "alter table test_table rename column c to abcTest;";
//        assert !runScript(scriptResource, sql, p4);
        sql = "create table test_table(AbcTest int);";
        assert runScript(scriptResource, sql, p4);
        sql = "alter table test_table add AbcTest int;";
        assert runScript(scriptResource, sql, p4);
//        sql = "alter table test_table rename column c to AbcTest;";
//        assert runScript(scriptResource, sql, p4);
    }
}
