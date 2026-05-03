package com.clougence.clouddm.ds.rules.special.mysql.update;

import java.io.IOException;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;

public class MyRuleUpdateLimitTest extends MyAbstractRuleTest {

    private final String scriptResource = "rule-test/special/mysql/update/update-need-limit.txt";

    @Test
    public void testLimit_1() throws IOException {
        String sql = null;

        sql = "update table1 t1 set t1.a=1 limit 1";
        assert runScript(scriptResource, sql, null);
        sql = "update table1 t1 set t1.a=1";
        assert !runScript(scriptResource, sql, null);
    }
}
