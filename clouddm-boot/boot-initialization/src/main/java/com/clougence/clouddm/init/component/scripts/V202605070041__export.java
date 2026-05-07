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

public class V202605070041__export extends BaseJavaMigration {

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
            // MySQL / OceanBase 错误码：
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
        sqls.add("create table dm_files\n" + "    (\n" + "        id           bigint       not null auto_increment,\n"
                 + "        gmt_create   datetime     not null default CURRENT_TIMESTAMP,\n" + "        gmt_modified datetime     not null default CURRENT_TIMESTAMP,\n"
                 + "        owner_uid    varchar(36)  not null,\n" + "        user_id      varchar(36)  not null,\n" + "        file_uri     varchar(500) not null,\n"
                 + "        file_format  varchar(200) not null,\n" + "        inner_format tinyint      not null,\n" + "        status       varchar(64)  not null,\n"
                 + "        message      text,\n" + "        unique_id    varchar(36)  not null,\n" + "        heartbeat    datetime     null,\n" + "        PRIMARY KEY (id),\n"
                 + "        unique key unique_id (unique_id)\n" + "    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4");
    }
}
