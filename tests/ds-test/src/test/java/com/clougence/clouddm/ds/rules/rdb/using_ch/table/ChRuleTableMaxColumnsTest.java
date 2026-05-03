package com.clougence.clouddm.ds.rules.rdb.using_ch.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ch.ChAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class ChRuleTableMaxColumnsTest extends ChAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-max-columns.txt";
    private final Map<String, String> p1             = Collections.emptyMap();
    private final Map<String, String> p2             = CollectionUtils.asMap("maxCount", "2");

    @Test
    public void createTableMaxColumns_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int, name string, age string) engine = memory";
        assert !runScript(scriptResource, sql, p2);
        sql = "create table test.abc_test(id int , name string ) engine = memory";
        assert runScript(scriptResource, sql, p2);
        sql = "create table test.abc_test(id string) engine = memory";
        assert runScript(scriptResource, sql, p2);
    }
}
