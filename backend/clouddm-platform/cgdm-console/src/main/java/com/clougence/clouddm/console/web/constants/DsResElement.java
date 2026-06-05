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
package com.clougence.clouddm.console.web.constants;

/**
 * @author bucketli 2021/1/6 19:00
 */
public enum DsResElement {

    CATALOG("DS_RES_ELEMENT_DB", "ruleCatalog"),

    SCHEMA("DS_RES_ELEMENT_SCHEMA", "ruleSchema"),

    TABLE("DS_RES_ELEMENT_TABLE", "ruleTable"),

    COLUMN("DS_RES_ELEMENT_COL", "ruleColumn");

    private final String nameI18nKey;

    private final String paramName;

    DsResElement(String i18Key, String paramName){
        this.nameI18nKey = i18Key;
        this.paramName = paramName;
    }

    public String getNameI18nKey() { return nameI18nKey; }

    public String getParamName() { return paramName; }
}
