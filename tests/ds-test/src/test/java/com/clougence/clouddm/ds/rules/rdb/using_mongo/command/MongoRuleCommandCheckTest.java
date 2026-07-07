package com.clougence.clouddm.ds.rules.rdb.using_mongo.command;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_mongo.MongoAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MongoRuleCommandCheckTest extends MongoAbstractRuleTest {

    private final String              scriptResource = "rule-test/special/mongo/func-check.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap(//
            "checkMode", "Allow",//
            "functions", "find;deleteOne");
    private final Map<String, String> p2             = CollectionUtils.asMap(//
            "checkMode", "Reject",//
            "functions", "find;deleteOne");

    @Test
    public void commandCheck_1() throws IOException {
        String sql = null;

        sql = "db.aa.find()";
        assert runScript(scriptResource, sql, p1);
        sql = "db.aa.findOne()";
        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void commandCheck_2() throws IOException {
        String sql = null;

        sql = "db.aa.find()";
        assert !runScript(scriptResource, sql, p2);
        sql = "db.aa.drop()";
        assert runScript(scriptResource, sql, p2);
    }
}
