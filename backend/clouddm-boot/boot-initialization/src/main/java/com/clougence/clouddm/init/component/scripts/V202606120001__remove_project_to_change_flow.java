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
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;
import com.clougence.clouddm.init.component.scripts.migration.RemoveProjectToChangeFlowMigrator;

public class V202606120001__remove_project_to_change_flow extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        List<String> scripts = new ArrayList<>();
        scripts.add("""
                    create table if not exists dm_git_ops_scm
                    (
                        id               bigint      not null auto_increment,
                        gmt_create       datetime    not null default CURRENT_TIMESTAMP,
                        gmt_modified     datetime    not null default CURRENT_TIMESTAMP,
                        owner_uid        varchar(36) not null,
                        scm_type         varchar(12) not null,
                        scm_display      varchar(64) not null,
                        scm_service_url  text        null,
                        scm_access_token text        null,
                        primary key (id),
                        key idx_scm_owner_type(owner_uid, scm_type)
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
        scripts.add("""
                    create table if not exists dm_change_flow
                    (
                        id                  bigint       not null auto_increment,
                        gmt_create          datetime     not null default CURRENT_TIMESTAMP,
                        gmt_modified        datetime     not null default CURRENT_TIMESTAMP,
                        owner_uid           varchar(36)  not null,
                        flow_uid            varchar(64)  not null,
                        flow_name           varchar(512) not null,
                        flow_desc           text         null,
                        flow_manager_uid    varchar(36)  not null,
                        flow_status         varchar(12)  not null default 'NORMAL',
                        flow_check          varchar(12)  not null default 'Failure',
                        flow_approve        varchar(12)  not null default 'Enable',
                        flow_execute        varchar(12)  not null default 'Manual',
                        flow_options        text         not null,
                        flow_scm_options    text         not null,
                        ref_scm_id          bigint       not null,
                        ref_scm_type        varchar(12)  not null,
                        scm_repo_space      varchar(128) not null,
                        scm_repo_name       varchar(128) not null,
                        scm_repo_url        text         not null,
                        scm_repo_branch     varchar(64)  not null,
                        scm_repo_event      varchar(128) not null,
                        scm_repo_script     varchar(256) not null,
                        scm_repo_hook_pwd   varchar(256) null,
                        enable_hook         tinyint      not null default 1,
                        enable_trigger      tinyint      not null default 0,
                        trigger_token       varchar(64)  not null,
                        ds_id               bigint       not null,
                        ds_type             varchar(64)  not null,
                        ds_instance         varchar(64)  not null,
                        ds_desc             text         not null,
                        ds_path             varchar(128) not null,
                        ref_msg_id          bigint       null,
                        ref_msg_type        varchar(12)  null,
                        msg_language        varchar(12)  null,
                        enable_msg          tinyint      not null default 0,
                        event_flow_status   tinyint      not null default 0,
                        event_flow_config   tinyint      not null default 0,
                        event_change_life   tinyint      not null default 0,
                        event_change_notice tinyint      not null default 0,
                        callback_url        text         not null,
                        callback_method     varchar(32)  not null,
                        enable_callback     tinyint      not null default 0,
                        flow_hashcode       bigint       not null,
                        enable              tinyint      not null default 1,
                        deleted             tinyint      not null default 0,
                        primary key (id),
                        unique key uk_flow_uid(owner_uid, flow_uid),
                        key idx_flow_name(owner_uid, flow_name, deleted),
                        key idx_flow_hash(owner_uid, flow_hashcode, deleted, enable),
                        key idx_flow_list(owner_uid, flow_status, deleted, gmt_create),
                        key idx_flow_status(owner_uid, flow_status, deleted),
                        key idx_flow_manager(owner_uid, flow_manager_uid),
                        key idx_flow_scm(owner_uid, ref_scm_id, enable, deleted),
                        key idx_flow_ds(owner_uid, ds_id, enable, deleted),
                        key idx_flow_im(owner_uid, ref_msg_id, enable_msg, deleted)
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
        scripts.add("""
                    create table if not exists dm_change_flow_item
                    (
                        id            bigint      not null auto_increment,
                        gmt_create    datetime    not null default CURRENT_TIMESTAMP,
                        gmt_modified  datetime    not null default CURRENT_TIMESTAMP,
                        owner_uid     varchar(36) not null,
                        ref_flow_id   bigint      not null,
                        content_name  text        not null,
                        content_index int         not null,
                        content       longtext    not null,
                        primary key (id),
                        key idx_flow_item(owner_uid, ref_flow_id, content_index)
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
        scripts.add("""
                    create table if not exists dm_change
                    (
                        id             bigint       not null auto_increment,
                        gmt_create     datetime     not null default CURRENT_TIMESTAMP,
                        gmt_modified   datetime     not null default CURRENT_TIMESTAMP,
                        owner_uid      varchar(36)  not null,
                        ref_flow_id    bigint       not null,
                        change_name    varchar(128) not null,
                        change_time    datetime     not null,
                        change_branch  varchar(256) not null,
                        current_step   varchar(36)  not null,
                        current_status varchar(36)  not null,
                        schedule_time  datetime     null,
                        version        int          not null default 0,
                        remark         text         null,
                        try_times      int          not null default 0,
                        last_commit_id varchar(64)  not null,
                        lock_status    tinyint      not null default 0,
                        flow_walked    text         not null,
                        primary key (id),
                        key idx_change_flow_commit(owner_uid, ref_flow_id, last_commit_id),
                        key idx_change_flow_time(owner_uid, ref_flow_id, change_time),
                        key idx_change_flow_lock(owner_uid, ref_flow_id, lock_status),
                        key idx_change_owner_flow_status(owner_uid, ref_flow_id, current_step, current_status),
                        key idx_change_flow_id(owner_uid, ref_flow_id, id),
                        key idx_change_schedule(current_status, schedule_time)
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
        scripts.add("""
                    create table if not exists dm_change_item
                    (
                        id                   bigint      not null auto_increment,
                        gmt_create           datetime    not null default CURRENT_TIMESTAMP,
                        gmt_modified         datetime    not null default CURRENT_TIMESTAMP,
                        owner_uid            varchar(36) not null,
                        ref_flow_id          bigint      not null,
                        ref_change_id        bigint      not null,
                        ref_change_item_type varchar(36) not null,
                        content_name         text        not null,
                        content_index        int         not null,
                        content              longtext    not null,
                        primary key (id),
                        key idx_change_item(owner_uid, ref_change_id, ref_change_item_type, content_index),
                        key idx_change_item_all(owner_uid, ref_change_id),
                        key idx_flow_change_item(owner_uid, ref_flow_id, ref_change_id)
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
        scripts.add("""
                    create table if not exists dm_change_version
                    (
                        id            bigint       not null auto_increment,
                        gmt_create    datetime     not null default CURRENT_TIMESTAMP,
                        gmt_modified  datetime     not null default CURRENT_TIMESTAMP,
                        owner_uid     varchar(36)  not null,
                        ref_flow_id   bigint       not null,
                        ref_change_id bigint       not null,
                        version       datetime     not null,
                        commit_id     varchar(128) not null,
                        content       longtext     not null,
                        type          varchar(24)  not null,
                        primary key (id),
                        key idx_flow_version(owner_uid, ref_flow_id, version),
                        key idx_change_version(owner_uid, ref_change_id)
                    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
        return scripts;
    }

    protected void afterMigrate(Connection connection) throws Exception {
        // 1. DDL first: create target tables with the regular SQL migration path.
        // 2. DML next: move and transform existing rows through Java code.
        // 3. DDL last: drop old tables.

        List<String> scripts = new ArrayList<>();
        scripts.add("drop table if exists dm_project_msg");
        scripts.add("drop table if exists dm_project_change_item");
        scripts.add("drop table if exists dm_project_version");
        scripts.add("drop table if exists dm_project_devops_item");
        scripts.add("drop table if exists dm_project_change");
        scripts.add("drop table if exists dm_project_devops");
        scripts.add("drop table if exists dm_project");
        scripts.add("drop table if exists dm_project_scm");

        new RemoveProjectToChangeFlowMigrator(connection).migrate();
        for (String sql : scripts) {
            safeExecute(connection, sql);
        }
    }
}
