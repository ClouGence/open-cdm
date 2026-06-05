package com.clougence.clouddm.ds.rules.rdb.using_my.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;

public class MyRuleColumnNotNullTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-need-notnull.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void columnNotNull_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int(4))";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int(4) not null)";
        assert runScript(scriptResource, sql, p1);
    }
}
