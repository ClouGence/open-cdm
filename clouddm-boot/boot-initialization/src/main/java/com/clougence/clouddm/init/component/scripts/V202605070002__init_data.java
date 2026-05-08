package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.clouddm.init.constant.InitSeedConstants;

public class V202605070002__init_data extends AbstractUpgradeJavaMigration {

    private static final long   ADMIN_ROLE_ID              = 1L;
    private static final String ADMIN_ROLE_NAME            = "Manager";
    private static final String DEFAULT_PRIMARY_ACCESS_KEY = "ak0a2c62tdo1ap2416655mpyx0v36l359p1v5rn782caw8t0qkk1s94b80lfs90";
    private static final String DEFAULT_PRIMARY_SECRET_KEY = "sk6206iy4pb0eydz9hg97jo3tu5d80j97e91bbql65167u8wb75x4ej6e4v4aa4";

    @Override
    public List<String> collectScript() {
        return List.of(buildInitAdminRoleSql(), buildInitPrimaryUserSql(), """
                    INSERT INTO `rdp_ds_env` (`id`,`gmt_create`,`gmt_modified`,`owner_uid`,`env_name`,`description`) values (1,now(),now(),'%s','Default','Default Environment')\
                """.formatted(InitSeedConstants.ADMIN_UID));
    }

    private static String buildInitAdminRoleSql() {
        return """
                INSERT INTO `rdp_role` (`id`,`gmt_create`,`gmt_modified`,`owner_uid`,`role_name`,`role_auth_labels`,`alias_name`,`inner_tag`) VALUES (%s, now(), now(), '%s', '%s', '[]', '%s', 1)\
                """
            .formatted(ADMIN_ROLE_ID, InitSeedConstants.ADMIN_UID, ADMIN_ROLE_NAME, ADMIN_ROLE_NAME);
    }

    private static String buildInitPrimaryUserSql() {
        String adminEmail = InitSeedConstants.escapeSqlLiteral(InitSeedConstants.resolveAdminEmail());
        String encodedPassword = CryptService.INSTANCE.encryptForOneWay(InitSeedConstants.resolveAdminPassword()).getEncryptPassword();
        String encryptedSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(DEFAULT_PRIMARY_SECRET_KEY);
        return """
                    INSERT INTO `rdp_user` (`id`,`gmt_create`, `gmt_modified`, `uid`, `username`, `email`, `phone`, `sub_account`,
                                                   `company`, `password`, `op_password`, `role_id`, `access_key`, `secret_key`,
                                                   `last_try_login_time`,`login_fail_count`, `login_locked`, `last_try_op_verify_time`, `op_verify_fail_count`,
                                                   `op_locked`, `account_type`, `user_domain`, `disable`, `parent_id`, `maintainer`, `aliyun_ak`, `aliyun_sk`,
                                                   `last_date_update_aliyun_ak`, `bind_type`, `bind_account`, `phone_area_code`,
                                                   `user_status`, `src`, `client_id`, `keyword`, `contact_me`, `country`)
                        VALUES (1,now(), now(), '%s', 'Trial', '%s', '12345678900', null, '',
                            '%s', null, %s,
                            '%s',
                            '%s',
                            now(), 0, 0, now(), 0, 0, 'PRIMARY_ACCOUNT', '%s.clougence.com', 0, null,
                            0, null, null, now(), 'INTERNAL', null, null, 'NORMAL', null, null, null, 0, null)\
                """.formatted(InitSeedConstants.ADMIN_UID, adminEmail, encodedPassword, ADMIN_ROLE_ID, DEFAULT_PRIMARY_ACCESS_KEY, encryptedSecretKey, InitSeedConstants.ADMIN_UID);
    }
}
