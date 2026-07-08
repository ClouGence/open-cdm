package com.clougence.clouddm.ds.rules.rdb.using_db2.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;

public class Db2RuleColumnNotNullTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-need-notnull.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void columnNotNull_1() throws IOException {
        String sql;

        sql = "create table test.abc_test(id int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int not null)";
        assert runScript(scriptResource, sql, p1);
    }
}
