package com.clougence.clouddm.ds.rules.special.mysql.column;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleColumnAllowCharacterSetAndCollateTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/column/column-allow-characterset-and-collate.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "Both");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "Character set");
    private final Map<String, String> p3             = CollectionUtils.asMap("allow", "Collate");
    private final Map<String, String> p4             = CollectionUtils.asMap("allow", "Nothing");

    @Test
    public void columnAllowCharacterSetAndCollate_1() throws IOException {
        String sql = null;

        // true(allow=Both)
        sql = "create table test.abc(name varchar(25));";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25);";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abc(name varchar(25) character set utf8mb4);";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abc(name varchar(25) collate utf8mb4_unicode_ci);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25) character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25) collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnAllowCharacterSetAndCollate_2() throws IOException {
        String sql = null;

        // true(allow=Character set)
        sql = "create table test.abc(name varchar(25));";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc add name varchar(25);";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.abc(name varchar(25) character set utf8mb4);";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.abc(name varchar(25) collate utf8mb4_unicode_ci);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test.abc add name varchar(25) character set utf8mb4;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc add name varchar(25) collate utf8mb4_unicode_ci;";
        assert !runScript(scriptResource, sql, p2);
    }

    @Test
    public void columnAllowCharacterSetAndCollate_3() throws IOException {
        String sql = null;

        // true(allow=Collate)
        sql = "create table test.abc(name varchar(25));";
        assert runScript(scriptResource, sql, p3);
        sql = "alter table test.abc add name varchar(25);";
        assert runScript(scriptResource, sql, p3);
        sql = "create table test.abc(name varchar(25) character set utf8mb4);";
        assert !runScript(scriptResource, sql, p3);
        sql = "create table test.abc(name varchar(25) collate utf8mb4_unicode_ci);";
        assert runScript(scriptResource, sql, p3);
        sql = "alter table test.abc add name varchar(25) character set utf8mb4;";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test.abc add name varchar(25) collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p3);
    }

    @Test
    public void columnAllowCharacterSetAndCollate_4() throws IOException {
        String sql = null;

        // true(allow=Nothing)
        sql = "create table test.abc(name varchar(25));";
        assert runScript(scriptResource, sql, p4);
        sql = "alter table test.abc add name varchar(25);";
        assert runScript(scriptResource, sql, p4);
        sql = "create table test.abc(name varchar(25) character set utf8mb4);";
        assert !runScript(scriptResource, sql, p4);
        sql = "create table test.abc(name varchar(25) collate utf8mb4_unicode_ci);";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test.abc add name varchar(25) character set utf8mb4;";
        assert !runScript(scriptResource, sql, p4);
        sql = "alter table test.abc add name varchar(25) collate utf8mb4_unicode_ci;";
        assert !runScript(scriptResource, sql, p4);
    }
}
