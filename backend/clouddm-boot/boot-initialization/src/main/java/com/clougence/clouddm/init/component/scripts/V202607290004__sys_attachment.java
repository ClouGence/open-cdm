/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202607290004__sys_attachment extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table dm_approval
                        add column content_type varchar(16) not null default 'INLINE'
                            comment 'SQL content source: INLINE or ATTACHMENT'
                """, """
                    alter table dm_approval
                        add column features longtext null
                            comment 'JSON list of enabled approval features'
                """, """
                    update dm_approval
                    set features = '["PRE_INIT"]'
                    where appro_biz = 'DM_QUERY'
                """, """
                    create table dm_sys_attachment
                    (
                        id                bigint auto_increment primary key,
                        gmt_create        datetime not null default current_timestamp,
                        gmt_modified      datetime not null default current_timestamp,
                        owner_uid         varchar(64) not null default '',
                        approval_id       bigint null,
                        attachment_type   varchar(32) not null,
                        attachment_status varchar(16) not null,
                        file_name         varchar(512) not null,
                        file_size         bigint not null,
                        file_hash         varchar(64) null,
                        blob_content      longblob null,
                        key idx_attachment_approval (approval_id),
                        key idx_attachment_cleanup (attachment_status, gmt_modified),
                        unique key uk_sys_attachment_approval (approval_id, attachment_type)
                    )
                """, """
                    alter table dm_exec_auto_job
                        modify column uid varchar(127) null,
                        drop column primary_uid
                """, """
                    create index idx_auto_exec_task_job_order
                        on dm_exec_auto_task (auto_exec_job_id, exec_order)
                """, """
                    alter table dm_exec_auto_task
                        modify column exec_sql longtext not null
                """, """
                    alter table dm_exec_auto_task
                        add column query_id varchar(128) null
                """, """
                    update dm_exec_auto_task
                    set query_id = uuid()
                    where query_id is null
                """, """
                    alter table dm_exec_auto_task
                        modify column query_id varchar(128) not null
                """, """
                    create unique index uk_exec_auto_task_query_id
                        on dm_exec_auto_task (query_id)
                """, """
                    create unique index uk_exec_auto_job_query_id
                        on dm_exec_auto_job (query_id)
                """, """
                    update dm_change_item
                    set ref_change_item_type = 'CHECKS_DETAIL'
                    where ref_change_item_type = 'CHECKS'
                """);
    }
}
