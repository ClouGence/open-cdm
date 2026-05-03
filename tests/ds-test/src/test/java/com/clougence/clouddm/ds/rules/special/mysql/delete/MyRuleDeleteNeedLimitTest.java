package com.clougence.clouddm.ds.rules.special.mysql.delete;

import java.io.IOException;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;

public class MyRuleDeleteNeedLimitTest extends MyAbstractRuleTest {

    private final String scriptResource = "rule-test/special/mysql/delete/delete-need-limit.txt";

    @Test(expected = UnsupportedOperationException.class)
    public void testLimit_1() throws IOException {
        String sql = null;

        sql = "DELETE o, od FROM test.orders o JOIN test.order_details od ON o.order_id = od.order_id WHERE o.order_id = 12345 limit 1;";
        assert runScript(scriptResource, sql, null);

        sql = "delete from test_specify_column where id =1";
        assert !runScript(scriptResource, sql, null);
    }
}
