package com.clougence.clouddm.ds.rules.rdb.using_db2.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;

public class Db2RuleTableNeedPrimaryTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-need-primary.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void createTableMaxColumns_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int not null primary key)";
        assert runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int)";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc(id int not null, name varchar(25) not null,primary key (id))";
        assert runScript(scriptResource, sql, p1);
    }
}
