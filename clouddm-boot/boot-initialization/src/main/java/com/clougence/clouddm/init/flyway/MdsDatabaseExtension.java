package com.clougence.clouddm.init.flyway;

import org.flywaydb.core.extensibility.PluginMetadata;

public class MdsDatabaseExtension implements PluginMetadata {

    public String getDescription() { return "Community-contributed OceanBase database support extension " + readVersion() + " by Redgate"; }

    public static String readVersion() {
        return "10.24.0";
    }
}
