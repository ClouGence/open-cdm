package com.clougence.adapter.dameng;

public enum DmPartitionType {

    HASH("HASH"),
    RANGE("RANGE"),
    LIST("LIST");

    private final String typeName;

    DmPartitionType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DmPartitionType valueOfCode(String code) {
        for (DmPartitionType type : DmPartitionType.values()) {
            if (type.typeName.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
