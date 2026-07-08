package com.clougence.clouddm.ds.rules.special.mysql.column;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleColumnAutoTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/column/column-auto.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("columnName", "id", "requireUnsigned", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("columnName", "id", "requireUnsigned", "false");

    @Test
    public void columnAuto_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int(4) primary key auto_increment)";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int(4) unsigned primary key auto_increment)";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnAuto_2() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int(4) primary key auto_increment)";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.abc_test(id int(4) unsigned primary key auto_increment)";
        assert runScript(scriptResource, sql, p2);
    }
}
