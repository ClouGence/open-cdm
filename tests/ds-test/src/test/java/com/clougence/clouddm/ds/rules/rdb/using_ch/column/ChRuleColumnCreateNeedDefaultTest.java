package com.clougence.clouddm.ds.rules.rdb.using_ch.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ch.ChAbstractRuleTest;

public class ChRuleColumnCreateNeedDefaultTest extends ChAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-create-need-default.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void columnNeedDefault_1() throws IOException {
        String sql = null;

        // true
        sql = "create table test.abc_test(id int(4) default 1) engine = test";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add column name varchar(25) default 'aa';";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add column name varchar(25) default '';";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnNeedDefault_2() throws IOException {
        String sql = null;

        // false
        sql = "create table test.abc_test(id int(4)) engine = test";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add column name varchar(25);";
        assert !runScript(scriptResource, sql, p1);
    }
}
