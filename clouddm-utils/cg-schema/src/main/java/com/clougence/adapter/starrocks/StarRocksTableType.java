package com.clougence.adapter.starrocks;

import com.clougence.schema.metadata.TableType;

/**
 * @author wanshao
 */
public enum StarRocksTableType implements TableType {

    Table("BASE TABLE"),
    ExtendTable("TABLE"),
    View("VIEW"),
    SystemView("SYSTEM VIEW");

    private final String typeName;

    StarRocksTableType(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() { return this.typeName; }

    public static StarRocksTableType valueOfCode(String code) {
        for (StarRocksTableType tableType : StarRocksTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
