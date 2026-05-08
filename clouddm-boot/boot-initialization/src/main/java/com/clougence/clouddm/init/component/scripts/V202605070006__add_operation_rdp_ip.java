package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070006__add_operation_rdp_ip extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `rdp_op_audit`
                        ADD COLUMN `ip` varchar(255) DEFAULT NULL\
                """, """
                    ALTER TABLE `rdp_op_audit`
                        ADD COLUMN `audit_type` varchar(255) DEFAULT NULL\
                """, """
                    ALTER TABLE `rdp_op_audit`
                        ADD COLUMN `operation_uri` varchar(255) DEFAULT NULL\
                """, """
                    ALTER TABLE `rdp_op_audit`
                        MODIFY COLUMN `resource_value` varchar(512) DEFAULT NULL\
                """);
    }

}
