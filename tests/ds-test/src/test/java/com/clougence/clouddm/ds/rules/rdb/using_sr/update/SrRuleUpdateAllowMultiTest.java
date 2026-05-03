package com.clougence.clouddm.ds.rules.rdb.using_sr.update;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_sr.SrAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class SrRuleUpdateAllowMultiTest extends SrAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/update/update-allow-multi.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void testLimit_1() throws IOException {
        String sql = null;

        sql = "update table1 set a=1";
        assert runScript(scriptResource, sql, p1);

        //
        //        sql = "update table1 t1,table2 t2 set t1.a=1,t2.b=2 where t1.id=1 and t2.id=1";
        //        assert !runScript(scriptResource, sql, p1);
        //        sql = "update table1 t1,table2 t2 set t1.a=1,t2.b=2 where t1.id=1 and t2.id=1";
        //        assert runScript(scriptResource, sql, p2);
    }
}
