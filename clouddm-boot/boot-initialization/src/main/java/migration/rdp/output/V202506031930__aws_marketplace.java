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

public class V202506031930__aws_marketplace extends BaseJavaMigration {

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
        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_market_sub`\n" +
                "    (\n" +
                "        `id`                         bigint                                  NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`                 datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified`               datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `uid`                        varchar(127) NOT NULL,\n" +
                "        `market_type`                varchar(255) NOT NULL,\n" +
                "        `aws_customer_id`            varchar(255) DEFAULT NULL,\n" +
                "        `aws_product_code`           varchar(255) DEFAULT NULL,\n" +
                "        `aws_account_id`             varchar(255) DEFAULT NULL,\n" +
                "        `sub_status`                 varchar(64)  DEFAULT 'SUBSCRIBE',\n" +
                "        PRIMARY KEY (`id`),\n" +
                "        KEY `idx_uid` (`uid`),\n" +
                "        KEY `idx_aws_uniq` (`aws_customer_id`, `aws_product_code`,`aws_account_id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "         DEFAULT CHARSET = utf8mb4");

        sqls.add("ALTER TABLE `rdp_user`\n" +
                "        ADD COLUMN `marketplace_type` varchar(64) DEFAULT 'NONE'");
    }
}