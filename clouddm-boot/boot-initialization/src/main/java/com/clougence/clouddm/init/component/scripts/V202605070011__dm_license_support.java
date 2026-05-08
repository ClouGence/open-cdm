package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070011__dm_license_support extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `rdp_auth_result_info` MODIFY COLUMN `auth_result_status` VARCHAR(128) NOT  NULL  COMMENT ''\
                """);
    }

}
