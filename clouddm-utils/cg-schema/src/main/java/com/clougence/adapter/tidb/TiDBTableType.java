package com.clougence.adapter.tidb;

import com.clougence.schema.metadata.TableType;

/**
 * TiDB table type
 * @version : 2021-09-27
 * @author mode
 */
public enum TiDBTableType implements TableType {

    Table("BASE TABLE"),
    View("VIEW"),
    SystemView("SYSTEM VIEW");

    private final String typeName;

    TiDBTableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static TiDBTableType valueOfCode(String code) {
        for (TiDBTableType tableType : TiDBTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        throw new UnsupportedOperationException("Unsupported tidb tableType " + code);
    }
}
