package com.clougence.clouddm.ds.rules.rdb.using_pg.query;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_pg.PgAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class PgRuleQueryAllowSubQueryTest extends PgAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/query/query-allow-sub-query.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "Any");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "Select,From,Where");
    private final Map<String, String> p3             = CollectionUtils.asMap("allow", "None");

    @Test
    public void selectHasSelect_1() throws IOException {
        String sql;

        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p1);
        sql = "select (select id from bb) as id1, name from table1;";
        assert runScript(scriptResource, sql, p1);
        //
        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p2);
        sql = "select (select id from bb) as id1, name from table1;";
        assert runScript(scriptResource, sql, p2);
        //
        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p3);
        sql = "select (select id from bb) as id1, name from table1;";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void fromHasSelect_1() throws IOException {
        String sql;

        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p1);
        sql = "select t.id, t.name from (select id from bb) as t;";
        assert runScript(scriptResource, sql, p1);
        //
        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p2);
        sql = "select t.id, t.name from (select id from bb) as t;";
        assert runScript(scriptResource, sql, p2);
        //
        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p3);
        sql = "select t.id, t.name from (select id from bb) as t;";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void whereHasSelect_1() throws IOException {
        String sql;

        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p1);
        sql = "select id, name from table1 where id in (select id from bb);";
        assert runScript(scriptResource, sql, p1);
        //
        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p2);
        sql = "select id, name from table1 where id in (select id from bb);";
        assert runScript(scriptResource, sql, p2);
        //
        sql = "select id, name from table1;";
        assert runScript(scriptResource, sql, p3);
        sql = "select id, name from table1 where id in (select id from bb);";
        assert !runScript(scriptResource, sql, p3);
    }
}
