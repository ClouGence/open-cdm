package com.clougence.clouddm.ds.rules.rdb.using_dr.table;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_dr.DrAbstractRuleTest;

public class DrRuleTableNeedCommentTest extends DrAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/table/table-need-comment.txt";
    private final Map<String, String> p1             = Collections.emptyMap();

    @Test
    public void tableNeedComment_1() throws IOException {
        String sql = null;

        sql = "create table test.abc_test(id int(4)  key auto_increment) ";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int(4)  key auto_increment) comment '   '";
        assert !runScript(scriptResource, sql, p1);
        sql = "create table test.abc_test(id int(4)  key auto_increment) comment 'abc'";
        assert runScript(scriptResource, sql, p1);

        sql = "alter table test.abc modify comment '';";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc modify comment '   ';";
        assert !runScript(scriptResource, sql, p1);
        sql = "alter table test.abc modify comment 'abc';";
        assert runScript(scriptResource, sql, p1);
    }
}
