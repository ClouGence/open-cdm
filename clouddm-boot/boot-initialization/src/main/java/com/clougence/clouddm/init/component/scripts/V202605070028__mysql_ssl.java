package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070028__mysql_ssl extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `rdp_data_source` ADD COLUMN `security_file_password` varchar(512) DEFAULT NULL\
                """, """
                    ALTER TABLE `rdp_data_source` ADD COLUMN `client_security_file_password` varchar(512) DEFAULT NULL\
                """);
    }
}
