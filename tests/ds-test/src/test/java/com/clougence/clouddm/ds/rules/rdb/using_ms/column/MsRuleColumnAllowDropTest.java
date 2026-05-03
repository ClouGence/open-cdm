package com.clougence.clouddm.ds.rules.rdb.using_ms.column;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ms.MsAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MsRuleColumnAllowDropTest extends MsAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/column/column-allow-drop.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    @Test
    public void dropColumn_1() throws IOException {
        String sql = null;

        sql = "alter table test.abc drop column id;";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void dropColumn_2() throws IOException {
        String sql = null;

        sql = "alter table test.abc drop column id;";
        assert !runScript(scriptResource, sql, p2);
    }
}
