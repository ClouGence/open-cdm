package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202605070015__add_resource_manage_enable extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                        ALTER TABLE rdp_user
                            ADD COLUMN resource_manage_enable tinyint(1) DEFAULT 0\
                """);
    }
}
