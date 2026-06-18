package com.clougence.clouddm.ds.rules.rdb.using_pg.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_pg.PgAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class PgRuleColumnCharLengthTest extends PgAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-char-length.txt";
    private final Map<String, String> p1             = Collections.emptyMap();
    private final Map<String, String> p2             = CollectionUtils.asMap("allowEmpty", "true");
    private final Map<String, String> p3             = CollectionUtils.asMap("length", "5");

    @Test
    public void columnChar_1() throws IOException {
        String sql = null;

        sql = "create table test(a123 char);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test add a123 char(10);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test alter column a1 type char(10);";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnChar_2() throws IOException {
        String sql = null;

        sql = "create table test(a123 char);";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test add a123 char(10);";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test alter column a1 type char(10);";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void columnChar_3() throws IOException {
        String sql = null;

        sql = "create table test(a123 char);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test add a123 char(10);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test alter column a1 type char(10);";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void columnNChar_1() throws IOException {
        String sql = null;

        sql = "create table test(a123 nchar);";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test add a123 nchar(10);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test alter column a1 type nchar(10);";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnNChar_2() throws IOException {
        String sql = null;

        sql = "create table test(a123 nchar);";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test add a123 nchar(10);";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test alter column a1 type nchar(10);";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void columnNChar_3() throws IOException {
        String sql = null;

        sql = "create table test(a123 nchar);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test add a123 nchar(10);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table test alter column a1 type nchar(10);";
        assert !runScript(scriptResource, sql, p3);
    }
}
