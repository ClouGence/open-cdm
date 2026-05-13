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

public class V202605130001__worker_liveness_unify extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `dm_worker`
                        ADD COLUMN `conn_status` varchar(64) NOT NULL DEFAULT 'NEW' AFTER `worker_state`,
                        ADD COLUMN `last_heartbeat_report_ms` bigint DEFAULT NULL AFTER `conn_status`,
                        ADD COLUMN `last_heartbeat_ping_ms` bigint DEFAULT NULL AFTER `last_heartbeat_report_ms`
                """, """
                    ALTER TABLE `dm_worker`
                        ADD KEY `idx_cluster_conn_status` (`cluster_id`, `conn_status`),
                        ADD KEY `idx_conn_hb` (`conn_status`, `last_heartbeat_report_ms`)
                """, """
                    drop table if exists dm_worker_heartbeat
                """, """
                    drop table if exists dm_worker_status
                """);
    }
}
