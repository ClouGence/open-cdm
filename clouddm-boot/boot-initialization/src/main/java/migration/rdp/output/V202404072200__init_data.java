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

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.init.constant.InitSeedConstants;
import com.clougence.utils.ExceptionUtils;

public class V202404072200__init_data extends BaseJavaMigration {
    private static final String DEFAULT_PRIMARY_ACCESS_KEY = "ak0a2c62tdo1ap2416655mpyx0v36l359p1v5rn782caw8t0qkk1s94b80lfs90";
    private static final String DEFAULT_PRIMARY_SECRET_KEY = "sk6206iy4pb0eydz9hg97jo3tu5d80j97e91bbql65167u8wb75x4ej6e4v4aa4";

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
        sqls.add(buildInitPrimaryUserSql());
        sqls.add("INSERT INTO `rdp_ds_env` (`id`,`gmt_create`,`gmt_modified`,`owner_uid`,`env_name`,`description`) values (1,now(),now(),'" + InitSeedConstants.ADMIN_UID + "','Default','Default Environment')");
    }

    private static String buildInitPrimaryUserSql() {
        String adminEmail = InitSeedConstants.escapeSqlLiteral(InitSeedConstants.resolveAdminEmail());
        String encodedPassword = CryptService.INSTANCE.encryptForOneWay(InitSeedConstants.resolveAdminPassword()).getEncryptPassword();
        String encryptedSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(DEFAULT_PRIMARY_SECRET_KEY);
        return "INSERT INTO `rdp_user` (`id`,`gmt_create`, `gmt_modified`, `uid`, `username`, `email`, `phone`, `sub_account`,\n"
               + "                               `company`, `password`, `op_password`, `role_id`, `access_key`, `secret_key`,\n"
               + "                               `last_try_login_time`,`login_fail_count`, `login_locked`, `last_try_op_verify_time`, `op_verify_fail_count`,\n"
               + "                               `op_locked`, `account_type`, `user_domain`, `disable`, `parent_id`, `maintainer`, `aliyun_ak`, `aliyun_sk`,\n"
               + "                               `last_date_update_aliyun_ak`, `bind_type`, `bind_account`, `phone_area_code`,\n"
               + "                               `user_status`, `src`, `client_id`, `keyword`, `contact_me`, `country`)\n"
               + "    VALUES (1,now(), now(), '" + InitSeedConstants.ADMIN_UID + "', 'Trial', '" + adminEmail + "', '12345678900', null, '',\n"
               + "        '" + encodedPassword + "', null, 1,\n"
               + "        '" + DEFAULT_PRIMARY_ACCESS_KEY + "',\n"
               + "        '" + encryptedSecretKey + "',\n"
               + "        now(), 0, 0, now(), 0, 0, 'PRIMARY_ACCOUNT', '" + InitSeedConstants.ADMIN_UID
               + ".clougence.com', 0, null,\n"
               + "        0, null, null, now(), 'INTERNAL', null, null, 'NORMAL', null, null, null, 0, null)";
    }
}
