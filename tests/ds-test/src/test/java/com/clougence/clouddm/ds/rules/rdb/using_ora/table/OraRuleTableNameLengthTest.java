package com.clougence.clouddm.ds.rules.rdb.using_ora.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ora.OraAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class OraRuleTableNameLengthTest extends OraAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-name-length.txt";
    private final Map<String, String> p1             = Collections.emptyMap();
    private final Map<String, String> p2             = CollectionUtils.asMap("length", "5");

    @Test
    public void tableNameMaxLength_1() throws IOException {
        String sql = null;

        sql = "create table test.a123456(r_int int not null primary key);";
        assert runScript(scriptResource, sql, p1);
        sql = "rename \"table\" to a123456";
        assert runScript(scriptResource, sql, p1);
        sql = "rename abc.\"table\" to a123456";
        assert runScript(scriptResource, sql, p1);

        sql = "create table test.a123456(r_int int not null primary key);";
        assert !runScript(scriptResource, sql, p2);
        sql = "rename \"table\" to a123456";
        assert !runScript(scriptResource, sql, p2);
    }
}
