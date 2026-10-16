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
package com.clougence.clouddm.dsfamily.oracle.execute.fetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.utils.StringUtils;

import oracle.sql.CharacterSet;

public final class OracleClientCharsetRegistry {

    private static final Map<String, CharacterSet> CHARSETS = new ConcurrentHashMap<>();

    private OracleClientCharsetRegistry(){
    }

    public static CharacterSet resolve(Connection connection, String charsetName) throws SQLException {
        String normalizedName = normalize(charsetName);
        if (StringUtils.isBlank(normalizedName)) {
            return null;
        }
        CharacterSet cached = CHARSETS.get(normalizedName);
        if (cached != null) {
            return cached;
        }

        int charsetId;
        try (PreparedStatement statement = connection.prepareStatement("SELECT NLS_CHARSET_ID(?) FROM DUAL")) {
            statement.setString(1, normalizedName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw unsupportedCharset(normalizedName, null);
                }
                charsetId = resultSet.getInt(1);
                if (resultSet.wasNull() || charsetId <= 0) {
                    throw unsupportedCharset(normalizedName, null);
                }
            }
        }

        try {
            CHARSETS.putIfAbsent(normalizedName, CharacterSet.make(charsetId));
        } catch (RuntimeException e) {
            throw unsupportedCharset(normalizedName, e);
        }
        return CHARSETS.get(normalizedName);
    }

    private static String normalize(String charsetName) {
        return StringUtils.trimToEmpty(charsetName).toUpperCase(Locale.ROOT);
    }

    private static SQLException unsupportedCharset(String charsetName, Throwable cause) {
        return new SQLException("Unsupported Oracle client character set: " + charsetName, cause);
    }
}
