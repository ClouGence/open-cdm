package com.clougence.clouddm.ds.rules.special.mysql.update;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleUpdateAllowIgnoreTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/update/update-allow-ignore.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void ignore_1() throws IOException {
        String sql = null;

        sql = "update ignore table1 t1 set t1.a=1";
        assert !runScript(scriptResource, sql, p1);
        sql = "update table1 t1 set t1.a=1";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void ignore_2() throws IOException {
        String sql = null;

        sql = "update ignore table1 t1 set t1.a=1";
        assert runScript(scriptResource, sql, p2);
        sql = "update table1 t1 set t1.a=1";
        assert runScript(scriptResource, sql, p2);
    }
}
