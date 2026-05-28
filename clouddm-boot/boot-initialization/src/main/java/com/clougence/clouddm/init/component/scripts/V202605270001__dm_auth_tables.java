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

public class V202605270001__dm_auth_tables extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    RENAME TABLE rdp_user TO dm_auth_user
                """, """
                    RENAME TABLE rdp_role TO dm_auth_role
                """, """
                    RENAME TABLE rdp_user_mfa TO dm_auth_mfa
                """, """
                    RENAME TABLE rdp_verify_code TO dm_auth_verify
                """, """
                    RENAME TABLE rdp_ticket_auth TO dm_auth_approval
                """, """
                    RENAME TABLE dm_res_auth TO dm_auth_res
                """, """
                    RENAME TABLE dm_csrf_token TO dm_auth_csrf_token
                """, """
                    RENAME TABLE rdp_user_auth TO dm_auth_user_auth
                """);
    }
}
