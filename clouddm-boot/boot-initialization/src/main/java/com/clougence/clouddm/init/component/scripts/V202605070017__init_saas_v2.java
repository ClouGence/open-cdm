package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070017__init_saas_v2 extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                        ALTER TABLE rdp_user
                            ADD COLUMN `saas_user_status` varchar(128) DEFAULT 'SAAS_UN_BIND_BANK',
                            ADD COLUMN `customer_id` varchar(128) DEFAULT NULL\
                """);
    }
}
