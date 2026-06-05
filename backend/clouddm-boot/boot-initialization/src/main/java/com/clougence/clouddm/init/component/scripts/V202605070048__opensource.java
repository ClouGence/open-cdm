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

public class V202605070048__opensource extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `rdp_data_source` DROP COLUMN `region`
                """, """
                    ALTER TABLE `rdp_data_source_history` DROP COLUMN `region`
                """, """
                    drop table rdp_async_task
                """, """
                    drop table rdp_csrf_token
                """, """
                    CREATE TABLE IF NOT EXISTS dm_csrf_token
                        (
                            id           bigint       NOT NULL AUTO_INCREMENT primary key,
                            gmt_create   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            gmt_modified datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            token        varchar(127) NOT NULL,
                            jump_url     text,
                            secret_token text
                        )
                """, """
                    drop table rdp_product_cluster
                """, """
                    drop table rdp_product
                """, """
                    drop table rdp_sso_register
                """, """
                    drop table rdp_user_download
                """, """
                    drop table rdp_auth_version_field
                """, """
                    drop table rdp_data_source_history
                """, """
                    drop table rdp_market_sub
                """, """
                    drop table rdp_apply_code_info
                """, """
                    drop table rdp_apply_collect_info
                """, """
                    drop table rdp_auth_code_info
                """, """
                    drop table rdp_auth_result_info
                """, """
                    drop table dm_res_auth
                """, """
                    rename table rdp_res_auth to dm_res_auth;
                """, """
                    drop table dm_console_heartbeat
                """, """
                    drop table dm_ds_statistics
                """);
    }
}
