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

import com.clougence.schema.metadata.TableType;

/**
 * @author mode 2021/11/16 11:15:51
 */
public enum Db2TableType implements TableType {

    Table("BASE TABLE"),
    View("VIEW");

    private final String typeName;

    Db2TableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static Db2TableType valueOfCode(String code) {
        for (Db2TableType tableType : Db2TableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code.trim())) {
                return tableType;
            }
        }
        return null;
    }
}
