package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070004__user_last_reset_pwd extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table rdp_user add column `last_date_update_pwd` datetime DEFAULT NULL\
                """, """
                    alter table rdp_user_kv_base_config add column `conf_val_type` varchar(64) NOT NULL DEFAULT "TEXT"\
                """);
    }

}
