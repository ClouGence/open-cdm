package com.clougence.clouddm.ds.rules.rdb.using_db2.table;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleTableNeedColumnsTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-need-columns.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("columns", "a,c");

    @Test
    public void needColumns_1() throws IOException {
        String sql;

        sql = "create table test.abc(a int,b int,c int,d int)";
        assert runScript(scriptResource, sql, p1);

        sql = "create table test.abc(a int,b int,d int)";
        assert !runScript(scriptResource, sql, p1);
    }
}
