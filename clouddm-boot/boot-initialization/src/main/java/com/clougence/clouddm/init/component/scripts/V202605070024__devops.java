package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070024__devops extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table rdp_ticket_process
                                modify next_id bigint null\
                """, """
                    alter table rdp_ticket_process
                                modify appro_biz varchar(64) null\
                """);
    }
}
