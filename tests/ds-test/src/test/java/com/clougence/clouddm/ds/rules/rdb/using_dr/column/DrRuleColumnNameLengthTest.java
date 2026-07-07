package com.clougence.clouddm.ds.rules.rdb.using_dr.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_dr.DrAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class DrRuleColumnNameLengthTest extends DrAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-name-length.txt";
    private final Map<String, String> p1             = Collections.emptyMap();
    private final Map<String, String> p2             = CollectionUtils.asMap("length", "5");

    @Test
    public void columnNameLength_1() throws IOException {
        String sql = null;

        // true
        sql = "create table test(a123456 int);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test add column a123456 int;";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test modify column a123456 int;";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnNameLength_2() throws IOException {
        String sql = null;

        // false
        sql = "create table test(a123456 int);";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test add column a123456 int;";
        assert !runScript(scriptResource, sql, p2);
    }
}
