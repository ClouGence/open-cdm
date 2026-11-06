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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

final class IotdbObjectScripts {

    private IotdbObjectScripts(){
    }

    static List<String> tree(Statement statement, String database, UmiTypes type, String name) throws SQLException {
        RdbValue selected = IotdbTreeMetadata.detail(statement, database, type, name);
        if (selected == null) {
            throw unsupported(name);
        }

        String device = selected.asValue();
        List<Map<String, String>> measurements = new ArrayList<>();
        if (type == UmiTypes.Timeseries) {
            device = selected.getAttributes().get("device");
            measurements.add(selected.getAttributes());
        } else {
            for (Map<String, String> row : IotdbMetadataRows.read(statement, "SHOW TIMESERIES " + device + ".*")) {
                IotdbTreeMetadata.addPathAttributes(row, row.get("timeseries"), UmiTypes.Timeseries);
                if (database.equals(row.get("database")) && device.equals(row.get("device"))) {
                    measurements.add(row);
                }
            }
        }
        RdbValue deviceMetadata = IotdbTreeMetadata.detail(statement, database, UmiTypes.Device, device);
        if (measurements.isEmpty() || deviceMetadata == null || StringUtils.isNotBlank(deviceMetadata.getAttributes().get("template"))) {
            throw unsupported(name);
        }
        String aligned = deviceMetadata.getAttributes().get("isaligned");
        if (!"true".equals(aligned) && !"false".equals(aligned)) {
            throw unsupported(name);
        }

        List<String> columns = new ArrayList<>();
        List<String> scripts = new ArrayList<>();
        for (Map<String, String> row : measurements) {
            if (!"BASE".equals(row.get("viewtype")) || StringUtils.isNotBlank(row.get("deadband")) || StringUtils.isNotBlank(row.get("deadbandparameters"))
                || StringUtils.isNotBlank(row.get("deadband parameters"))) {
                throw unsupported(row.get("timeseries"));
            }
            String alias = "";
            if (StringUtils.isNotBlank(row.get("alias"))) {
                alias = "(" + quoteTreeNode(row.get("alias")) + ")";
            }
            String datatype = token(row.get("datatype"));
            String encoding = token(row.get("encoding"));
            String compression = token(row.get("compression"));
            String attributes = properties("TAGS", row.get("tags")) + properties("ATTRIBUTES", row.get("attributes"));

            if ("true".equals(aligned)) {
                columns.add(row.get("name") + alias + " " + datatype + " ENCODING=" + encoding + " COMPRESSOR=" + compression + attributes);
            } else {
                scripts.add("CREATE TIMESERIES " + row.get("timeseries") + alias + " WITH DATATYPE=" + datatype + ", ENCODING=" + encoding + ", COMPRESSOR=" + compression
                            + attributes + ";");
            }
        }
        if ("true".equals(aligned)) {
            scripts.add("CREATE ALIGNED TIMESERIES " + device + " (" + String.join(", ", columns) + ");");
        }
        return scripts;
    }

    static List<String> table(Statement statement, String database, String name) throws SQLException {
        RdbTable table = IotdbTableMetadata.detail(statement, database, UmiTypes.Table, name);
        if (table == null || !"BASE TABLE".equals(table.getTableType())) {
            throw unsupported(name);
        }
        List<Map<String, String>> rows = IotdbMetadataRows.read(statement, "SHOW CREATE TABLE " + quoteTableName(database) + "." + quoteTableName(name));
        if (rows.size() != 1) {
            throw unsupported(name);
        }
        String script = rows.get(0).get("create table");
        String prefix = "CREATE TABLE " + quoteTableName(name) + " ";
        if (script == null || !script.startsWith(prefix)) {
            throw unsupported(name);
        }
        // Native SHOW CREATE TABLE omits the database. Bind the script to the inspected object.
        script = "CREATE TABLE " + quoteTableName(database) + "." + quoteTableName(name) + " " + script.substring(prefix.length());
        if (!script.endsWith(";")) {
            script += ";";
        }
        return List.of(script);
    }

    private static String properties(String clause, String json) {
        if (StringUtils.isBlank(json)) {
            return "";
        }
        Map<String, String> values = JsonUtils.toMap(json);
        List<String> assignments = new ArrayList<>();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            assignments.add(quoteString(entry.getKey()) + "=" + quoteString(entry.getValue()));
        }
        if (assignments.isEmpty()) {
            return "";
        }
        return " " + clause + "(" + String.join(", ", assignments) + ")";
    }

    private static String token(String value) {
        if (value == null || !value.matches("[A-Z][A-Z0-9_]*")) {
            throw unsupported(value);
        }
        return value;
    }

    private static String quoteTreeNode(String value) {
        // SHOW TIMESERIES preserves native quoting for aliases containing special characters.
        if (value.startsWith("`")) {
            return value;
        }
        return "`" + value.replace("`", "``") + "`";
    }

    private static String quoteTableName(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static String quoteString(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    private static ThirdPartyApiException unsupported(String name) {
        return ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_OBJECT_SCRIPT_UNSUPPORTED, name);
    }
}
