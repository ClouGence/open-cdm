package com.clougence.clouddm.init.component.scripts;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import com.clougence.utils.ExceptionUtils;

public class V202605070038__devops extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        Connection c = context.getConnection();
        for (String sql : sqls) {
            safeExecute(c, sql);
        }
    }

    private static final Set<Integer> errorCodes = new HashSet<>();
    static {
        errorCodes.add(1060);
        errorCodes.add(1061);
        errorCodes.add(1062);
        errorCodes.add(1050);
        errorCodes.add(1072);
        errorCodes.add(1091);
    }

    private void safeExecute(Connection conn, String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            // MySQL / OceanBase 错误码：
            // 1060 = Duplicate column name
            // 1050 = Table exists (for CREATE)
            // 1061 = Duplicate key name
            // 1091 = Can't DROP ... ; check that column/key exists
            if (errorCodes.contains(e.getErrorCode())) {
                System.out.println("Flyway java exec error but skip, msg:" + ExceptionUtils.getRootCauseMessage(e) + ", sql: " + sql);
                return;
            }

            throw new RuntimeException("Failed to execute: " + sql, e);
        }
    }

    private static final List<String> sqls = new ArrayList<>();

    static {
        sqls.add("alter table dm_ds_config\n" + "        add column enable_devops tinyint default 0");

        sqls.add("alter table dm_ticket_details_inst\n" + "        drop ddl_sql_exec_type");

        sqls.add("alter table dm_ticket_details_inst\n" + "        drop none_ddl_sql_exec_type");

        sqls.add("alter table dm_ticket_details_inst\n" + "        drop immediately");

        sqls.add(" create table dm_project_scm\n" + "    (\n" + "        id               bigint      not null auto_increment,\n"
                 + "        gmt_create       datetime    not null default CURRENT_TIMESTAMP,\n" + "        gmt_modified     datetime    not null default CURRENT_TIMESTAMP,\n"
                 + "        owner_uid        varchar(36) not null,\n" + "        scm_type         varchar(12) not null,\n" + "        scm_display      varchar(64) not null,\n"
                 + "        scm_service_url  text        not null,\n" + "        scm_access_token text,\n" + "        primary key (id)\n" + "    ) ENGINE = InnoDB\n"
                 + "      DEFAULT CHARSET = utf8mb4");

        sqls.add(" create table dm_messenger\n" + "    (\n" + "        id           bigint      not null auto_increment,\n"
                 + "        gmt_create   datetime    not null default CURRENT_TIMESTAMP,\n" + "        gmt_modified datetime    not null default CURRENT_TIMESTAMP,\n"
                 + "        owner_uid    varchar(36) not null,\n" + "        im_type      varchar(12) not null,\n" + "        im_display   varchar(64) not null,\n"
                 + "        webhook      text        null,\n" + "        secret       text        null,\n" + "        enable       tinyint     not null default 1,\n"
                 + "        primary key (id)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table dm_project\n" + "    (\n" + "        id             bigint       not null auto_increment,\n"
                 + "        gmt_create     datetime     not null default CURRENT_TIMESTAMP,\n" + "        gmt_modified   datetime     not null default CURRENT_TIMESTAMP,\n"
                 + "        owner_uid      varchar(36)  not null,\n" + "        project_uid    varchar(36)  not null,\n" + "        project_code   varchar(64)  not null,\n"
                 + "        project_name   varchar(128) not null,\n" + "        project_desc   text         null,\n"
                 + "        project_status varchar(12)  not null default 'NORMAL',\n" + "        project_mark   varchar(12)  not null default '',\n"
                 + "        flow_check     varchar(12)  not null default 'Failure',\n" + "        flow_approve   varchar(12)  not null default 'Enable',\n"
                 + "        flow_execute   varchar(12)  not null default 'Manual',\n" + "        options        text         not null,\n" + "        primary key (id),\n"
                 + "        unique key (project_code)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table dm_project_devops\n" + "    (\n" + "        id                bigint       not null auto_increment,\n"
                 + "        gmt_create        datetime     not null default CURRENT_TIMESTAMP,\n" + "        gmt_modified      datetime     not null default CURRENT_TIMESTAMP,\n"
                 + "        owner_uid         varchar(36)  not null,\n" + "        ref_project_id    bigint       not null,\n"
                 + "        ref_scm_id        bigint       not null,\n" + "        ref_scm_type      varchar(12)  not null,\n"
                 + "        scm_repo_space    varchar(128) not null,\n" + "        scm_repo_name     varchar(128) not null,\n"
                 + "        scm_repo_url      text         not null,\n" + "        scm_repo_branch   varchar(64)  not null,\n"
                 + "        scm_repo_event    varchar(128) not null,\n" + "        scm_repo_script   varchar(256) not null,\n" + "        scm_repo_hook_pwd varchar(256) null,\n"
                 + "        enable_hook       tinyint      not null default 1,\n" + "        ds_id             bigint       not null,\n"
                 + "        ds_type           varchar(64)  not null,\n" + "        ds_instance       varchar(64)  not null,\n"
                 + "        ds_desc           text         not null,\n" + "        ds_path           varchar(128) not null,\n"
                 + "        devops_options    text         not null,\n" + "        devops_hashcode   varchar(64)  not null,\n"
                 + "        enable            tinyint      not null default 1,\n" + "        deleted           tinyint      not null default 0,\n"
                 + "        callback_url      text         not null,\n" + "        callback_method   varchar(32)  not null,\n"
                 + "        enable_callback   tinyint      not null default 1,\n" + "        enable_trigger    tinyint      not null default 0,\n"
                 + "        trigger_token     varchar(64)  not null,\n" + "        primary key (id),\n" + "        key (devops_hashcode)\n" + "    ) ENGINE = InnoDB\n"
                 + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table dm_project_msg\n" + "    (\n" + "        id                   bigint      not null auto_increment,\n"
                 + "        gmt_create           datetime    not null default CURRENT_TIMESTAMP,\n"
                 + "        gmt_modified         datetime    not null default CURRENT_TIMESTAMP,\n" + "        owner_uid            varchar(36) not null,\n"
                 + "        ref_project_id       bigint      not null,\n" + "        ref_msg_id           bigint      not null,\n"
                 + "        ref_msg_type         varchar(12) not null,\n" + "        enable               tinyint     not null default 1,\n"
                 + "        language             varchar(12) not null default '',\n" + "        event_project_status tinyint     not null default 1,\n"
                 + "        event_project_config tinyint     not null default 1,\n" + "        event_change_life    tinyint     not null default 1,\n"
                 + "        event_change_notice  tinyint     not null default 1,\n" + "        primary key (id),\n" + "        unique key (owner_uid, ref_project_id)\n"
                 + "    ) ENGINE = InnoDB\n" + "          DEFAULT CHARSET = utf8mb4");

        sqls.add("create table dm_project_change\n" + "    (\n" + "        id             bigint       not null auto_increment,\n"
                 + "        gmt_create     datetime     not null default CURRENT_TIMESTAMP,\n" + "        gmt_modified   datetime     not null default CURRENT_TIMESTAMP,\n"
                 + "        owner_uid      varchar(36)  not null,\n" + "        ref_project_id bigint       not null,\n" + "        ref_devops_id  bigint       not null,\n"
                 + "        change_name    varchar(128) not null,\n" + "        change_time    datetime     not null,\n" + "        change_branch  varchar(256) not null,\n"
                 + "        current_step   varchar(36)  not null,\n" + "        current_status varchar(36)  not null,\n" + "        schedule_time  datetime     null,\n"
                 + "        version        int          not null default 0,\n" + "        remark         text         null,\n"
                 + "        try_times      int          not null default 0,\n" + "        last_commit_id varchar(64)  not null,\n"
                 + "        lock_status    tinyint      not null default 0,\n" + "        flow_walked    text         not null,\n" + "        primary key (id),\n"
                 + "        key change_idx(ref_project_id, ref_devops_id, last_commit_id)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table dm_project_change_item\n" + "    (\n" + "        id                   bigint       not null auto_increment,\n"
                 + "        gmt_create           datetime     not null default CURRENT_TIMESTAMP,\n"
                 + "        gmt_modified         datetime     not null default CURRENT_TIMESTAMP,\n" + "        owner_uid            varchar(36)  not null,\n"
                 + "        ref_project_id       bigint       not null,\n" + "        ref_change_id        bigint       not null,\n"
                 + "        ref_change_item_type varchar(36)  not null,\n" + "        content_name         text         not null,\n"
                 + "        content_index        int          not null,\n" + "        content              longtext     not null,\n" + "        primary key (id)\n"
                 + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table dm_project_version\n" + "    (\n" + "        id                   bigint       not null auto_increment,\n"
                 + "        gmt_create           datetime     not null default CURRENT_TIMESTAMP,\n"
                 + "        gmt_modified         datetime     not null default CURRENT_TIMESTAMP,\n" + "        owner_uid            varchar(36)  not null,\n"
                 + "        ref_project_id       bigint       not null,\n" + "        ref_devops_id        bigint       not null,\n"
                 + "        ref_change_id        bigint       not null,\n" + "        version              datetime     not null,\n"
                 + "        commit_id            varchar(128) not null,\n" + "        content              longtext     not null,\n"
                 + "        type                 varchar(24) not null,\n" + "        primary key (id)\n" + "    ) ENGINE = InnoDB\n" + "      DEFAULT CHARSET = utf8mb4");

        sqls.add("create table dm_project_devops_item\n" + "    (\n" + "        id                   bigint       not null auto_increment,\n"
                 + "        gmt_create           datetime     not null default CURRENT_TIMESTAMP,\n"
                 + "        gmt_modified         datetime     not null default CURRENT_TIMESTAMP,\n" + "        owner_uid            varchar(36)  not null,\n"
                 + "        ref_project_id       bigint       not null,\n" + "        ref_devops_id        bigint       not null,\n"
                 + "        content_name         text         not null,\n" + "        content_index        int          not null,\n"
                 + "        content              longtext     not null,\n" + "        primary key (id)\n" + "    ) ENGINE = InnoDB\n" + "          DEFAULT CHARSET = utf8mb4");
    }
}
