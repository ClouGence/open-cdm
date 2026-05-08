package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070040__information_cache extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    create table if not exists dm_meta_information_cache
                    (
                        id           bigint        not null auto_increment,
                        gmt_create   datetime      not null default CURRENT_TIMESTAMP,
                        gmt_modified datetime      not null default CURRENT_TIMESTAMP,
                        primary_uid  varchar(36)   not null,
                        ds_id        bigint        not null,
                        path         varchar(512)  not null,
                        type         varchar(32)   not null,
                        context      longtext      not null,
                        primary key (id)
                        ) ENGINE = InnoDB
                        DEFAULT CHARSET = utf8mb4\
                """, """
                    create unique index id_path_type_uindex
                            on dm_meta_information_cache (ds_id, path, type)\
                """);
    }
}
