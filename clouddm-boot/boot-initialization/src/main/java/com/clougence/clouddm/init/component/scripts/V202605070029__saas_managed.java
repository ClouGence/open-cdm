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

import com.clougence.clouddm.init.constant.InitSeedConstants;
import com.clougence.utils.ExceptionUtils;

public class V202605070029__saas_managed extends BaseJavaMigration {

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
        sqls.add("INSERT IGNORE INTO `rdp_role` (`id`, `gmt_create`, `gmt_modified`, `owner_uid`, `role_name`, `role_auth_labels`, `alias_name`, `inner_tag`)\n"
                 + "   VALUES (4, now(), now(), '" + InitSeedConstants.ADMIN_UID
                 + "', 'CC_SaaS_Developer', '[\"CC_DATAJOB_MANAGE\",\"CC_DATAJOB_READ\",\"CC_WORKER_READ\",\"RDP_DS_READ\",\"RDP_DS_MANAGE\",\"RDP_ROLE_READ\"]', 'SaaS 开发者角色', '1' )");
    }
}
