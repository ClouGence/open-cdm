package com.clougence.adapter.starrocks;

/**
 * @author wanshao
 */
public enum StarRocksIndexType {

    Normal,
    Unique,
    Primary,
    Foreign,;

    public static StarRocksIndexType valueOfCode(String code) {
        for (StarRocksIndexType indexType : StarRocksIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        throw new UnsupportedOperationException("Unsupported tidb indexType " + code);
    }
}
