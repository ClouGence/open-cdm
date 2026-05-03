package com.clougence.clouddm.ds.rules.rdb.using_mc.column;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_mc.McAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class McRuleColumnTableNameCaseTypeTest extends McAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-name-case-type.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("caseType", "Upper case");
    private final Map<String, String> p2             = CollectionUtils.asMap("caseType", "Lower case");
    private final Map<String, String> p3             = CollectionUtils.asMap("caseType", "Lower camel case");
    private final Map<String, String> p4             = CollectionUtils.asMap("caseType", "Upper camel case");

    @Test
    public void columnTableNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "alter table test_table change daw abc_test int;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test_table(abc_test int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table add columns ( abc_test int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table change c abc_test int;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test_table(ABC_TEST int);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test_table add columns ( ABC_TEST int);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test_table change c ABC_TEST int;";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test_table(abcTest int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table add columns( abcTest int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table change c abcTest int;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test_table(AbcTest int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table add columns( AbcTest int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test_table change c AbcTest int;";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnTableNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "alter table test_table change a abc_test int;";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test_table(abc_test int);";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test_table add columns (abc_test int);";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test_table change c abc_test int;";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test_table(ABC_TEST int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table add columns (ABC_TEST int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table change c ABC_TEST int;";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test_table(abcTest int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table add columns (abcTest int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table change c abcTest int;";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test_table(AbcTest int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table add columns (AbcTest int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test_table change c AbcTest int;";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void columnTableNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "create table test_table(abc_test int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table add columns (abc_test int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table change c abc_test int;";
        assert !runScript(scriptResource, sql, p3);
        sql = "create table test_table(ABC_TEST int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table add columns (ABC_TEST int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table change c ABC_TEST int;";
        assert !runScript(scriptResource, sql, p3);
        sql = "create table test_table(abcTest int);";
        assert runScript(scriptResource, sql, p3);
        sql = "alter table test_table add columns (abcTest int);";
        assert runScript(scriptResource, sql, p3);
        sql = "alter table test_table change c abcTest int;";
        assert runScript(scriptResource, sql, p3);
        sql = "create table test_table(AbcTest int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table add columns (AbcTest int);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test_table change c AbcTest int;";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void columnTableNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "create table test_table(abc_test int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table add columns (abc_test int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table change c abc_test int;";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test_table(ABC_TEST int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table add columns( ABC_TEST int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table change c ABC_TEST int;";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test_table(abcTest int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table add columns( abcTest int);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test_table change c abcTest int;";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test_table(AbcTest int);";
        assert runScript(scriptResource, sql, p4);
        sql = "alter table test_table add columns (AbcTest int);";
        assert runScript(scriptResource, sql, p4);
        sql = "alter table test_table change c AbcTest int;";
        assert runScript(scriptResource, sql, p4);
    }
}
