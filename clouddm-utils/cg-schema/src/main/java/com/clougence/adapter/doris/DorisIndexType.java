package com.clougence.adapter.doris;

/**
 * @author wanshao
 */
public enum DorisIndexType {

    Normal,
    Unique,
    Primary,
    Foreign,;

    public static DorisIndexType valueOfCode(String code) {
        for (DorisIndexType indexType : DorisIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        throw new UnsupportedOperationException("Unsupported doris indexType " + code);
    }
}
