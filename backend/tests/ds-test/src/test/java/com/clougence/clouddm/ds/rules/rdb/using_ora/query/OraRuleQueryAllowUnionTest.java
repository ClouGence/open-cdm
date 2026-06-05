package com.clougence.clouddm.ds.rules.rdb.using_ora.query;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ora.OraAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class OraRuleQueryAllowUnionTest extends OraAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/query/query-allow-union.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void selectAndWithTest_1() throws IOException {
        String sql = null;

        // has and allow = false
        sql = "select * from table_1 union select * from table_2";
        assert !runScript(scriptResource, sql, p1);
        sql = "select * from (select * from table_1 union select * from table_2) t";
        assert !runScript(scriptResource, sql, p1);
        sql = "select id from table_3 where id in (select id from table_1 union select id from table_2)";
        assert !runScript(scriptResource, sql, p1);
        // unHas and allow = false
        sql = "select * from table_1";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void unionQueryTest_1() throws IOException {
        String sql = null;

        // has and allow = true
        sql = "select * from table_1 union select * from table_2";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from (select * from table_1 union select * from table_2) t";
        assert runScript(scriptResource, sql, p2);
        sql = "select id from table_3 where id in (select id from table_1 union select id from table_2)";
        assert runScript(scriptResource, sql, p2);
        // unHas and allow = true
        sql = "select * from table_1";
        assert runScript(scriptResource, sql, p2);
    }
}
