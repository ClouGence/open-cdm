package com.clougence.clouddm.ds.rules.rdb.using_pg.catalog;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_pg.PgAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class PgRuleCatalogNameCaseTypeTest extends PgAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/catalog/catalog-name-case-type.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("caseType", "Upper case");
    private final Map<String, String> p2             = CollectionUtils.asMap("caseType", "Lower case");
    private final Map<String, String> p3             = CollectionUtils.asMap("caseType", "Lower camel case");
    private final Map<String, String> p4             = CollectionUtils.asMap("caseType", "Upper camel case");

    @Test
    public void createCatalogNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "create database abc_test";
        assert !runScript(scriptResource, sql, p1);
        sql = "create database ABC_TEST";
        assert runScript(scriptResource, sql, p1);
        sql = "create database abcTest";
        assert !runScript(scriptResource, sql, p1);
        sql = "create database AbcTest";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void createCatalogNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "create database abc_test";
        assert runScript(scriptResource, sql, p2);
        sql = "create database ABC_TEST";
        assert !runScript(scriptResource, sql, p2);
        sql = "create database abcTest";
        assert !runScript(scriptResource, sql, p2);
        sql = "create database AbcTest";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void createCatalogNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "create database abc_test";
        assert !runScript(scriptResource, sql, p3);
        sql = "create database ABC_TEST";
        assert !runScript(scriptResource, sql, p3);
        sql = "create database abcTest";
        assert runScript(scriptResource, sql, p3);
        sql = "create database AbcTest";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void createCatalogNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "create database abc_test";
        assert !runScript(scriptResource, sql, p4);
        sql = "create database ABC_TEST";
        assert !runScript(scriptResource, sql, p4);
        sql = "create database abcTest";
        assert !runScript(scriptResource, sql, p4);
        sql = "create database AbcTest";
        assert runScript(scriptResource, sql, p4);
    }

    @Test
    public void alterCatalogNameCaseType_1() throws IOException {
        String sql = null;

        // Upper case
        sql = "alter database abc rename to abc_test";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter database abc rename to ABC_TEST";
        assert runScript(scriptResource, sql, p1);
        sql = "alter database abc rename to abcTest";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter database abc rename to AbcTest";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void alterCatalogNameCaseType_2() throws IOException {
        String sql = null;

        // Lower case
        sql = "alter database abc rename to abc_test";
        assert runScript(scriptResource, sql, p2);
        sql = "alter database abc rename to ABC_TEST";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter database abc rename to abcTest";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter database abc rename to AbcTest";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void alterCatalogNameCaseType_3() throws IOException {
        String sql = null;

        // Lower camel case
        sql = "alter database abc rename to abc_test";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter database abc rename to ABC_TEST";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter database abc rename to abcTest";
        assert runScript(scriptResource, sql, p3);
        sql = "alter database abc rename to AbcTest";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void alterCatalogNameCaseType_4() throws IOException {
        String sql = null;

        // Upper camel case
        sql = "alter database abc rename to abc_test";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter database abc rename to ABC_TEST";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter database abc rename to abcTest";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter database abc rename to AbcTest";
        assert runScript(scriptResource, sql, p4);
    }
}
