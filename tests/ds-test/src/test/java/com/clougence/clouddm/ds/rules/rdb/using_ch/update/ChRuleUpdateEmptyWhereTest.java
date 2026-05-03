package com.clougence.clouddm.ds.rules.rdb.using_ch.update;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ch.ChAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class ChRuleUpdateEmptyWhereTest extends ChAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/update/update-empty-where.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void emptyWhereTest_1() throws IOException {
        String sql = null;

        sql = "update abc set name = '11' where 1=1";
        assert !runScript(scriptResource, sql, p1);
        sql = "update abc set name = '11' where a=a";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void emptyWhereTest_2() throws IOException {
        String sql = null;

        sql = "update abc set name = '11' where 1=1";
        assert runScript(scriptResource, sql, p2);
        sql = "update abc set name = '11' where a=a";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void emptyWhereTest_3() throws IOException {
        String sql = null;

        // unEmpty and allow = false
        sql = "update abc set name = '11' where 1>1";
        assert !runScript(scriptResource, sql, p1);
        sql = "update abc set name = '11' where 1=1 and a = '123'";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void emptyWhereTest_4() throws IOException {
        String sql = null;

        // unEmpty and allow = true
        sql = "update abc set name = '11' where 1>1";
        assert runScript(scriptResource, sql, p2);
        sql = "update abc set name = '11' where 1=1 and a = '123'";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void testUpdateHasWhere_1() throws IOException {
        String sql = null;

        sql = "update table1  set a=1 where t1.id=(select id from bb)";
        assert runScript(scriptResource, sql, null);
        sql = "update table1  set a=1 where 1=1";
        assert !runScript(scriptResource, sql, null);
    }
}
