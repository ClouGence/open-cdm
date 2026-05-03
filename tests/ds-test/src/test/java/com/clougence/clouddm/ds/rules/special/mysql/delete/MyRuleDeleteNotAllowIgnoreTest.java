package com.clougence.clouddm.ds.rules.special.mysql.delete;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MyRuleDeleteNotAllowIgnoreTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/delete/delete-not-allow-ignore.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    @Test
    public void deleteIgnore_1() throws IOException {
        String sql = null;

        sql = "delete ignore from test_specify_column where id = 1";
        assert runScript(scriptResource, sql, p1);
        sql = "delete from test_specify_column where id = 1";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void deleteIgnore_2() throws IOException {
        String sql = null;

        sql = "delete ignore from test_specify_column where id = 1";
        assert !runScript(scriptResource, sql, p2);
        sql = "delete from test_specify_column where id = 1";
        assert runScript(scriptResource, sql, p2);
    }

}
