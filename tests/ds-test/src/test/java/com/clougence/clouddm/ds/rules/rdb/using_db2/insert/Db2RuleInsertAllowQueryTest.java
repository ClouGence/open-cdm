package com.clougence.clouddm.ds.rules.rdb.using_db2.insert;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleInsertAllowQueryTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/insert/insert-allow-query.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    @Test
    public void testInsertSubQuery_1() throws IOException {
        String sql;

        sql = "insert into table1 select * from table2";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void testInsertSubQuery_2() throws IOException {
        String sql;

        sql = "insert into table1 select * from table2";
        assert !runScript(scriptResource, sql, p2);
    }
}
