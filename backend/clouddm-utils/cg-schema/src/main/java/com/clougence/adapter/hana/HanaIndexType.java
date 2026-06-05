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

/**
 * @author wanshao
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/6898554c56e249f2aa8747c3dcef3b5d.html
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20d014b575191014b66d8ef88f6a55f6.html
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/4fe29514fd584807ac9f2a04f6754767/20d4117e75191014ba5aaab91b3f087d.html
 * https://help.sap.com/docs/SAP_HANA_PLATFORM/cbbbfc20871e4559abfd45a78ad58c02/61eef2834c274690b84cf9847ea30356.html
 * */
public enum HanaIndexType {

    CPBTREE("CPBTREE"),
    BTREE("BTREE"),
    INVERTED_VALUE("INVERTED VALUE"),
    INVERTED_HASH("INVERTED HASH"),
    INVERTED_INDIVIDUAL("INVERTED INDIVIDUAL"),
    FULLTEXT("FULLTEXT"),
    GEOCODE("GEOCODE"),;

    private final String code;

    HanaIndexType(String code){
        this.code = code;
    }

    public static HanaIndexType valueOfCode(String code) {
        if (code == null) {
            return null;
        }
        for (HanaIndexType indexType : HanaIndexType.values()) {
            if (code.contains(indexType.getCode()) || indexType.getCode().equalsIgnoreCase(code)) {
                return indexType;
            }
        }
        return null;
    }

    public String getCode() { return code; }
}
