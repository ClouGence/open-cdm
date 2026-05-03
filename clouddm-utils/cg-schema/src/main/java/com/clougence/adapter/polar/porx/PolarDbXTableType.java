package com.clougence.adapter.polar.porx;

import com.clougence.schema.metadata.TableType;

public enum PolarDbXTableType implements TableType {

    Table("BASE TABLE");

    private final String typeName;

    PolarDbXTableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static PolarDbXTableType valueOfCode(String code) {
        for (PolarDbXTableType tableType : PolarDbXTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
