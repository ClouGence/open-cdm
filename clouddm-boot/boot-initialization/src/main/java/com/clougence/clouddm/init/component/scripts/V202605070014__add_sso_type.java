package com.clougence.clouddm.init.component.scripts;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.utils.ExceptionUtils;

public class V202605070014__add_sso_type extends AbstractUpgradeJavaMigration {

    @Override
    protected void beforeMigrate(Connection conn) {
        tryRenameColumn(conn);
    }

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `rdp_user` MODIFY COLUMN union_id varchar(128) CHARACTER SET utf8mb4 NULL AFTER `country`\
                """, """
                    ALTER TABLE `rdp_user` ADD COLUMN `sso_type` varchar(128) NULL AFTER `union_id`\
                """);
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
}
