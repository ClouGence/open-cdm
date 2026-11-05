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
package com.clougence.clouddm.ds.iotdb.execute;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.MatchUtils;
import com.clougence.utils.StringUtils;

final class IotdbMetadataRows {

    private IotdbMetadataRows(){
    }

    static List<Map<String, String>> read(Statement statement, String sql) throws SQLException {
        try (ResultSet result = statement.executeQuery(sql)) {
            ResultSetMetaData metadata = result.getMetaData();
            List<String> labels = new ArrayList<>();
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                labels.add(metadata.getColumnLabel(i).toLowerCase(Locale.ROOT));
            }
            List<Map<String, String>> rows = new ArrayList<>();
            while (result.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < labels.size(); i++) {
                    String value = result.getString(i + 1);
                    if (value != null) {
                        row.put(labels.get(i), value);
                    }
                }
                rows.add(row);
            }
            return rows;
        }
    }

    static DsElement element(UmiTypes type, String name, IotdbSqlDialect model, Map<String, String> row) {
        Map<String, Object> attributes = new LinkedHashMap<>(row);
        attributes.put("model", model.name());
        if (type == UmiTypes.Catalog || type == UmiTypes.Schema) {
            if (model == IotdbSqlDialect.TREE) {
                attributes.put("leafTypes", List.of("DEVICE", "TIMESERIES"));
            } else {
                attributes.put("leafTypes", List.of("TABLE", "VIEW"));
            }
        }
        if (type == UmiTypes.Timeseries) {
            attributes.put("quickQuery", "SELECT " + row.get("name") + " FROM " + row.get("device") + " LIMIT 20;");
        } else if (type == UmiTypes.Device) {
            attributes.put("quickQuery", "SHOW DEVICES " + name + " WITH DATABASE;");
        } else if (type == UmiTypes.Table || type == UmiTypes.View) {
            String database = row.get("database").replace("\"", "\"\"");
            String table = name.replace("\"", "\"\"");
            attributes.put("quickQuery", "SELECT * FROM \"" + database + "\".\"" + table + "\" LIMIT 20;");
        }

        DsElement element = new DsElement();
        element.setObjId(-1);
        element.setObjType(type);
        element.setObjName(name);
        element.setObjAttr(attributes);
        return element;
    }

    static Pattern namePattern(String pattern) {
        if (StringUtils.isBlank(pattern) || "*".equals(pattern)) {
            return Pattern.compile(".*", Pattern.DOTALL);
        }
        return Pattern.compile(MatchUtils.wildToRegex(pattern), Pattern.DOTALL);
    }
}
