package com.clougence.adapter.tidb;

/**
 * TiDB index type
 * @version : 2021-09-27
 * @author mode
 */
public enum TiDBIndexType {

    Normal,
    Unique,
    Primary,
    Foreign,;

    public static TiDBIndexType valueOfCode(String code) {
        for (TiDBIndexType indexType : TiDBIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        throw new UnsupportedOperationException("Unsupported tidb indexType " + code);
    }
}
