package com.clougence.adapter.polar.porx;

public enum PolarDbXIndexType {

    Normal,

    Unique,

    Primary;

    public static PolarDbXIndexType valueOfCode(String code) {
        for (PolarDbXIndexType indexType : PolarDbXIndexType.values()) {
            if (indexType.name().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        throw new UnsupportedOperationException("Unsupported PolarDBMy indexType " + code);
    }
}
