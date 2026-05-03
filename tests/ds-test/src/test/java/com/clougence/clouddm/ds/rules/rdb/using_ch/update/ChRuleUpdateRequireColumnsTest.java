package com.clougence.clouddm.ds.rules.rdb.using_ch.update;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ch.ChAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class ChRuleUpdateRequireColumnsTest extends ChAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/update/update-require-columns.txt";
    private final Map<String, String> p1             = Collections.emptyMap();
    private final Map<String, String> p2             = CollectionUtils.asMap("columns", "modify_gmt");

    @Test
    public void requireColumns_1() throws IOException {
        String sql = null;

        sql = "update table1  set a=1 where 1=1";
        assert runScript(scriptResource, sql, p1);
        sql = "update table1 set id=1,create_gmt=3  where id = 1";
        assert runScript(scriptResource, sql, p1);
        sql = "update table1 set id=1,create_gmt=3,modify_gmt=3 where id = 1";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void requireColumns_2() throws IOException {
        String sql = null;

        sql = "update  table1  set a=1 where id = 1";
        assert !runScript(scriptResource, sql, p2);
        sql = "update table1 set id=1,create_gmt=3  where id = 1";
        assert !runScript(scriptResource, sql, p2);
        sql = "update table1 set id=1,create_gmt=3,modify_gmt=3  where id = 1";
        assert runScript(scriptResource, sql, p2);
    }
}
