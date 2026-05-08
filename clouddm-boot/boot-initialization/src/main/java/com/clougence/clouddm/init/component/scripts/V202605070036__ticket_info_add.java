package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070036__ticket_info_add extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table dm_ticket_details_inst add levels varchar(512) null\
                """, """
                    drop table if exists cache_appro_template\
                """);
    }
}
