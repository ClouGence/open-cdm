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
package com.clougence.clouddm.ds.hana.definition.ui.editor.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.adapter.hana.HanaSqlValues;
import com.clougence.adapter.hana.HanaTypes;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbUniqueKey;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public final class HanaDataEditorUtils {
    private HanaDataEditorUtils(){
    }

    public static List<String> keyColumns(RdbTable table) {
        if (table.getPrimaryKey() != null && CollectionUtils.isNotEmpty(table.getPrimaryKey().getColumnList())) {
            return table.getPrimaryKey().getColumnList();
        }
        List<RdbUniqueKey> keys = new ArrayList<>();
        if (table.getUniqueConstraints() != null) {
            keys.addAll(table.getUniqueConstraints());
        }
        if (table.getUniqueKeys() != null) {
            keys.addAll(table.getUniqueKeys());
        }
        // A nullable unique constraint does not identify every row. Never combine independent keys.
        for (RdbUniqueKey key : keys) {
            if (CollectionUtils.isNotEmpty(key.getColumnList()) && key.getColumnList().stream().allMatch(name -> {
                RdbColumn column = table.getColumns().get(name);
                return column != null && column.hasConstraint(NonNull.class);
            })) {
                return key.getColumnList();
            }
        }
        return Collections.emptyList();
    }

    public static boolean generated(RdbColumn column) {
        return StringUtils.isNotBlank(column.getAttribute(HanaAttributeNames.GENERATION_TYPE))
               || StringUtils.isNotBlank(column.getAttribute(HanaAttributeNames.GENERATION_ALWAYS_AS));
    }

    public static boolean editable(RdbColumn column) {
        HanaTypes type = (HanaTypes) column.getSqlType();
        return type != HanaTypes.ARRAY && type != HanaTypes.ST_POINT && type != HanaTypes.ST_GEOMETRY && type != HanaTypes.BINTEXT;
    }

    public static String literal(RdbColumn column, String value) {
        try {
            return HanaSqlValues.literal((HanaTypes) column.getSqlType(), value);
        } catch (IllegalArgumentException e) {
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
