package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070043__must_select extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    create table if not exists dm_query_constraints
                    (
                        id           bigint        not null auto_increment,
                        gmt_create   datetime      not null default CURRENT_TIMESTAMP,
                        gmt_modified datetime      not null default CURRENT_TIMESTAMP,
                        primary_uid  varchar(36)   not null,
                        ds_id        bigint        not null,
                        path         varchar(512)  not null,
                        constraints_json  text  not null,
                        primary key (id)
                        ) ENGINE = InnoDB
                        DEFAULT CHARSET = utf8mb4\
                """, """
                    create unique index id_path_uindex
                        on dm_query_constraints (ds_id, path)\
                """);
    }
}
