package com.clougence.clouddm.ds.rules.rdb.using_sr.delete;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_sr.SrAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class SrRuleDeleteAllowWithTest extends SrAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/delete/delete-allow-with.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void selectAndWithTest_1() throws IOException {
        //        String sql = null;
        //
        //        sql = "delete from user_info where user_uuid in (1,2,3)";
        //        assert runScript(scriptResource, sql, p1);
        //        sql = "with tab1Cnt as (select user_uuid from user_info)\n" +//
        //              "delete from user_info where user_uuid in (select * from tab1Cnt);";
        //        assert !runScript(scriptResource, sql, p1);
        //        sql = "delete from user_info where user_uuid in\n" +//
        //              "(with tab1Cnt as (select user_uuid from user_info) select * from tab1Cnt);";
        //        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void selectAndWithTest_2() throws IOException {
        //        String sql = null;
        //
        //        sql = "delete from user_info where user_uuid in (1,2,3)";
        //        assert runScript(scriptResource, sql, p2);
        //        sql = "with tab1Cnt as (select user_uuid from user_info)\n" +//
        //              "delete from user_info where user_uuid in (select * from tab1Cnt);";
        //        assert !runScript(scriptResource, sql, p2);
        //        sql = "delete from user_info where user_uuid in\n" +//
        //              "(with tab1Cnt as (select user_uuid from user_info) select * from tab1Cnt);";
        //        assert !runScript(scriptResource, sql, p2);
    }
}
