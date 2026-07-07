package com.clougence.clouddm.ds.rules.rdb.using_my.insert;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleInsertAllowIgnoreTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/insert/insert-allow-ignore.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    @Test
    public void insertIgnore_1() throws IOException {
        String sql = null;

        sql = "insert ignore into table1 values(1,2,null)";
        assert runScript(scriptResource, sql, p1);
        sql = "insert into table1 values(1,2,null)";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void insertIgnore_2() throws IOException {
        String sql = null;

        sql = "insert ignore into table1 values(1,2,3)";
        assert !runScript(scriptResource, sql, p2);
        sql = "insert into table1 values(1,2,3)";
        assert runScript(scriptResource, sql, p2);
        sql = "insert into table1 select * from table2";
        assert runScript(scriptResource, sql, p2);
        sql = "insert ignore into table1 select * from table2";
        assert !runScript(scriptResource, sql, p2);
    }
}
