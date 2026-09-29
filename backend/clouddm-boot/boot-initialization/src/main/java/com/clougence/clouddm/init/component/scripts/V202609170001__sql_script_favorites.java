/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202609170001__sql_script_favorites extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                CREATE TABLE dm_exec_sql_script
                (
                    id           bigint       NOT NULL AUTO_INCREMENT,
                    gmt_create   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    gmt_modified datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    owner_uid    varchar(64)  NOT NULL,
                    name         varchar(128) DEFAULT NULL,
                    ds_type      varchar(64)  NOT NULL,
                    file_uri     varchar(500) NOT NULL,
                    status       varchar(16)  NOT NULL DEFAULT 'Ready',
                    version      bigint       NOT NULL DEFAULT 0,
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_sql_script_owner_name (owner_uid, name),
                    UNIQUE KEY uk_sql_script_file_uri (file_uri),
                    KEY idx_sql_script_owner_status_modified (owner_uid, status, gmt_modified, id),
                    KEY idx_sql_script_status_modified (status, gmt_modified, id)
                ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
                """);
    }
}
