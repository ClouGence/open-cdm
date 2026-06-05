/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
