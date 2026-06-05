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
package com.clougence.clouddm.dsfamily.definition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.schema.metadata.FieldType;
import com.clougence.utils.StringUtils;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public final class TypeMapUtils {

    private static final Map<DataSourceType, FieldType[]> dsColumnTypes;
    static {
        dsColumnTypes = new ConcurrentHashMap<>();
    }

    public static FieldType findColumnTypes(DataSourceType dsProduct, String columnType) {
        FieldType[] types = dsColumnTypes.get(dsProduct);
        if (types != null) {
            for (FieldType type : types) {
                if (StringUtils.equals(type.getCodeKey(), columnType)) {
                    return type;
                }
            }
        }
        return null;
    }

    public static FieldType[] findColumnTypes(DataSourceType dsProduct) {
        return dsColumnTypes.get(dsProduct);
    }

    public static void addColumnTypes(DataSourceType dsProduct, FieldType[] columnTypes) {
        dsColumnTypes.put(dsProduct, columnTypes);
    }
}
