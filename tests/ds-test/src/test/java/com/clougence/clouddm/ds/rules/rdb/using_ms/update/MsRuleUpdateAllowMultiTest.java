package com.clougence.clouddm.ds.rules.rdb.using_ms.update;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ms.MsAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MsRuleUpdateAllowMultiTest extends MsAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/update/update-allow-multi.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void testLimit_1() throws IOException {
        String sql = null;

        sql = "update table1 set a=1";
        assert runScript(scriptResource, sql, p1);

    }
}
