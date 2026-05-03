package com.clougence.adapter.postgre;

import com.clougence.schema.metadata.CgTableType;
import com.clougence.schema.metadata.TableType;
import com.clougence.utils.StringUtils;

/**
 * Postgres 的表类型
 * @version : 2021-05-10
 * @author 赵永春 (zyc@hasor.net)
 */
public enum PostgresTableType implements TableType {

    Table("BASE TABLE", CgTableType.BASE_TABLE),
    View("VIEW", CgTableType.VIEW),
    ForeignTable("FOREIGN", null),
    LocalTemporary("LOCAL TEMPORARY", null),
    Materialized("MATERIALIZED VIEW", null),;

    private final String typeName;

    private final CgTableType cgTableType;

    PostgresTableType(String typeName, CgTableType cgTableType){
        this.typeName = typeName;
        this.cgTableType = cgTableType;
    }

    public String getTypeName() { return this.typeName; }

    public CgTableType getCgTableType() { return cgTableType; }

    public static PostgresTableType valueOfCgTableType(CgTableType cgTableType) {
        if (cgTableType == null) {
            return Table;
        }
        for (PostgresTableType tableType : PostgresTableType.values()) {
            if (tableType.cgTableType == cgTableType) {
                return tableType;
            }
        }
        return Table;
    }

    public static PostgresTableType valueOfCode(String code) {
        if (StringUtils.equalsIgnoreCase("r", code) || StringUtils.equalsIgnoreCase("p", code)) {
            return Table;
        } else if (StringUtils.equalsIgnoreCase("v", code)) {
            return View;
        } else if (StringUtils.equalsIgnoreCase("f", code)) {
            return ForeignTable;
        }

        for (PostgresTableType tableType : PostgresTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
