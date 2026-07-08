package com.clougence.clouddm.ds.rules.special.mysql.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;

public class MyRuleTableUseConvertTest extends MyAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mysql/table/table-use-convert.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void tableCollate_1() throws IOException {
        String sql = null;

        sql = "alter table test.abc convert to character set utf8mb4;";
        assert !runScript(scriptResource, sql, p1);

        sql = "alter table test.abc character set utf8mb4;";
        assert !runScript(scriptResource, sql, p1);
    }
}
