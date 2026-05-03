package com.clougence.clouddm.ds.maxcompute.analysis.secrules;

public enum McFlushType {

    PRIVILEGES,
    LOGS,
    HOSTS,
    STATUS,
    TABLES,
    USER_RESOURCE;

    public static McFlushType valueOfString(String type) {
        if (type.equalsIgnoreCase("PRIVILEGES")) {
            return PRIVILEGES;
        } else if (type.equalsIgnoreCase("LOGS")) {
            return LOGS;
        } else if (type.equalsIgnoreCase("HOSTS")) {
            return HOSTS;
        } else if (type.equalsIgnoreCase("STATUS")) {
            return STATUS;
        } else if (type.equalsIgnoreCase("TABLES")) {
            return TABLES;
        } else if (type.equalsIgnoreCase("USER_RESOURCES")) {
            return USER_RESOURCE;
        } else {
            throw new IllegalArgumentException("Not support: " + type);
        }
    }

}
