package com.clougence.clouddm.ds.rules.rdb.using_db2.column;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleColumnTypesTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-types.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("ruleType", "AllowList", "typeList", "int,char,datetime");
    private final Map<String, String> p2             = CollectionUtils.asMap("ruleType", "BlockList", "typeList", "int,char,datetime");

    @Test
    public void columnTypes_1() throws IOException {
        String sql;

        sql = "create table test.abc_test(id int)";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id varchar(4))";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnTypes_2() throws IOException {
        String sql;

        sql = "create table test.abc_test(id int)";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test.abc_test(id varchar(4))";
        assert runScript(scriptResource, sql, p2);
    }
}
