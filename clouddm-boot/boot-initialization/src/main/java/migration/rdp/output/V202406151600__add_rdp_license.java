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

public class V202406151600__add_rdp_license extends BaseJavaMigration {

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
        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_auth_version_field`\n" +
                "    (\n" +
                "        `id` int(11)                     NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "        `gmt_modified` datetime          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `license_version` varchar(32)    NOT NULL,\n" +
                "        `fields` text                    NOT NULL,\n" +
                "        UNIQUE license_version_unique(license_version),\n" +
                "        PRIMARY KEY (`id`)\n" +
                "    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4");

        sqls.add(" CREATE TABLE IF NOT EXISTS `rdp_auth_code_info`\n" +
                "    (\n" +
                "        `id`                  int(11)                                NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`          datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "        `gmt_modified`        datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `auth_code`           varchar(128)                           NOT NULL,\n" +
                "        `second_auth_code`    text                                   NOT NULL,\n" +
                "        `remind`              boolean                                NOT NULL DEFAULT FALSE,\n" +
                "        PRIMARY KEY (`id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "      DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_apply_code_info`\n" +
                "    (\n" +
                "        `id`                    int(11)                                NOT NULL AUTO_INCREMENT,\n" +
                "        `gmt_create`            datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "        `gmt_modified`          datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,\n" +
                "        `report_time`           datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "        `cluster_ip`            text                                   NOT NULL,\n" +
                "        `cluster_mac_address`   text                                   NOT NULL,\n" +
                "        `cluster_hardware_uuid` text                                   NOT NULL,\n" +
                "        `info`                  text                                   NOT NULL,\n" +
                "        `apply_type`            varchar(32)                            NOT NULL,\n" +
                "        PRIMARY KEY (`id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "    DEFAULT CHARSET = utf8mb4");

        sqls.add("CREATE TABLE IF NOT EXISTS `rdp_auth_result_info`\n" +
                "    (\n" +
                "    `id`                    int(11)                                NOT NULL AUTO_INCREMENT,\n" +
                "    `gmt_create`            datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "    `gmt_modified`          datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,\n" +
                "    `report_time`           datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "    `active`                boolean                                NOT NULL DEFAULT FALSE,\n" +
                "    `auth_result_status`    varchar(32)                            NOT NULL,\n" +
                "    `msg`                   text                                   NOT NULL,\n" +
                "    `success`               boolean                                NOT NULL DEFAULT FALSE,\n" +
                "    PRIMARY KEY (`id`)\n" +
                "    ) ENGINE = InnoDB\n" +
                "    DEFAULT CHARSET = utf8mb4");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBy6FoCIkoegei97YUFRSW0w=')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.2.5',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBy6FoCIkoegei97YUFRSW0w=')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.3.1',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcXiH0+o2ZgPhdQOHbFaVg90')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.3.2',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxW3IDq62Ihy0Degb6DkiDww==')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.3.3',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxW3IDq62Ihy0Degb6DkiDww==')");

        sqls.add(" INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.3.4',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.4',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.5',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.6',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.7',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('2.8',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxhJqf2KT0SK3IhF0+tumfjg==')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('3.0',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRG3Ak108G26AY4vsbVzn5s4=')");

        sqls.add("INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('3.2.1',\n" +
                "            'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRF3SMmNlERlBapOkgBCwi0pD2ntXtj4DoTwsJKRmYXcD')");

        sqls.add(" INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)\n" +
                "    VALUES ('4.2.0',\n" +
                "             'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRF3SMmNlERlBapOkgBCwi0ra3qAykB1qMpacQ2euFy5DFXecoBGSoJLICssoHU+gIi0tY3HHABeCTx/JQw5rXAA=')");
    }
}