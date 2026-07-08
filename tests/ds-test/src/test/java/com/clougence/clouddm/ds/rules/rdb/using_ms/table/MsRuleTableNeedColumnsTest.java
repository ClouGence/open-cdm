package com.clougence.clouddm.ds.rules.rdb.using_ms.table;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ms.MsAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MsRuleTableNeedColumnsTest extends MsAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-need-columns.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("columns", "a,c");

    @Test
    public void needColumns_1() throws IOException {
        String sql = null;

        sql = "create table test.abc(a int,b int,c int,d int)";
        assert runScript(scriptResource, sql, p1);

        sql = "create table test.abc(a int,b int,d int)";
        assert !runScript(scriptResource, sql, p1);
    }
}
