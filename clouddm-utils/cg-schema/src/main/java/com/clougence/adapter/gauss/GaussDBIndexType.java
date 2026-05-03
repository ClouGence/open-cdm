package com.clougence.adapter.gauss;


public enum GaussDBIndexType {

    /** 普通索引 */
    Normal,
    /** 唯一索引 */
    Unique,;

    public static GaussDBIndexType valueOfCode(String code) {
        for (GaussDBIndexType indexType : GaussDBIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        return null;
    }
}
