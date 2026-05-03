package com.clougence.adapter.gauss;

import com.clougence.schema.metadata.CgTableType;
import com.clougence.schema.metadata.TableType;
import com.clougence.utils.StringUtils;


public enum GaussDBTableType implements TableType {

    Table("BASE TABLE", CgTableType.BASE_TABLE),
    View("VIEW", CgTableType.VIEW),
    ForeignTable("FOREIGN", null),
    LocalTemporary("LOCAL TEMPORARY", null),
    Materialized("MATERIALIZED VIEW", null),;

    private final String typeName;

    private final CgTableType cgTableType;

    GaussDBTableType(String typeName, CgTableType cgTableType){
        this.typeName = typeName;
        this.cgTableType = cgTableType;
    }

    public String getTypeName() { return this.typeName; }

    public CgTableType getCgTableType() { return cgTableType; }

    public static GaussDBTableType valueOfCgTableType(CgTableType cgTableType) {
        if (cgTableType == null) {
            return Table;
        }
        for (GaussDBTableType tableType : GaussDBTableType.values()) {
            if (tableType.cgTableType == cgTableType) {
                return tableType;
            }
        }
        return Table;
    }

    public static GaussDBTableType valueOfCode(String code) {
        if (StringUtils.equalsIgnoreCase("r", code) || StringUtils.equalsIgnoreCase("p", code)) {
            return Table;
        } else if (StringUtils.equalsIgnoreCase("v", code)) {
            return View;
        } else if (StringUtils.equalsIgnoreCase("f", code)) {
            return ForeignTable;
        }

        for (GaussDBTableType tableType : GaussDBTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
