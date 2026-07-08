package com.clougence.clouddm.ds.rules.rdb.using_redis.command;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_redis.RedisAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class RedisRuleCommandCheckTest extends RedisAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/redis/command-check.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap(//
            "checkMode", "allow",//
            "commands", "get;set");
    private final Map<String, String> p2             = CollectionUtils.asMap(//
            "checkMode", "Reject",//
            "commands", "get;set");

    @Test
    public void commandCheck_1() throws IOException {
        String sql = null;

        sql = "get abc";
        assert runScript(scriptResource, sql, p1);
        sql = "del abc";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void commandCheck_2() throws IOException {
        String sql = null;

        sql = "get abc";
        assert !runScript(scriptResource, sql, p2);
        sql = "del abc";
        assert runScript(scriptResource, sql, p2);
    }
}
