package com.clougence.clouddm.ds.rules.rdb.using_ch.delete;

import java.util.Map;

import com.clougence.clouddm.ds.rules.rdb.using_ch.ChAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class ChRuleDeleteAllowMultiTablesTest extends ChAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/delete/delete-allow-multi-tables.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    //    @Test
    //    public void deleteMultiTables_1() throws IOException {
    //        String sql = null;
    //
    //        sql = "DELETE o, od FROM test.orders o JOIN test.order_details od ON o.order_id = od.order_id WHERE o.order_id = 12345;";
    //        assert runScript(scriptResource, sql, p1);
    //        sql = "delete from test_specify_column where id =1";
    //        assert runScript(scriptResource, sql, p1);
    //    }
    //
    //    @Test
    //    public void deleteMultiTables_2() throws IOException {
    //        String sql = null;
    //
    //        sql = "DELETE o, od FROM test.orders o JOIN test.order_details od ON o.order_id = od.order_id WHERE o.order_id = 12345;";
    //        assert !runScript(scriptResource, sql, p2);
    //        sql = "delete from test_specify_column where id =1";
    //        assert runScript(scriptResource, sql, p2);
    //    }
}
