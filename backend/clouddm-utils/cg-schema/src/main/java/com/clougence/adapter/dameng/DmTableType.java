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
package com.clougence.adapter.dameng;

import com.clougence.schema.metadata.TableType;

/**
 * 达梦 表类型
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
public enum DmTableType implements TableType {

    Table("TABLE"),
    HUGE("HUGE"),
    TEMPORARY("TEMPORARY"),
    View("VIEW");

    private final String typeName;

    DmTableType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DmTableType valueOfCode(String code) {
        for (DmTableType tableType : DmTableType.values()) {
            if (tableType.typeName.equalsIgnoreCase(code)) {
                return tableType;
            }
        }
        return null;
    }
}
