package com.clougence.clouddm.ds.rules.rdb.using_ms.query;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ms.MsAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MsRuleQueryAllowCrossInnerTest extends MsAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/query/query-allow-cross-inner.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void joinTest_1() throws IOException {
        String sql = null;

        // has and allow = false
        sql = "select * from table_1 a join table_2 b on a.id = b.id";
        assert !runScript(scriptResource, sql, p1);
        sql = "select * from (select * from table_1 a join table_2 b on a.id = b.id) t";
        assert !runScript(scriptResource, sql, p1);
        sql = "select * from table_1 where id in (select id from table_1 a join table_2 b on a.id = b.id)";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void joinTest_2() throws IOException {
        String sql = null;

        // unHas and allow = false
        sql = "select * from table_1 a left join table_2 b on a.id = b.id";
        assert runScript(scriptResource, sql, p1);
        sql = "select * from (select * from table_1) t";
        assert runScript(scriptResource, sql, p1);
        sql = "select * from table_1 where id in (select id from table_1)";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void joinTest_3() throws IOException {
        String sql = null;

        // has and allow = true
        sql = "select * from table_1 a join table_2 b on a.id = b.id";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from (select * from table_1 a join table_2 b on a.id = b.id) t";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from table_1 where id in (select id from table_1 a join table_2 b on a.id = b.id)";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void joinTest_4() throws IOException {
        String sql = null;

        // unHas and allow = true
        sql = "select * from table_1 a left join table_2 b on a.id = b.id";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from (select * from table_1) t";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from table_1 where id in (select id from table_1)";
        assert runScript(scriptResource, sql, p2);
    }
}
