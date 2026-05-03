package com.clougence.clouddm.ds.rules.special.mysql.schema;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleSchemaCollateTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/schema/my-schema-collate.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("collate", "utf8mb4_unicode_ci");
    private final Map<String, String> p2             = CollectionUtils.asMap("collate", "utf8mb4_unicode_ci", "require", "false");

    @Test
    public void schemaCollate_1() throws IOException {
        String sql = null;

        sql = "alter schema abc default collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter schema abc default collate utf8_unicode_ci;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create schema abc default collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p1);
        sql = "create schema abc default collate utf8_unicode_ci;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create schema abc;";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void schemaCollate_2() throws IOException {
        String sql = null;

        sql = "alter schema abc default collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter schema abc default collate utf8_unicode_ci;";
        assert !runScript(scriptResource, sql, p2);
        sql = "create schema abc default collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p2);
        sql = "create schema abc default collate utf8_unicode_ci;";
        assert !runScript(scriptResource, sql, p2);
        sql = "create schema abc;";
        assert runScript(scriptResource, sql, p2);
    }
}
