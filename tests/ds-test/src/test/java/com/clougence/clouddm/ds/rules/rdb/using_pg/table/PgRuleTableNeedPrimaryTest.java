package com.clougence.clouddm.ds.rules.rdb.using_pg.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_pg.PgAbstractRuleTest;

public class PgRuleTableNeedPrimaryTest extends PgAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-need-primary.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void createTableMaxColumns_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int primary key)";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int)";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc(id int, name varchar(25) not null,primary key (id))";
        assert runScript(scriptResource, sql, p1);
    }
}
