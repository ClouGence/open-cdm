package com.clougence.clouddm.base.metadata.rdp.enumeration;

import lombok.Getter;

@Getter
public enum CatalogType {

    NESSIE("nessie"),

    GLUE("glue"),

    REST("rest"),

    JDBC("jdbc");

    private final String code;

    CatalogType(String code){
        this.code = code;
    }

    public static CatalogType valueOfCode(String code) {
        for (CatalogType tableType : CatalogType.values()) {
            if (tableType.code.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
