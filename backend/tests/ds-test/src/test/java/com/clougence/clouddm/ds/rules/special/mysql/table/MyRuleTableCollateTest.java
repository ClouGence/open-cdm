package com.clougence.clouddm.ds.rules.special.mysql.table;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleTableCollateTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/table/table-collate.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("collate", "utf8mb4_unicode_ci");
    private final Map<String, String> p2             = CollectionUtils.asMap("collate", "utf8mb4_unicode_ci", "require", "false");

    @Test
    public void tableCollate_1() throws IOException {
        String sql = null;

        //true(require = true)
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc character set utf8mb4 collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc comment 'abc' engine MyISAM, convert to character set utf8mb4 collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc comment 'abc';";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void tableCollate_2() throws IOException {
        String sql = null;

        //false(require = true)
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) collate utf8_unicode_ci;";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc character set utf8mb4 collate utf8_unicode_ci;";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void tableCollate_3() throws IOException {
        String sql = null;

        //true(require = false)
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc character set utf8mb4 collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc comment 'abc' engine MyISAM, convert to character set utf8mb4 collate utf8mb4_unicode_ci;";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test.abc comment 'abc';";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void tableCollate_4() throws IOException {
        String sql = null;

        //false(require = false)
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) collate utf8_unicode_ci";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test.abc character set utf8mb4 collate utf8_unicode_ci;";
        assert !runScript(scriptResource, sql, p2);
    }
}
