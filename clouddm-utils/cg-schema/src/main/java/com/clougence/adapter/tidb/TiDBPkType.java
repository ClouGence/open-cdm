package com.clougence.adapter.tidb;

public enum TiDBPkType {

    CLUSTERED("CLUSTERED"),
    NON_CLUSTERED("NONCLUSTERED");

    private final String typeName;

    TiDBPkType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static TiDBPkType valueOfCode(String code) {
        for (TiDBPkType constraintType : TiDBPkType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
