package com.clougence.clouddm.ds.rules.rdb.using_ora.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ora.OraAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class OraRuleColumnVarCharLengthTest extends OraAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-varchar-length.txt";
    private final Map<String, String> p1             = Collections.emptyMap();
    private final Map<String, String> p2             = CollectionUtils.asMap("allowEmpty", "true");
    private final Map<String, String> p3             = CollectionUtils.asMap("length", "5");

    @Test
    public void columnVarchar_1() throws IOException {
        String sql = null;

        sql = "alter table test add a123 varchar(10);";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnVarchar_2() throws IOException {
        String sql = null;

        sql = "alter table test add a123 varchar(10);";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void columnVarchar_3() throws IOException {
        String sql = null;
        sql = "alter table test add a123 varchar(10);";
        assert !runScript(scriptResource, sql, p3);
    }

    @Test
    public void columnNVarchar_1() throws IOException {
        String sql = null;

        sql = "alter table test add a123 nvarchar2(10);";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void columnNVarchar_2() throws IOException {
        String sql = null;

        sql = "alter table test add a123 nvarchar2(10);";
        assert runScript(scriptResource, sql, p2);
    }

    @Test
    public void columnNVarchar_3() throws IOException {
        String sql = null;

        sql = "alter table test add a123 nvarchar2(10);";
        assert !runScript(scriptResource, sql, p3);
    }
}
