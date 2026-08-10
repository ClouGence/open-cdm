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

import java.sql.Connection;
import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

/** Consolidates the release flow schema, data, and historical upgrade cleanup. */
public class V202608060002__complete_consolidated_upgrade extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                ALTER TABLE dm_change_flow
                    ADD COLUMN `flow_type` varchar(16) NOT NULL DEFAULT 'SCM'
                        COMMENT 'Change flow type: SCM or BUILT_IN' AFTER `flow_status`,
                    ADD COLUMN `ref_parent_flow_id` bigint DEFAULT NULL
                        COMMENT 'Parent flow for a built-in change flow' AFTER `flow_type`,
                    ADD KEY `idx_flow_parent` (`owner_uid`, `ref_parent_flow_id`, `deleted`)
                """, """
                ALTER TABLE dm_change_flow
                    MODIFY COLUMN `ref_scm_id` bigint DEFAULT NULL,
                    MODIFY COLUMN `ref_scm_type` varchar(12) DEFAULT NULL
                """, """
                ALTER TABLE dm_change
                    ADD COLUMN `ref_batch_id` bigint DEFAULT NULL
                        COMMENT 'Cascade batch' AFTER `ref_flow_id`,
                    ADD COLUMN `ref_parent_change_id` bigint DEFAULT NULL
                        COMMENT 'Source change in the parent flow' AFTER `ref_batch_id`,
                    ADD KEY `idx_change_batch_lock` (`owner_uid`, `ref_batch_id`, `lock_status`),
                    ADD KEY `idx_change_parent` (`owner_uid`, `ref_parent_change_id`)
                """, """
                CREATE TABLE IF NOT EXISTS dm_change_batch
                (
                    id                 bigint      NOT NULL AUTO_INCREMENT,
                    gmt_create         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    gmt_modified       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    owner_uid          varchar(36) NOT NULL,
                    ref_root_flow_id   bigint      NOT NULL,
                    ref_root_change_id bigint      NOT NULL,
                    batch_status       varchar(16) NOT NULL DEFAULT 'RUNNING',
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_change_batch_root(owner_uid, ref_root_change_id),
                    KEY idx_change_batch_running(owner_uid, ref_root_flow_id, batch_status),
                    KEY idx_change_batch_status(batch_status, id)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """, """
                CREATE TABLE IF NOT EXISTS dm_change_transfer
                (
                    id                   bigint       NOT NULL AUTO_INCREMENT,
                    gmt_create           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    gmt_modified         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    owner_uid            varchar(36)  NOT NULL,
                    ref_batch_id         bigint       NOT NULL,
                    ref_source_flow_id   bigint       NOT NULL,
                    ref_source_change_id bigint       NOT NULL,
                    ref_target_flow_id   bigint       NOT NULL,
                    ref_target_change_id bigint       DEFAULT NULL,
                    transfer_status      varchar(16)  NOT NULL DEFAULT 'PENDING',
                    schedule_time        datetime     DEFAULT NULL,
                    try_times            int          NOT NULL DEFAULT 0,
                    last_error           text         DEFAULT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_change_transfer(owner_uid, ref_source_change_id, ref_target_flow_id),
                    KEY idx_change_transfer_schedule(transfer_status, schedule_time),
                    KEY idx_change_transfer_batch(owner_uid, ref_batch_id, transfer_status)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """, """
                ALTER TABLE dm_change
                    ADD COLUMN `trigger_uid` varchar(36) DEFAULT NULL
                        COMMENT 'User who manually triggered the change' AFTER `owner_uid`
                """, """
                ALTER TABLE dm_approval_process_activity
                    ADD COLUMN task_status varchar(16) null
                        COMMENT 'Execution state of a PRE_INIT child task'
                """, """
                ALTER TABLE dm_approval DROP COLUMN error_count
                """, """
                ALTER TABLE dm_approval DROP COLUMN session_id
                """, """
                ALTER TABLE dm_approval DROP COLUMN explain_sql_data
                """, """
                ALTER TABLE dm_approval DROP COLUMN risk_sql_count
                """, """
                ALTER TABLE dm_approval DROP COLUMN expected_exec_time
                """, """
                ALTER TABLE dm_approval DROP COLUMN total_count
                """, """
                ALTER TABLE dm_approval DROP COLUMN checked_info
                """, """
                ALTER TABLE dm_approval DROP COLUMN behaviors
                """, """
                ALTER TABLE dm_change_flow DROP COLUMN flow_check
                """, """
                ALTER TABLE dm_change_flow DROP COLUMN flow_approve
                """, """
                ALTER TABLE dm_change_flow DROP COLUMN flow_execute
                """, """
                ALTER TABLE dm_change DROP COLUMN flow_walked
                """);
    }

    @Override
    protected void afterMigrate(Connection connection) throws Exception {
        safeExecute(connection, """
                UPDATE dm_change
                SET current_step = CASE
                    WHEN current_status = 'FINISH' THEN 'FINISH'
                    ELSE 'APPROVAL'
                END
                WHERE current_step IN ('CHECK', 'EXECUTE')
                """);
    }
}
