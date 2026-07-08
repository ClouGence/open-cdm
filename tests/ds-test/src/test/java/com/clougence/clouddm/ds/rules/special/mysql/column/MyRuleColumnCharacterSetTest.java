package com.clougence.clouddm.ds.rules.special.mysql.column;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleColumnCharacterSetTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/column/column-character-set.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("character_set", "utf8mb4", "require", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("character_set", "utf8mb4", "require", "false");

    @Test
    public void columnCharacterSet_1() throws IOException {
        String sql = null;

        // true(require=true)
        sql = "create table test.abc(name varchar(25) character set utf8mb4);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25) character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc modify name varchar(25) character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc change name name1 varchar(25) character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc modify name varchar(25);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc change name name1 varchar(25);";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnCharacterSet_2() throws IOException {
        String sql = null;

        // false(require=true)
        sql = "create table test.abc(name varchar(25));";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc(name varchar(25) character set utf8);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25) character set utf8;";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnCharacterSet_3() throws IOException {
        String sql = null;

        // true(require=false)
        sql = "create table test.abc(name varchar(25));";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc add name varchar(25);";
        assert runScript(scriptResource, sql, p2);
    }
}
