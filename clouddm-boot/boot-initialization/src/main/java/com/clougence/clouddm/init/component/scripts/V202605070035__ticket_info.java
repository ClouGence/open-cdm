package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070035__ticket_info extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table dm_ticket_details_inst add ticket_info text\
                """, """
                    alter table dm_sec_rules add deprecated tinyint NOT NULL default 0\
                """, """
                    alter table dm_sec_sensitive add deprecated tinyint NOT NULL default 0\
                """);
    }
}
