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

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.clouddm.init.constant.InitSeedConstants;

public class V202606050001__dm_auth_user_account extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        String adminAccount = InitSeedConstants.escapeSqlLiteral(InitSeedConstants.DEFAULT_PRIMARY_ACCOUNT);
        return List.of("""
                    ALTER TABLE dm_auth_user
                        MODIFY COLUMN username varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        MODIFY COLUMN email varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        MODIFY COLUMN phone varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL
                """, """
                    ALTER TABLE dm_auth_user ADD COLUMN account varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL AFTER username
                """, """
                    CREATE INDEX idx_account ON dm_auth_user(account)
                """, String.format("""
                    UPDATE dm_auth_user SET account = '%s' WHERE uid = '9999999999999999' AND (account IS NULL OR account = '')
                """, adminAccount), """
                    UPDATE dm_auth_user SET bind_type = 'INTERNAL' WHERE uid = '9999999999999999'
                """, """
                    ALTER TABLE dm_auth_user DROP COLUMN aliyun_ak
                """, """
                    ALTER TABLE dm_auth_user DROP COLUMN aliyun_sk
                """, """
                    ALTER TABLE dm_auth_user DROP COLUMN last_date_update_aliyun_ak
                """, """
                    UPDATE dm_auth_user
                    SET account = COALESCE(NULLIF(sub_account, ''), LOWER(SUBSTRING(REPLACE(UUID(), '-', ''), 1, 8)))
                    WHERE uid <> '9999999999999999' AND (account IS NULL OR account = '')
                """, """
                    ALTER TABLE dm_auth_user DROP INDEX idx_sub_account
                """, """
                    ALTER TABLE dm_auth_user DROP COLUMN sub_account
                """, """
                    ALTER TABLE dm_auth_user DROP COLUMN user_domain
                """, """
                    ALTER TABLE dm_auth_user ADD COLUMN allow_local tinyint(1) NOT NULL DEFAULT 0 AFTER bind_account
                """, """
                    UPDATE dm_auth_user SET allow_local = 1 WHERE account_type = 'PRIMARY_ACCOUNT' OR bind_type = 'INTERNAL'
                """, """
                    ALTER TABLE dm_auth_user DROP COLUMN company
                """);
    }
}
