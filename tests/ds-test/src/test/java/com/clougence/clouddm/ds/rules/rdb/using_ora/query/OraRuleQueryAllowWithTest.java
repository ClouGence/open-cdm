package com.clougence.clouddm.ds.rules.rdb.using_ora.query;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ora.OraAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class OraRuleQueryAllowWithTest extends OraAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/query/query-allow-with.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void selectAndWithTest_1() throws IOException {
        String sql = null;

        sql = "select * from abc";
        assert runScript(scriptResource, sql, p1);
        sql = "with one as (select 1) select * from table_1";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void selectAndWithTest_2() throws IOException {
        String sql = null;

        sql = "select * from abc";
        assert runScript(scriptResource, sql, p2);
        sql = "with one as (select 1) select * from table_1";
        assert runScript(scriptResource, sql, p2);
    }
}
