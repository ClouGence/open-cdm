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

public class V202605070014__add_sso_type extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        Connection c = context.getConnection();
        tryRenameColumn(c);
        for (String sql : SQLS) {
            safeExecute(c, sql);
        }
    }

    private static final Set<Integer> ERROR_CODES = new HashSet<>();
    static {
        ERROR_CODES.add(1060);
        ERROR_CODES.add(1061);
        ERROR_CODES.add(1062);
        ERROR_CODES.add(1050);
        ERROR_CODES.add(1054);
        ERROR_CODES.add(1072);
        ERROR_CODES.add(1091);
    }

    private void tryRenameColumn(Connection conn) {
        try {
            safeExecute(conn, "ALTER TABLE `rdp_user` RENAME COLUMN wechat_union_id to union_id");
        } catch (RuntimeException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof SQLException sqlEx) {
                int errorCode = sqlEx.getErrorCode();
                if (1064 == errorCode) {
                    // 1064 = You have an error in your SQL syntax; check the manual that
                    safeExecute(conn, "ALTER TABLE `rdp_user` CHANGE COLUMN wechat_union_id union_id varchar(128) CHARACTER SET utf8mb4 NULL AFTER `country`");
                    return;
                }
            }
            throw e;
        }
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
            if (ERROR_CODES.contains(e.getErrorCode())) {
                System.out.println("Flyway java exec error but skip, msg:" + ExceptionUtils.getRootCauseMessage(e) + ", sql: " + sql);
                return;
            }

            throw new RuntimeException("Failed to execute: " + sql, e);
        }
    }

    private static final List<String> SQLS = new ArrayList<>();
    static {
        SQLS.add("ALTER TABLE `rdp_user` MODIFY COLUMN union_id varchar(128) CHARACTER SET utf8mb4 NULL AFTER `country`");

        SQLS.add("ALTER TABLE `rdp_user` ADD COLUMN `sso_type` varchar(128) NULL AFTER `union_id`");
    }
}
