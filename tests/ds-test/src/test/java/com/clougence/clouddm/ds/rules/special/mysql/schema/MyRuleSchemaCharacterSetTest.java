package com.clougence.clouddm.ds.rules.special.mysql.schema;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleSchemaCharacterSetTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/schema/my-schema-character-set.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("character_set", "utf8mb4");
    private final Map<String, String> p2             = CollectionUtils.asMap("character_set", "utf8mb4", "require", "false");

    @Test
    public void schemaCharacterSet_1() throws IOException {
        String sql = null;

        sql = "alter schema abc default character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter schema abc default character set utf8;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create schema abc default character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "create schema abc default character set utf8;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create schema abc;";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void schemaCharacterSet_2() throws IOException {
        String sql = null;

        sql = "alter schema abc default character set utf8mb4;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter schema abc default character set utf8;";
        assert !runScript(scriptResource, sql, p2);
        sql = "create schema abc default character set utf8mb4;";
        assert runScript(scriptResource, sql, p2);
        sql = "create schema abc default character set utf8;";
        assert !runScript(scriptResource, sql, p2);
        sql = "create schema abc;";
        assert runScript(scriptResource, sql, p2);
    }
}
