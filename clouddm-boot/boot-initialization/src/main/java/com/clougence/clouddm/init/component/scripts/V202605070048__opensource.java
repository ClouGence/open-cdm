package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070048__opensource extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                    ALTER TABLE `rdp_data_source` DROP COLUMN `region`\
                """, """
                    ALTER TABLE `rdp_data_source_history` DROP COLUMN `region`\
                """);
    }
}
