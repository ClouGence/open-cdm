package com.clougence.clouddm.ds.rules.rdb.using_sr.insert;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_sr.SrAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class SrRuleInsertAllowWithTest extends SrAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/insert/insert-allow-with.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "false");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "true");

    @Test
    public void selectAndWithTest_1() throws IOException {
        //        String sql = null;
        //
        //        sql = "insert into table1 select * from table2";
        //        assert runScript(scriptResource, sql, p1);
        //        sql = "insert into `table2` " + //
        //              "with tab1Cnt as (select id,name from table1) " + //
        //              "select * from tab1Cnt;";
        //        assert !runScript(scriptResource, sql, p1);
        //        sql = "insert into table1 select *\n" +//
        //              "from (with tab1Cnt as (select id, name from table1) select * from tab1Cnt);";
        //        assert !runScript(scriptResource, sql, p1);
    }

    @Test
    public void selectAndWithTest_2() throws IOException {
        //        String sql = null;
        //
        //        sql = "insert into table1 select * from table2";
        //        assert runScript(scriptResource, sql, p2);
        //        sql = "insert into `table2` " + //
        //              "with tab1Cnt as (select id,name from table1) " + //
        //              "select * from tab1Cnt;";
        //        assert runScript(scriptResource, sql, p2);
        //        sql = "insert into table1 select *\n" +//
        //              "from (with tab1Cnt as (select id, name from table1) select * from tab1Cnt);";
        //        assert runScript(scriptResource, sql, p1);
    }
}
