package com.clougence.clouddm.ds.rules.rdb.using_ms.query;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ms.MsAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MsRuleQueryEmptyWhereTest extends MsAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/query/query-empty-where.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void emptyWhereTest_1() throws IOException {
        String sql = null;

        // empty and allow = false
        sql = "select * from abc";
        assert !runScript(scriptResource, sql, p1);
        sql = "select * from table_1 where 1=1";
        assert !runScript(scriptResource, sql, p1);
        sql = "select * from table_1 where a=a";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void emptyWhereTest_2() throws IOException {
        String sql = null;

        // empty and allow = true
        sql = "select * from abc";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from table_1 where 1=1";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from table_1 where a=a";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void emptyWhereTest_3() throws IOException {
        String sql = null;

        // unEmpty and allow = false
        sql = "select * from table_1 where 1>1";
        assert runScript(scriptResource, sql, p1);
        sql = "select * from table_1 where 1=1 and a = '123'";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void emptyWhereTest_4() throws IOException {
        String sql = null;

        // unEmpty and allow = true
        sql = "select * from table_1 where 1>1";
        assert runScript(scriptResource, sql, p2);
        sql = "select * from table_1 where 1=1 and a = '123'";
        assert runScript(scriptResource, sql, p2);
    }
}
