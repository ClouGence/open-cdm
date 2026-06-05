package com.clougence.clouddm.ds.rules.rdb.using_db2.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleColumnVarCharLengthTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-varchar-length.txt";
    private final Map<String, String> p1             = Collections.emptyMap();
    private final Map<String, String> p2             = CollectionUtils.asMap("allowEmpty", "true");
    private final Map<String, String> p3             = CollectionUtils.asMap("length", "5");

    @Test
    public void columnVarchar_1() throws IOException {
        String sql;

        sql = "create table test(a123 varchar(1));";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test add a123 varchar(10);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table abc alter column a1 set data type varchar(10);";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnVarchar_2() throws IOException {
        String sql;

        sql = "create table test(a123 varchar(1));";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table test add a123 varchar(10);";
        assert runScript(scriptResource, sql, p2);
        sql = "alter table abc alter column a1 set data type varchar(10);";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void columnVarchar_3() throws IOException {
        String sql;

        sql = "create table test(a123 varchar(1));";
        assert runScript(scriptResource, sql, p3);
        sql = "alter table test add a123 varchar(10);";
        assert !runScript(scriptResource, sql, p3);
        sql = "alter table abc alter column a1 set data type varchar(10);";
        assert !runScript(scriptResource, sql, p3);
    }
}
