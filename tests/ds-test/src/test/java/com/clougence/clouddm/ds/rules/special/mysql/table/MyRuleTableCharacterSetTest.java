package com.clougence.clouddm.ds.rules.special.mysql.table;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleTableCharacterSetTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/table/table-character-set.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("character_set", "utf8mb4");
    private final Map<String, String> p2             = CollectionUtils.asMap("character_set", "utf8mb4", "require", "false");

    @Test
    public void tableCharacterSet_1() throws IOException {
        String sql = null;

        // true(require = true)
        sql = "create table test.abc(id int(4), name varchar(25) not null) default character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc comment 'abc' engine MyISAM, convert to character set utf8mb4;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc comment 'abc';";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void tableCharacterSet_2() throws IOException {
        String sql = null;

        // false(require = true)
        sql = "create table test.abc(id int(4), name varchar(25) not null) default character set utf8;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc(id int(4), name varchar(25) not null);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc character set utf8;";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void tableCharacterSet_3() throws IOException {
        String sql = null;

        // true(require = false)
        sql = "create table test.abc(id int(4), name varchar(25) not null) default character set utf8mb4;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc character set utf8mb4;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc comment 'abc' engine MyISAM, convert to character set utf8mb4;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc comment 'abc';";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.abc(id int(4), name varchar(25) not null)";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void tableCharacterSet_4() throws IOException {
        String sql = null;

        // false(require = false)
        sql = "create table test.abc(id int(4), name varchar(25) not null) default character set utf8;";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc character set utf8;";
        assert !runScript(scriptResource, sql, p1);
    }
}
