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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.ds.iotdb.i18n.IotdbI18nKeys;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class IotdbTreeMetadata {

    private static final int    PAGE_SIZE  = 256;
    private static final Method SPLIT_PATH = loadPathParser();

    private IotdbTreeMetadata(){
    }

    static List<DsElement> list(Statement statement, String database, UmiTypes type, String pattern) throws SQLException {
        String databasePath = concretePath(database);
        String scope = databasePath + ".**";
        String sql = show(type, scope);
        String nameField = nameField(type);
        Pattern names = IotdbMetadataRows.namePattern(pattern);
        List<DsElement> elements = new ArrayList<>();
        // The recursive wildcard starts below the database, which can itself be a device.
        if (type == UmiTypes.Device) {
            for (Map<String, String> row : IotdbMetadataRows.read(statement, show(type, databasePath))) {
                if (databasePath.equals(row.get(nameField))) {
                    String shortName = addPathAttributes(row, databasePath, type);
                    if (names.matcher(databasePath).matches() || names.matcher(shortName).matches()) {
                        elements.add(IotdbMetadataRows.element(type, databasePath, IotdbSqlDialect.TREE, row));
                    }
                }
            }
        }
        for (long offset = 0;; offset += PAGE_SIZE) {
            List<Map<String, String>> page = IotdbMetadataRows.read(statement, sql + " LIMIT " + PAGE_SIZE + " OFFSET " + offset);
            for (Map<String, String> row : page) {
                String path = row.get(nameField);
                if (!databasePath.equals(row.get("database"))) {
                    continue;
                }
                String shortName = addPathAttributes(row, path, type);
                if (names.matcher(path).matches() || names.matcher(shortName).matches()) {
                    elements.add(IotdbMetadataRows.element(type, path, IotdbSqlDialect.TREE, row));
                }
            }
            if (page.size() < PAGE_SIZE) {
                return elements;
            }
        }
    }

    static RdbValue detail(Statement statement, String database, UmiTypes type, String name) throws SQLException {
        String path = concretePath(name);
        String databasePath = concretePath(database);
        if (!path.equals(databasePath) && !path.startsWith(databasePath + ".")) {
            return null;
        }

        for (Map<String, String> row : IotdbMetadataRows.read(statement, show(type, path))) {
            if (path.equals(row.get(nameField(type))) && databasePath.equals(row.get("database"))) {
                addPathAttributes(row, path, type);
                RdbValue detail = new RdbValue();
                detail.setUmiType(type);
                detail.setValue(path);
                detail.merge(row);
                detail.setAttribute("model", IotdbSqlDialect.TREE.name());
                return detail;
            }
        }
        return null;
    }

    static String addPathAttributes(Map<String, String> row, String path, UmiTypes type) {
        // Paths returned by IoTDB are already valid. Preserve quoted nodes verbatim.
        boolean quoted = false;
        int separator = path.length() - 1;
        for (; separator >= 0; separator--) {
            char ch = path.charAt(separator);
            if (ch == '`') {
                quoted = !quoted;
            } else if (ch == '.' && !quoted) {
                break;
            }
        }
        String shortName = path.substring(separator + 1);
        row.put("name", shortName);
        if (type == UmiTypes.Timeseries) {
            row.put("device", path.substring(0, separator));
        }
        return shortName;
    }

    private static String show(UmiTypes type, String path) {
        if (type == UmiTypes.Device) {
            return "SHOW DEVICES " + path + " WITH DATABASE";
        }
        return "SHOW TIMESERIES " + path;
    }

    private static String nameField(UmiTypes type) {
        if (type == UmiTypes.Device) {
            return "device";
        }
        return "timeseries";
    }

    private static String concretePath(String path) {
        String[] parts = splitPath(path);
        if (parts.length < 2 || !"root".equals(parts[0])) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_INVALID_TREE_PATH);
        }
        for (String part : parts) {
            if (!part.startsWith("`") && part.indexOf('*') >= 0) {
                throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_INVALID_TREE_PATH);
            }
        }
        return String.join(".", parts);
    }

    private static String[] splitPath(String path) {
        if (StringUtils.isBlank(path)) {
            throw ThirdPartyApiException.as().with(IotdbI18nKeys.IOTDB_INVALID_TREE_PATH);
        }
        try {
            return (String[]) SPLIT_PATH.invoke(null, path);
        } catch (InvocationTargetException e) {
            throw ThirdPartyApiException.as().with(e.getCause(), IotdbI18nKeys.IOTDB_INVALID_TREE_PATH);
        } catch (IllegalAccessException e) {
            log.error("Call IoTDB Tree path parser failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }

    private static Method loadPathParser() {
        try {
            Class<?> parser = IotdbTreeMetadata.class.getClassLoader().loadClass("org.apache.tsfile.read.common.parser.PathNodesGenerator");
            return parser.getMethod("splitPathToNodes", String.class);
        } catch (ReflectiveOperationException e) {
            log.error("Load IoTDB Tree path parser failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
