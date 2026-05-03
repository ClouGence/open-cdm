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

public class V202408061600__add_apply_collect extends BaseJavaMigration {

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
        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_apply_collect_info`\n" +
                "    (\n" +
                "        `id`                    int                                    NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`            datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "        `gmt_modified`          datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `report_time`           datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "        `apply_id`              int                                    NOT NULL,\n" +
                "        `job_id`                int                                             DEFAULT NULL,\n" +
                "        `task_id`               int                                             DEFAULT NULL,\n" +
                "        `link_type`             text                                            DEFAULT NULL,\n" +
                "        `src_ds_type`           text                                            DEFAULT NULL,\n" +
                "        `dst_ds_type`           text                                            DEFAULT NULL,\n" +
                "        `collect_name`          longtext                                        DEFAULT NULL,\n" +
                "        `collect_value`         longtext                                        DEFAULT NULL,\n" +
                "        PRIMARY KEY (`id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "    DEFAULT CHARSET = utf8mb4");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('4.2.2',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRF3SMmNlERlBapOkgBCwi0ra3qAykB1qMpacQ2euFy5DFXecoBGSoJLICssoHU+gIsVyd1wN8VvrxFb2vi0UilvVcWFAS5rsDm3Qw248InTX')");
    }
}