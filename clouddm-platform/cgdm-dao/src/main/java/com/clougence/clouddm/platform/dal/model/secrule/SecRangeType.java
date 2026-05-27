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
package com.clougence.clouddm.platform.dal.model.secrule;

import com.clougence.clouddm.platform.dal.handler.EnumOfCode;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
public enum SecRangeType implements EnumOfCode<SecRangeType> {

    Environment("RDB_ENV"),
    Instance("RDB_INSTANCE"),
    Catalog("RDB_CATALOG"),
    Schema("RDB_SCHEMA"),
    TableOrView("RDB_TABLE_OR_VIEW"),
    Column("RDB_COLUMN");

    private final String i18nKey;

    SecRangeType(String i18nKey){
        this.i18nKey = i18nKey;
    }

    public static SecRangeType ofTarget(TargetType type) {
        switch (type) {
            case Environment:
                return Environment;
            case Instance:
                return Instance;
            case Catalog:
                return Catalog;
            case Schema:
                return Schema;
            case Table:
            case View:
            case Materialized:
                return TableOrView;
            case Column:
                return Column;
            default:
                return null;
        }
    }

    @Override
    public SecRangeType valueOfCode(String codeString) {
        try {
            return SecRangeType.valueOf(codeString);
        } catch (Exception e) {
            return ofTarget(TargetType.valueOf(codeString));
        }
    }
}
