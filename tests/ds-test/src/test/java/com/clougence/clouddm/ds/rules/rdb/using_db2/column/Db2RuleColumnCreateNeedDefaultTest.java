package com.clougence.clouddm.ds.rules.rdb.using_db2.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;

public class Db2RuleColumnCreateNeedDefaultTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-create-need-default.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void columnNeedDefault_1() throws IOException {
        String sql;

        // true
        sql = "create table test.abc_test(id int default 1);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25) default 'aa';";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25) default '';";
        assert runScript(scriptResource, sql, p1);

    }

    @Test
    public void columnNeedDefault_2() throws IOException {
        String sql;

        // false
        sql = "create table test.abc_test(id int);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add name varchar(25);";
        assert !runScript(scriptResource, sql, p1);
    }

}
