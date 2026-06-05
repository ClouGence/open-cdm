package com.clougence.clouddm.ds.rules.rdb.using_db2.update;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleUpdateAllowSubQueryTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/update/update-allow-sub-query.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "Any");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "Set,Where");
    private final Map<String, String> p3             = CollectionUtils.asMap("allow", "None");

    @Test
    public void setHasSelect_1() throws IOException {
        String sql;

        sql = "update table1 t1 set t1.id=(select id from bb)";
        assert runScript(scriptResource, sql, p1);
        sql = "update table1 t1 set t1.id=1";
        assert runScript(scriptResource, sql, p1);
        //
        sql = "update table1 t1 set t1.id=(select id from bb)";
        assert runScript(scriptResource, sql, p2);
        sql = "update table1 t1 set t1.id=1";
        assert runScript(scriptResource, sql, p2);
        //
        sql = "update table1 t1 set t1.id=(select id from bb)";
        assert !runScript(scriptResource, sql, p3);
        sql = "update table1 t1 set t1.id=1";
        assert runScript(scriptResource, sql, p3);
    }

    @Test
    public void whereHasSelect_1() throws IOException {
        String sql;

        sql = "update table1 t1 set t1.id=1 where id in (select id from bb)";
        assert runScript(scriptResource, sql, p1);
        sql = "update table1 t1 set t1.id=1 where id =1";
        assert runScript(scriptResource, sql, p1);
        //
        sql = "update table1 t1 set t1.id=1 where id in (select id from bb)";
        assert runScript(scriptResource, sql, p2);
        sql = "update table1 t1 set t1.id=1 where id =1";
        assert runScript(scriptResource, sql, p2);
        //
        sql = "update table1 t1 set t1.a=1 where id in (select id from bb)";
        assert !runScript(scriptResource, sql, p3);
        sql = "update table1 t1 set t1.a=1 where id =1";
        assert runScript(scriptResource, sql, p3);
    }
}
