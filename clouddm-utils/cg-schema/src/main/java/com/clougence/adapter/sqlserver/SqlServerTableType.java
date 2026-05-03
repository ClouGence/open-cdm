package com.clougence.adapter.sqlserver;

import com.clougence.schema.metadata.CgTableType;
import com.clougence.schema.metadata.TableType;

/**
 * @author chunlin create time is 2025/4/15
 * for metadata query
 */
public enum SqlServerTableType implements TableType {

    Table("BASE TABLE", CgTableType.BASE_TABLE),
    View("VIEW", CgTableType.VIEW);

    private final String      typeName;

    private final CgTableType cgTableType;

    SqlServerTableType(String typeName, CgTableType cgTableType){
        this.typeName = typeName;
        this.cgTableType = cgTableType;
    }

    @Override
    public String getTypeName() { return this.typeName; }

    public CgTableType getCgTableType() { return cgTableType; }

    public static SqlServerTableType valueOfCgTableType(CgTableType cgTableType) {
        if (cgTableType == null) {
            return Table;
        }
        for (SqlServerTableType tableType : SqlServerTableType.values()) {
            if (tableType.cgTableType == cgTableType) {
                return tableType;
            }
        }
        return Table;
    }

    public static SqlServerTableType valueOfCode(String code) {
        for (SqlServerTableType tableType : SqlServerTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
