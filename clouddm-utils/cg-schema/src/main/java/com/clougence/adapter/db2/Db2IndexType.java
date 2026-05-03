package com.clougence.adapter.db2;

/**
 * Db2 index type
 */
public enum Db2IndexType {

    // Block index
    BLOK("BLOK"),
    // Clustering index (controls the physical placement of newly inserted rows)
    CLUS("CLUS"),
    // Page map index for a column-organized table
    CPMA("CPMA"),
    // Dimension block index
    DIM("DIM"),
    // Modification state index
    MDST("MDST"),
    // Key sequence index for a range-clustered table
    RCT("RCT"),
    // REG = Regular index
    REG("REG"),
    // Text index
    TEXT("TEXT"),
    // XPTH = XML path index
    XPTH("XPTH"),
    // XRGN = XML region index
    XRGN("XRGN"),
    // Index over XML column (logical)
    XVIL("XVIL"),
    // Index over XML column (physical)
    XVIP("XVIP");

    private final String typeName;

    Db2IndexType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static Db2IndexType valueOfCode(String code) {
        for (Db2IndexType tableType : Db2IndexType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
