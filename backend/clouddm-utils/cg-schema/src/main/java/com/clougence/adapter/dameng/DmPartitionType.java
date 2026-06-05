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

public enum DmPartitionType {

    HASH("HASH"),
    RANGE("RANGE"),
    LIST("LIST");

    private final String typeName;

    DmPartitionType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static DmPartitionType valueOfCode(String code) {
        for (DmPartitionType type : DmPartitionType.values()) {
            if (type.typeName.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
