/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.clouddm.init.constant.InitSeedConstants;

public class V202605070002__init_data extends AbstractUpgradeJavaMigration {

    private static final long   ADMIN_ROLE_ID   = 1L;
    private static final String ADMIN_ROLE_NAME = "Manager";

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
        String encryptedSecretKey = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(InitSeedConstants.DEFAULT_PRIMARY_SECRET_KEY);
        return """
                    INSERT INTO `rdp_user` (`id`,`gmt_create`, `gmt_modified`, `uid`, `username`, `email`, `phone`, `sub_account`,
                                                   `company`, `password`, `op_password`, `role_id`, `access_key`, `secret_key`,
                                                   `last_try_login_time`,`login_fail_count`, `login_locked`, `last_try_op_verify_time`, `op_verify_fail_count`,
                                                   `op_locked`, `account_type`, `user_domain`, `disable`, `parent_id`, `maintainer`, `aliyun_ak`, `aliyun_sk`,
                                                   `last_date_update_aliyun_ak`, `bind_type`, `bind_account`, `phone_area_code`,
                                                   `user_status`, `src`, `client_id`, `keyword`, `contact_me`, `country`)
                        VALUES (1,now(), now(), '%s', 'Trial', '%s', '%s', null, '',
                            '%s', null, %s,
                            '%s',
                            '%s',
                            now(), 0, 0, now(), 0, 0, 'PRIMARY_ACCOUNT', '%s.cdmgr.com', 0, null,
                            0, null, null, now(), 'INTERNAL', null, null, 'NORMAL', null, null, null, 0, null)\
                """.formatted(                               //
                InitSeedConstants.ADMIN_UID,                 //
                adminEmail,                                  //
                InitSeedConstants.DEFAULT_PRIMARY_PHONE,     //
                encodedPassword, ADMIN_ROLE_ID,              //
                InitSeedConstants.DEFAULT_PRIMARY_ACCESS_KEY,//
                encryptedSecretKey,                          //
                InitSeedConstants.ADMIN_UID);
    }
}
