package com.clougence.clouddm.ds.rules.special.mysql.table;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleTableRequireAutoIncrementTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/table/table-require-auto-increment.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("number", "1", "require", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("number", "1", "require", "false");

    @Test
    public void autoIncrementTest_1() throws IOException {
        String sql = null;

        // true(require=true)
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) auto_increment = 1";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void autoIncrementTest_2() throws IOException {
        String sql = null;

        // true(require=false)
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null) auto_increment = 12";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void autoIncrementTest_3() throws IOException {
        String sql = null;

        // true(require=false)
        sql = "create table test.abc(id int(4) primary key auto_increment, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p2);
    }
}
