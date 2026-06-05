package com.clougence.clouddm.ds.rules.rdb.using_db2.schema;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleSchemaNameCaseTypeTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/schema/schema-name-case-type.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("caseType", "Upper case");
    private final Map<String, String> p2             = CollectionUtils.asMap("caseType", "Lower case");
    private final Map<String, String> p3             = CollectionUtils.asMap("caseType", "Lower camel case");
    private final Map<String, String> p4             = CollectionUtils.asMap("caseType", "Upper camel case");

    @Test
    public void createSchemaNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "create schema abc_test";
        assert !runScript(scriptResource, sql, p1);
        sql = "create schema ABC_TEST";
        assert runScript(scriptResource, sql, p1);
        sql = "create schema abcTest";
        assert !runScript(scriptResource, sql, p1);
        sql = "create schema AbcTest";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void createSchemaNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "create schema abc_test";
        assert runScript(scriptResource, sql, p2);
        sql = "create schema ABC_TEST";
        assert !runScript(scriptResource, sql, p2);
        sql = "create schema abcTest";
        assert !runScript(scriptResource, sql, p2);
        sql = "create schema AbcTest";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void createSchemaNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "create schema abc_test";
        assert !runScript(scriptResource, sql, p3);
        sql = "create schema ABC_TEST";
        assert !runScript(scriptResource, sql, p3);
        sql = "create schema abcTest";
        assert runScript(scriptResource, sql, p3);
        sql = "create schema AbcTest";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void createSchemaNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "create schema abc_test";
        assert !runScript(scriptResource, sql, p4);
        sql = "create schema ABC_TEST";
        assert !runScript(scriptResource, sql, p4);
        sql = "create schema abcTest";
        assert !runScript(scriptResource, sql, p4);
        sql = "create schema AbcTest";
        assert runScript(scriptResource, sql, p4);
    }
}
