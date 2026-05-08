package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070020__sub_account_sso extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE rdp_user
                            add column access_token text DEFAULT NULL\
                """, """
                    CREATE TABLE IF NOT EXISTS rdp_csrf_token
                        (
                            id           bigint       NOT NULL AUTO_INCREMENT primary key,
                            gmt_create   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            gmt_modified datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            token        varchar(127) NOT NULL,
                            jump_url     text,
                            secret_token text
                        )\
                """);
    }
}
