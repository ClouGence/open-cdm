package com.clougence.clouddm.ds.rules.rdb.using_db2.query;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleQueryAllowWithTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/query/query-allow-with.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void selectAndWithTest_1() throws IOException {
        String sql;

        sql = "select * from abc";
        assert runScript(scriptResource, sql, p1);
        sql = "with one as (select 1 as a from SYSIBM.SYSDUMMY1) select * from one";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void selectAndWithTest_2() throws IOException {
        String sql;

        sql = "select * from abc";
        assert runScript(scriptResource, sql, p2);
        sql = "with one as (select 1 as a from SYSIBM.SYSDUMMY1) select * from one";
        assert runScript(scriptResource, sql, p2);
    }
}
