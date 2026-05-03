package migration.rdp.output;

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

public class V202507072130__mfa extends BaseJavaMigration {

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
        sqls.add("ALTER TABLE `rdp_user` ADD COLUMN `use_mfa` tinyint(1) NOT NULL DEFAULT 0");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_user_mfa`\n" +
                "    (\n" +
                "        `id`               bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `user_id`          bigint(20)   NOT NULL,\n" +
                "        `uid`              varchar(127) NOT NULL,\n" +
                "        `mfa_status`       varchar(127) NOT NULL,\n" +
                "        `mfa_key`          varchar(512) NOT NULL,\n" +
                "        `reset_mfa_key`    varchar(512) DEFAULT NULL,\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_user_id` (`user_id`),\n" +
                "        KEY `idx_uid` (`uid`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");
    }
}