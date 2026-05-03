package com.clougence.clouddm.ds.rules.rdb.using_ms.constraint;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_ms.MsAbstractRuleTest;
import com.clougence.utils.CollectionUtils;

public class MsRuleConstraintAllowForeignTest extends MsAbstractRuleTest {

    private final String              scriptResource = "rule-test/rdb/constraint/constraint-allow-foreign.txt";
    private final Map<String, String> p1             = CollectionUtils.asMap("allow", "true");
    private final Map<String, String> p2             = CollectionUtils.asMap("allow", "false");

    @Test
    public void tableNeedForeign_1() throws IOException {
        String sql = null;

        sql = "create table test.abc(r_int int not null primary key,r_k1 int,r_k2 int,r_name  varchar(100) null,constraint ptr foreign key (r_k1, r_k2) references fk_table (c_id, c_name));";
        assert runScript(scriptResource, sql, p1);
        sql = "alter table test.abc add constraint ptr foreign key (r_k1, r_k2) references fk_table (c_id, c_name);";
        assert runScript(scriptResource, sql, p1);
    }

    @Test
    public void tableNeedForeign_2() throws IOException {
        String sql = null;

        sql = "create table test.abc(r_int int not null primary key,r_k1 int,r_k2 int,r_name  varchar(100) null,constraint ptr foreign key (r_k1, r_k2) references fk_table (c_id, c_name));";
        assert !runScript(scriptResource, sql, p2);
        sql = "alter table test.abc add constraint ptr foreign key (r_k1, r_k2) references fk_table (c_id, c_name);";
        assert !runScript(scriptResource, sql, p2);
    }
}
