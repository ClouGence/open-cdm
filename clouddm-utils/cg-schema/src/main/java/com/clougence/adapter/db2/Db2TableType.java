package com.clougence.adapter.db2;

import com.clougence.schema.metadata.TableType;

/**
 * @author mode 2021/11/16 11:15:51
 */
public enum Db2TableType implements TableType {

    Table("BASE TABLE"),
    View("VIEW");

    private final String typeName;

    Db2TableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static Db2TableType valueOfCode(String code) {
        for (Db2TableType tableType : Db2TableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code.trim())) {
                return tableType;
            }
        }
        return null;
    }
}
