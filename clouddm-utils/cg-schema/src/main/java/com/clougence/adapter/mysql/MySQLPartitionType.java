package com.clougence.adapter.mysql;

public enum MySQLPartitionType {

    HASH("HASH"),
    LINEAR_HASH("LINEAR HASH"),
    KEY("KEY"),
    LINEAR_KEY("LINEAR KEY"),
    RANGE("RANGE"),
    LIST("LIST");

    private final String typeName;

    MySQLPartitionType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static MySQLPartitionType valueOfCode(String code) {
        for (MySQLPartitionType type : MySQLPartitionType.values()) {
            if (type.typeName.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
