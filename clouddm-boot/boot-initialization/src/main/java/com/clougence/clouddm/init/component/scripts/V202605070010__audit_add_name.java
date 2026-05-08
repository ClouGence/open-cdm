package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070010__audit_add_name extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `rdp_op_audit` add COLUMN `owner_uid` varchar(127)\
                """, """
                    ALTER TABLE `rdp_op_audit` add COLUMN `user_name` varchar(255)\
                """, """
                    ALTER TABLE `rdp_op_audit` add COLUMN `resource_name` varchar(512)\
                """);
    }
}
