package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070019__file_src extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table rdp_ds_kv_base_config add column `conf_val_type` varchar(64) NOT NULL DEFAULT "TEXT"\
                """, """
                    alter table rdp_data_source add column `info_fetch_type` varchar(64) DEFAULT NULL\
                """, """
                    alter table rdp_data_source_history add column `info_fetch_type` varchar(64) DEFAULT NULL\
                """);
    }
}
