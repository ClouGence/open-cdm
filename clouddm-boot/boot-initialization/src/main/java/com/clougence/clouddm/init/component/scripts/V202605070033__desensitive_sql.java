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

public class V202605070033__desensitive_sql extends BaseJavaMigration {

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
        sqls.add("drop table if exists `data_handle_config`");

        sqls.add("drop table if exists `data_handle_package`");

        sqls.add("drop table if exists `data_desensitize_rule`");

        sqls.add("alter table `dm_sec_rules`\n" + "        add `rule_md5` varchar(36) null,\n" + "        add `rule_id`  varchar(36) null");

        sqls.add("create index `dm_sec_rules_rule_id` on `dm_sec_rules` (`rule_id`)");

        sqls.add("CREATE TABLE `dm_sec_sensitive`\n" + "    (\n" + "        `id`           bigint       NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + "        `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `owner_uid`    varchar(255)          DEFAULT NULL,\n" + "        `sen_id`       varchar(36)  NOT NULL,\n"
                 + "        `sen_name`     varchar(255) NOT NULL,\n" + "        `sen_desc`     text,\n" + "        `sen_type`     varchar(64)  not null,\n"
                 + "        `sen_def`      text         NOT NULL,\n" + "        `sen_content`  text         NOT NULL,\n" + "        `sen_md5`      varchar(36)  NOT NULL,\n"
                 + "        `inner_share`  tinyint      NOT NULL,\n" + "        `sen_mode`     varchar(12)  NOT NULL,\n" + "        PRIMARY KEY (`id`),\n"
                 + "        index `dm_sec_sensitive_sen_id` (`sen_id`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("ALTER TABLE `dm_sec_referer`\n" + "        ADD `ref_kind` VARCHAR(12) NOT NULL,\n" + "        ADD `ref_md5`  varchar(36) NULL,\n"
                 + "        ADD `sen_mode` varchar(12) NULL");

        sqls.add("drop index `dm_sec_referer_refs` ON `dm_sec_referer`");

        sqls.add("create index `dm_sec_referer_refs` ON `dm_sec_referer` (`ref_rule`, `ref_spec`, `rule_kind`)");

        sqls.add("update `dm_sec_referer` set `ref_kind` = 'QUERY' where `ref_kind` is null");

        sqls.add("CREATE TABLE `dm_sec_range`\n" + "    (\n" + "        `id`           bigint      NOT NULL AUTO_INCREMENT,\n"
                 + "        `gmt_create`   datetime     DEFAULT CURRENT_TIMESTAMP,\n" + "        `gmt_modified` datetime     DEFAULT CURRENT_TIMESTAMP,\n"
                 + "        `owner_uid`    varchar(255) DEFAULT NULL,\n" + "        `ref_spec`     bigint      NOT NULL,\n" + "        `ref_id`       bigint      NOT NULL,\n"
                 + "        `range_type`   varchar(24) NOT NULL,\n" + "        `match_mode`   varchar(12) NOT NULL,\n" + "        `level_prefix` text        NOT NULL,\n"
                 + "        `level_nodes`  text        NOT NULL,\n" + "        `choose_all`   tinyint     NOT NULL,\n" + "        `table_level`  varchar(64) NULL,\n"
                 + "        `ref_ds_type`  varchar(64) NULL,\n" + "        PRIMARY KEY (`id`),\n" + "        index `dm_sec_range_by_id` (`owner_uid`, `ref_spec`, `ref_id`),\n"
                 + "        index `dm_sec_range_by_spec` (`owner_uid`, `ref_spec`)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");
    }
}
