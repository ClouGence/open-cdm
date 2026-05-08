package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070003__add_sso extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    CREATE TABLE `rdp_sso_register` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `request_id` varchar(32) NOT NULL,
                            `union_id` varchar(32) NOT NULL,
                            `nickname` varchar(255) DEFAULT NULL,
                            `register_status` varchar(32) NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_request_id` (`request_id`) USING BTREE
                        )\
                """, """
                    alter table rdp_user add column `wechat_union_id` varchar(32) DEFAULT NULL\
                """);
    }

}
