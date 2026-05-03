package com.clougence.adapter.doris;

import com.clougence.schema.metadata.TableType;

/**
 * @author wanshao
 */
public enum DorisTableType implements TableType {

    Table("BASE TABLE"),
    View("VIEW"),
    SystemView("SYSTEM VIEW");

    private final String typeName;

    DorisTableType(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() { return this.typeName; }

    public static DorisTableType valueOfCode(String code) {
        for (DorisTableType tableType : DorisTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
