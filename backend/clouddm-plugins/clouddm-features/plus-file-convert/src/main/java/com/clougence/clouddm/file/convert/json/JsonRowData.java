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
package com.clougence.clouddm.file.convert.json;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.file.convert.config.json.JsonOption;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public class JsonRowData {

    private final List<String>  rowData  = new ArrayList<>();
    private final List<String>  columns  = new ArrayList<>();
    private final List<Boolean> asString = new ArrayList<>();

    public void addRow(String key, String value, boolean isString) {
        columns.add(key);
        rowData.add(value);
        asString.add(isString);
    }

    public String toJsonString(JsonOption option) {
        StringBuilder sb = new StringBuilder("{");

        boolean first = true;
        for (int i = 0; i < columns.size(); i++) {
            JsonOption.ColumnOption columnOption = option.getColumns().get(i);
            if (!columnOption.isExport()) {
                continue;
            }

            String key = columnOption.getColumnName();
            String value;
            if (this.rowData.get(i) == null) {
                value = "null";
            } else if (this.asString.get(i)) {
                value = "\"" + escapeSpecialChar(this.rowData.get(i)) + "\"";
            } else {
                value = this.rowData.get(i);
            }

            if (!first) {
                sb.append(",");
            }
            sb.append("\"").append(key).append("\":").append(value);
            first = false;
        }

        sb.append("}");
        return sb.toString();
    }

    private static String escapeSpecialChar(String data) {
        data = StringUtils.replace(data, "\r", "\\r");
        data = StringUtils.replace(data, "\n", "\\n");
        data = StringUtils.replace(data, "\t", "\\t");
        data = StringUtils.replace(data, "\b", "\\b");
        data = StringUtils.replace(data, "\f", "\\f");
        data = StringUtils.replace(data, "\"", "\\\"");
        data = StringUtils.replace(data, "\\", "\\\\");
        return data;
    }
}
