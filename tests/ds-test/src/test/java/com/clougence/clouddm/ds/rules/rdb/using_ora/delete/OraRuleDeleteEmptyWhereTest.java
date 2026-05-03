package com.clougence.clouddm.ds.rules.rdb.using_ora.delete;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ora.OraAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class OraRuleDeleteEmptyWhereTest extends OraAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/delete/delete-empty-where.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void emptyWhereTest_1() throws IOException {
        String sql = null;

        // empty and allow = false
        sql = "delete from abc";
        assert !runScript(scriptResource, sql, p1);
        sql = "delete from table_1 where 1=1";
        assert !runScript(scriptResource, sql, p1);
        sql = "delete from table_1 where a=a";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void emptyWhereTest_2() throws IOException {
        String sql = null;

        // empty and allow = true
        sql = "delete from abc";
        assert runScript(scriptResource, sql, p2);
        sql = "delete from table_1 where 1=1";
        assert runScript(scriptResource, sql, p2);
        sql = "delete from table_1 where a=a";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void notEmptyWhereTest_1() throws IOException {
        String sql = null;

        // unEmpty and allow = false
        sql = "delete from table_1 where 1>1";
        assert !runScript(scriptResource, sql, p1);
        sql = "delete from table_1 where 1=1 and a = '123'";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void notEmptyWhereTest_2() throws IOException {
        String sql = null;

        // unEmpty and allow = true
        sql = "delete from table_1 where 1>1";
        assert runScript(scriptResource, sql, p2);
        sql = "delete from table_1 where 1=1 and a = '123'";
        assert runScript(scriptResource, sql, p2);
    }
}
