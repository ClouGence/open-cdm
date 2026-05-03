package com.clougence.clouddm.ds.rules.rdb.using_db2.update;

import java.util.Map;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleUpdateAllowWithTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/update/update-allow-with.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    //    @Test
    //    public void selectAndWithTest_1() throws IOException {
    //        String sql = null;
    //
    //        sql = "select * from abc";
    //        assert runScript(scriptResource, sql, p1);
    //        sql = "with one as (select 1) select * from table_1";
    //        assert !runScript(scriptResource, sql, p1);
    //    }
    //
    //    @Test
    //    public void selectAndWithTest_2() throws IOException {
    //        String sql = null;
    //
    //        sql = "select * from abc";
    //        assert runScript(scriptResource, sql, p2);
    //        sql = "with one as (select 1) select * from table_1";
    //        assert runScript(scriptResource, sql, p2);
    //    }
}
