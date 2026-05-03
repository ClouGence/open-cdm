# noinspection SqlNoDataSourceInspectionForFile

DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init()
begin
    declare continue handler for 1060 begin
    end;
    declare continue handler for 1061 begin
    end;
    declare continue handler for 1062 begin
    end;
    declare continue handler for 1050 begin
    end;
    declare continue handler for 1072 begin
    end;
    declare continue handler for 1091 begin
    end;
    declare continue handler for 1051 begin
    end;

    -- ----------------------------
    -- DataSource tables --
    -- ----------------------------
    drop table if exists `data_handle_config`;
    drop table if exists `data_handle_package`;
    drop table if exists `data_desensitize_rule`;

    alter table `dm_sec_rules`
        add `rule_md5` varchar(36) null,
        add `rule_id`  varchar(36) null;

    create index `dm_sec_rules_rule_id` on `dm_sec_rules` (`rule_id`);

    CREATE TABLE `dm_sec_sensitive`
    (
        `id`           bigint       NOT NULL AUTO_INCREMENT,
        `gmt_create`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `owner_uid`    varchar(255)          DEFAULT NULL,
        `sen_id`       varchar(36)  NOT NULL,
        `sen_name`     varchar(255) NOT NULL,
        `sen_desc`     text,
        `sen_type`     varchar(64)  not null,
        `sen_def`      text         NOT NULL,
        `sen_content`  text         NOT NULL,
        `sen_md5`      varchar(36)  NOT NULL,
        `inner_share`  tinyint      NOT NULL,
        `sen_mode`     varchar(12)  NOT NULL,
        PRIMARY KEY (`id`),
        index `dm_sec_sensitive_sen_id` (`sen_id`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

    ALTER TABLE `dm_sec_referer`
        ADD `ref_kind` VARCHAR(12) NOT NULL,
        ADD `ref_md5`  varchar(36) NULL,
        ADD `sen_mode` varchar(12) NULL;

    drop index `dm_sec_referer_refs` ON `dm_sec_referer`;
    create index `dm_sec_referer_refs` ON `dm_sec_referer` (`ref_rule`, `ref_spec`, `rule_kind`);

    update `dm_sec_referer` set `ref_kind` = 'QUERY' where `ref_kind` is null;

    CREATE TABLE `dm_sec_range`
    (
        `id`           bigint      NOT NULL AUTO_INCREMENT,
        `gmt_create`   datetime     DEFAULT CURRENT_TIMESTAMP,
        `gmt_modified` datetime     DEFAULT CURRENT_TIMESTAMP,
        `owner_uid`    varchar(255) DEFAULT NULL,
        `ref_spec`     bigint      NOT NULL,
        `ref_id`       bigint      NOT NULL,
        `range_type`   varchar(24) NOT NULL,
        `match_mode`   varchar(12) NOT NULL,
        `level_prefix` text        NOT NULL,
        `level_nodes`  text        NOT NULL,
        `choose_all`   tinyint     NOT NULL,
        `table_level`  varchar(64) NULL,
        `ref_ds_type`  varchar(64) NULL,
        PRIMARY KEY (`id`),
        index `dm_sec_range_by_id` (`owner_uid`, `ref_spec`, `ref_id`),
        index `dm_sec_range_by_spec` (`owner_uid`, `ref_spec`)
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci;

-- sample user and worker (end) --------------------
end
$$

call proc_init();