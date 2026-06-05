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
package com.clougence.adapter.starrocks;

/**
 * @author wanshao
 */
public enum StarRocksAggType {

    SUM("SUM"),
    MAX("MAX"),
    MIN("MIN"),
    REPLACE("REPLACE"),
    HLL_UNION("HLL_UNION"), //（仅用于HLL列，为HLL独有的聚合方式)。
    BITMAP_UNION("BITMAP_UNION"), //（仅用于 BITMAP 列，为 BITMAP 独有的聚合方式)。
    REPLACE_IF_NOT_NULL("REPLACE_IF_NOT_NULL"),;

    private final String typeName;

    StarRocksAggType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static StarRocksAggType valueOfCode(String code) {
        for (StarRocksAggType constraintType : StarRocksAggType.values()) {
            if (constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }
}
