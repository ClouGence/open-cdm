package com.clougence.clouddm.ds.rules.rdb.using_pg.delete;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_pg.PgAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class PgRuleDeleteAllowMultiTablesTest extends PgAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/delete/delete-allow-multi-tables.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    @Test
    public void deleteMultiTables_1() throws IOException {
        String sql = null;

        //sql = "delete t1, t2, t3 from table1 t1 join table2 t2 join table3 t3 on t1.id=t2.id where t1.id1 = 1 and t2.id2 = 2;";
        //assert runScript(scriptResource, sql, p1);
        sql = "delete from test_specify_column where id =1";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void deleteMultiTables_2() throws IOException {
        String sql = null;

        //sql = "delete t1, t2, t3 from table1 t1 join table2 t2 join table3 t3 on t1.id=t2.id where t1.id1 = 1 and t2.id2 = 2;";
        //assert !runScript(scriptResource, sql, p2);
        sql = "delete from test_specify_column where id =1";
        assert runScript(scriptResource, sql, p2);
    }
}
