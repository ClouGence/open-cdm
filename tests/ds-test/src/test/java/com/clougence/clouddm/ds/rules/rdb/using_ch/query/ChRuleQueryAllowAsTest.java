package com.clougence.clouddm.ds.rules.rdb.using_ch.query;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ch.ChAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class ChRuleQueryAllowAsTest extends ChAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/query/query-allow-as.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void selectAndWithTest_1() throws IOException {
        String sql = null;

        // has and allow = false
        sql = "select id as a, name from table_1";
        assert !runScript(scriptResource, sql, p1);
        sql = "select * from (select id as a, name from table_1) t";
        assert !runScript(scriptResource, sql, p1);
        sql = "select * from table_1 where id in (select id as a from table_1)";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void selectAndWithTest_2() throws IOException {
        String sql = null;

        sql = "select id, name from table_1";
        assert runScript(scriptResource, sql, p1);
        sql = "select * from (select * from table_1) t";
        assert runScript(scriptResource, sql, p1);
        sql = "select * from table_1 where id in (select id from table_1)";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void selectAndWithTest_3() throws IOException {
        String sql = null;

        sql = "select id as a, name from table_1";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from (select id as a, name from table_1) t";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from table_1 where id in (select id as a from table_1)";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void selectAndWithTest_4() throws IOException {
        String sql = null;

        sql = "select id, name from table_1";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from (select * from table_1) t";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from table_1 where id in (select id from table_1)";
        assert runScript(scriptResource, sql, p2);
    }
}
