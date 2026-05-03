package com.clougence.clouddm.ds.rules.rdb.using_pg.insert;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_pg.PgAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class PgRuleInsertAllowQueryTest extends PgAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/insert/insert-allow-query.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    @Test
    public void testInsertSubQuery_1() throws IOException {
        String sql = null;

        sql = "insert into table1 select * from table2";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void testInsertSubQuery_2() throws IOException {
        String sql = null;

        sql = "insert into table1 select * from table2";
        assert !runScript(scriptResource, sql, p2);
    }
}
