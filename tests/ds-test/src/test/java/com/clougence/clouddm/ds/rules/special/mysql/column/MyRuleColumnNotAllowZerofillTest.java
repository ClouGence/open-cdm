package com.clougence.clouddm.ds.rules.special.mysql.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;

public class MyRuleColumnNotAllowZerofillTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/column/column-notallow-zerofill.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void columnNotAllowZerofill_1() throws IOException {
        String sql = null;

        sql = "create table test.abc(id int);";
        assert runScript(scriptResource, sql, p1);

        sql = "create table test.abc(id int zerofill);";
        assert !runScript(scriptResource, sql, p1);
    }
}
