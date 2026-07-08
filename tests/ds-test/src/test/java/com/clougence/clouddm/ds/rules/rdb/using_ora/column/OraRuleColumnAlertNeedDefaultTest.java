package com.clougence.clouddm.ds.rules.rdb.using_ora.column;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ora.OraAbstractRuleTest;

public class OraRuleColumnAlertNeedDefaultTest extends OraAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-alter-need-default.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void columnNeedDefault_1() throws IOException {
        String sql = null;

        // true
        sql = "alter table test.abc modify name varchar(25);";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc modify name varchar(25) default 'aaa';";
        assert runScript(scriptResource, sql, p1);
    }
}
