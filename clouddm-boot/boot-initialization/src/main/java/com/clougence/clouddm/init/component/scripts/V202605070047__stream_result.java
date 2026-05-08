package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070047__stream_result extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    alter table dm_files
                            modify unique_id varchar(80) not null\
                """, """
                    alter table dm_files
                            add query_id varchar(40) not null\
                """, """
                    create index idx_files_query_id
                            on dm_files (query_id)\
                """, """
                    alter table dm_files
                            add try_count int not null default 0\
                """);
    }
}
