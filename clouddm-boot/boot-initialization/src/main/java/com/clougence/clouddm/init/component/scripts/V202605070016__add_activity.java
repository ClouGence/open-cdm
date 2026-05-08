package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070016__add_activity extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    create table `rdp_ticket_process_activity`
                    (
                        id              bigint auto_increment primary key,
                        gmt_create      datetime     default CURRENT_TIMESTAMP not null,
                        gmt_modified    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
                        activity_id     varchar(64)                            not null,
                        process_id      bigint                                 not null,
                        ticket_id       bigint                                 not null,
                        activity_title  varchar(128)                           not null,
                        context         text,
                        deleted         tinyint(1)   default 0                 not null,
                        order_number    int                                    not null
                    ) ENGINE = InnoDB
                      DEFAULT CHARSET = utf8mb4\
                """, """
                    create index idx_process_activity
                        on `rdp_ticket_process_activity` (process_id, activity_id)\
                """, """
                    create index idx_ticket_id
                        on `rdp_ticket_process_activity` (ticket_id)\
                """, """
                    ALTER TABLE `rdp_ticket_inst` add COLUMN `approval_url` text\
                """, """
                    delete from rdp_async_task where handler_name = 'rdpTicketAsyncTask'\
                """);
    }
}
