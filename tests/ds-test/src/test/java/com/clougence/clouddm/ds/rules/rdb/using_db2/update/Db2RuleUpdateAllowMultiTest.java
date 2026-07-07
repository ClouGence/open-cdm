package com.clougence.clouddm.ds.rules.rdb.using_db2.update;

import java.util.Map;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleUpdateAllowMultiTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/update/update-allow-multi.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    //    @Test
    //    public void testLimit_1() throws IOException {
    //        String sql = null;
    //
    //        sql = "update table1 t1 set t1.a=1";
    //        assert runScript(scriptResource, sql, p1);
    //
    //        //
    //        sql = "update table1 t1,table2 t2 set t1.a=1,t2.b=2 where t1.id=1 and t2.id=1";
    //        assert !runScript(scriptResource, sql, p1);
    //        sql = "update table1 t1,table2 t2 set t1.a=1,t2.b=2 where t1.id=1 and t2.id=1";
    //        assert runScript(scriptResource, sql, p2);
    //    }
}
