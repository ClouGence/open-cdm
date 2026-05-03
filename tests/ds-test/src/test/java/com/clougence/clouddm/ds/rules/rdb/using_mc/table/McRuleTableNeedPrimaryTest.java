package com.clougence.clouddm.ds.rules.rdb.using_mc.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_mc.McAbstractRuleTest;

public class McRuleTableNeedPrimaryTest extends McAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-need-primary.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void createTableMaxColumns_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int(4) primary key ) ";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int(4)) comment '   '";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc(id int(4), name varchar(25) not null,primary key (id))";
        assert runScript(scriptResource, sql, p1);
    }
}
