package com.clougence.clouddm.ds.rules.rdb.using_db2.column;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_db2.Db2AbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class Db2RuleColumnAllowDropTest extends Db2AbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-allow-drop.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    @Test
    public void dropColumn_1() throws IOException {
        String sql;

        sql = "alter table test.abc drop column id;";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void dropColumn_2() throws IOException {
        String sql;

        sql = "alter table test.abc drop column id;";
        assert !runScript(scriptResource, sql, p2);
    }
}
