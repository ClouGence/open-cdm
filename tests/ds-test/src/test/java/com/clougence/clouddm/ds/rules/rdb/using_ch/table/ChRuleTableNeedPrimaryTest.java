package com.clougence.clouddm.ds.rules.rdb.using_ch.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ch.ChAbstractRuleTest;

public class ChRuleTableNeedPrimaryTest extends ChAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-need-primary.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void createTableMaxColumns_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int(4), primary key(id))engine = test ";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int(4)) engine = test comment '   '";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc(id int(4), name varchar,primary key (id)) engine = test";
        assert runScript(scriptResource, sql, p1);
    }
}
