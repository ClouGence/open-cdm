package com.clougence.clouddm.ds.rules.rdb.using_my.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleTableMaxColumnsTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-max-columns.txt";
    private final Map<String, String> p1             = Collections.emptyMap();
    private final Map<String, String> p2             = CollectionUtils.asMap("maxCount", "2");

    @Test
    public void createTableMaxColumns_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int(4) primary key auto_increment, name varchar(25) not null, age varchar(25) not null)";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test.abc_test(id int primary key auto_increment, name varchar(25) not null)";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.abc_test(id int(4) primary key auto_increment)";
        assert runScript(scriptResource, sql, p2);
    }
}
