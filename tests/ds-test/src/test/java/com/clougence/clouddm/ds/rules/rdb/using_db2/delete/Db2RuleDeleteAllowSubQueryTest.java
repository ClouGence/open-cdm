package com.clougence.clouddm.ds.rules.rdb.using_db2.delete;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleDeleteAllowSubQueryTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/delete/delete-allow-sub-query.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "Any");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "Where");
    private final Map<String, String> p3             = CollectionUtils.asMap("allow", "None");

    @Test
    public void testHasWhereSelect_1() throws IOException {
        String sql;

        sql = "delete from test_specify_column where id = (select id from text)";
        assert runScript(scriptResource, sql, p1);
        sql = "delete from test_specify_column where id =1";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void testHasWhereSelect_2() throws IOException {
        String sql;

        sql = "delete from test_specify_column where id = (select id from text)";
        assert runScript(scriptResource, sql, p2);
        sql = "delete from test_specify_column where id =1";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void testHasWhereSelect_3() throws IOException {
        String sql;

        sql = "delete from test_specify_column where id = (select id from text)";
        assert !runScript(scriptResource, sql, p3);
        sql = "delete from test_specify_column where id =1";
        assert runScript(scriptResource, sql, p3);
    }
}
