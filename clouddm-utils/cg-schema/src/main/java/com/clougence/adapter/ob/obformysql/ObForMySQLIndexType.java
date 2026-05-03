package com.clougence.adapter.ob.obformysql;

/**
 * @author wanshao
 */
public enum ObForMySQLIndexType {

    Normal,
    Unique,
    Primary,
    Foreign,
    FullText;

    public static ObForMySQLIndexType valueOfCode(String code) {
        for (ObForMySQLIndexType indexType : ObForMySQLIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        throw new UnsupportedOperationException("Unsupported tidb indexType " + code);
    }
}
