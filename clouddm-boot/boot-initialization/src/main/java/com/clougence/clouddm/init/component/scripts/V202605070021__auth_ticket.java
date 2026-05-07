package com.clougence.clouddm.init.component.scripts;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import com.clougence.utils.ExceptionUtils;

public class V202605070021__auth_ticket extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        Connection c = context.getConnection();
        for (String sql : sqls) {
            safeExecute(c, sql);
        }
    }

    private static final Set<Integer> errorCodes = new HashSet<>();
    static {
        errorCodes.add(1060);
        errorCodes.add(1061);
        errorCodes.add(1062);
        errorCodes.add(1050);
        errorCodes.add(1072);
        errorCodes.add(1091);
    }

    private void safeExecute(Connection conn, String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            // MySQL / OceanBase / TiDB
            // 1060 = Duplicate column name
            // 1050 = Table exists (for CREATE)
            // 1061 = Duplicate key name
            // 1091 = Can't DROP ... ; check that column/key exists
            if (errorCodes.contains(e.getErrorCode())) {
                System.out.println("Flyway java exec error but skip, msg:" + ExceptionUtils.getRootCauseMessage(e) + ", sql: " + sql);
                return;
            }

            throw new RuntimeException("Failed to execute: " + sql, e);
        }
    }

    private static final List<String> sqls = new ArrayList<>();
    static {
        sqls.add("create table if not exists rdp_ticket_auth\n" + "    (\n" + "        id                bigint auto_increment primary key,\n"
                 + "        gmt_create        datetime   default CURRENT_TIMESTAMP not null,\n"
                 + "        gmt_modified      datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,\n"
                 + "        rdp_ticket_ins_id varchar(64)                          not null,\n" + "        apply_auth_info   longtext                             not null,\n"
                 + "        kind_type         varchar(64)                          not null,\n" + "        deleted           tinyint(1) default 0                 not null,\n"
                 + "        constraint idx_unique_biz_id unique (rdp_ticket_ins_id)\n" + "    )");

        sqls.add("alter table rdp_ticket_inst modify bind_ds_id bigint null");

        sqls.add("drop index uk_path on rdp_res_auth");

        sqls.add("create index idx_id_path_type_uid on rdp_res_auth (res_id, res_path, kind_type, owner_uid)");
    }
}
