package com.clougence.adapter.hana;

import com.clougence.schema.metadata.TableType;

/**
 * @author hanlizhi
 */
public enum HanaTableType implements TableType {

    ROW("ROW"),
    COLUMN("COLUMN")
    ;

    private final String typeName;

    HanaTableType(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() { return this.typeName; }

    public static HanaTableType valueOfCode(String code) {
        for (HanaTableType tableType : HanaTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
