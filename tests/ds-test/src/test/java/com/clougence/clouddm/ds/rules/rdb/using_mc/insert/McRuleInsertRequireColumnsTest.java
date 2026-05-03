package com.clougence.clouddm.ds.rules.rdb.using_mc.insert;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_mc.McAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class McRuleInsertRequireColumnsTest extends McAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/insert/insert-require-columns.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("require", "true", "columns", "id,create_gmt,modify_gmt");
    private final Map<String, String> p2             = CollectionUtils.asMap("require", "false", "columns", "id,create_gmt,modify_gmt");
    private final Map<String, String> p3             = CollectionUtils.asMap("require", "true", "columns", "id");
    private final Map<String, String> p4             = CollectionUtils.asMap("require", "false", "columns", "id");

    @Test
    public void requireColumns_1() throws IOException {
        String sql = null;

        sql = "insert into table1(id,bb) values(1,2,3)";
        assert !runScript(scriptResource, sql, p1);
        sql = "insert into table1(id,bb) values(1,2,3)";
        assert !runScript(scriptResource, sql, p1);
        sql = "insert into table1(id,create_gmt,modify_gmt) values(1,2,3)";
        assert runScript(scriptResource, sql, p1);
        sql = "insert into table1 values(1,2,3)";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void requireColumns_2() throws IOException {
        String sql = null;

        sql = "insert into table1(id,bb) values(1,2,3)";
        assert !runScript(scriptResource, sql, p2);
        sql = "insert into table1(id,bb) values(1,2,3)";
        assert !runScript(scriptResource, sql, p2);
        sql = "insert into table1(id,create_gmt,modify_gmt) values(1,2,3)";
        assert runScript(scriptResource, sql, p2);
        sql = "insert into table1 values(1,2,3)";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void requireColumns_3() throws IOException {
        String sql = null;

        sql = "insert into table1(id,bb) values(1,2,3)";
        assert runScript(scriptResource, sql, p3);
        sql = "insert into table1(id,bb) values(1,2,3)";
        assert runScript(scriptResource, sql, p3);
        sql = "insert into table1(id,create_gmt,modify_gmt) values(1,2,3)";
        assert runScript(scriptResource, sql, p3);
        sql = "insert into table1 values(1,2,3)";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void requireColumns_4() throws IOException {
        String sql = null;

        sql = "insert into table1(id,bb) values(1,2,3)";
        assert runScript(scriptResource, sql, p4);
        sql = "insert into table1(id,bb) values(1,2,3)";
        assert runScript(scriptResource, sql, p4);
        sql = "insert into table1(id,create_gmt,modify_gmt) values(1,2,3)";
        assert runScript(scriptResource, sql, p4);
        sql = "insert into table1 values(1,2,3)";
        assert runScript(scriptResource, sql, p4);
    }
}
