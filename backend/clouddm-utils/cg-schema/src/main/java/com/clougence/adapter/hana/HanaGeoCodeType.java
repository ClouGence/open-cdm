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

public enum HanaGeoCodeType {

    COUNTRY("COUNTRY"),
    STATE("STATE"),
    COUNTY("COUNTY"),
    CITY("CITY"),
    POSTAL_CODE("POSTAL_CODE"),
    DISTRICT("DISTRICT"),
    STREET("STREET"),
    HOUSE_NUMBER("HOUSE_NUMBER"),
    ADDRESS_LINE("ADDRESS_LINE"),
    GEOCODE("GEOCODE"),;

    private final String typeName;

    HanaGeoCodeType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName() { return this.typeName; }

    public static HanaGeoCodeType valueOfCode(String code) {
        if (code == null) {
            return null;
        }
        for (HanaGeoCodeType constraintType : HanaGeoCodeType.values()) {
            if (code.contains(constraintType.typeName) || constraintType.typeName.equalsIgnoreCase(code)) {
                return constraintType;
            }
        }
        return null;
    }

}
