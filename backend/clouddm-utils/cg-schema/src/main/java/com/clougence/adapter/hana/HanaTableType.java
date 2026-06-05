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
package com.clougence.adapter.hana;

import com.clougence.schema.metadata.TableType;

/**
 * @author hanlizhi
 */
public enum HanaTableType implements TableType {

    ROW("ROW"),
    COLUMN("COLUMN")
    ;

    private final String typeName;

    HanaTableType(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() { return this.typeName; }

    public static HanaTableType valueOfCode(String code) {
        for (HanaTableType tableType : HanaTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
