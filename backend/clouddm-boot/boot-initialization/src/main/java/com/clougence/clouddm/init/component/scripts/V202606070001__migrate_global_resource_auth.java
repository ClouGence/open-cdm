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

public class V202606070001__migrate_global_resource_auth extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    INSERT INTO dm_auth_res (
                        gmt_create,
                        gmt_modified,
                        owner_uid,
                        res_id,
                        res_inst_id,
                        res_desc,
                        res_path,
                        level_one,
                        kind_type,
                        res_auth_label
                    )
                    SELECT
                        now(),
                        now(),
                        u.uid,
                        0,
                        'ALL',
                        'ALL',
                        '/',
                        '/',
                        'DataSource',
                        '["RDP_DAUTH_DS_READ","RDP_DATA_DS_MANAGER","DM_QUERY","DM_CALL","DM_DML","DM_DDL","DM_OBJ","DM_SPACE","DM_DCL","DM_OTHER","DM_SENSITIVE","DM_TICKET"]'
                    FROM dm_auth_user u
                    WHERE u.resource_manage_enable = 1
                      AND NOT EXISTS (
                          SELECT 1
                          FROM dm_auth_res r
                          WHERE r.owner_uid = u.uid
                            AND r.kind_type = 'DataSource'
                            AND r.res_id = 0
                            AND r.res_path = '/'
                      )
                """, """
                    ALTER TABLE dm_auth_user DROP COLUMN resource_manage_enable
                """);
    }
}
