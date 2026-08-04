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
package com.clougence.clouddm.ds.mongodb.execute;

import java.nio.charset.StandardCharsets;

import com.clougence.drivers.adapter.JdbcDriver;
import com.clougence.utils.StringUtils;

public final class MongoConnectionUri {

    private MongoConnectionUri(){
    }

    public static String buildSrvJdbcUrl(String adapterName, String host, String username, String password) {
        StringBuilder uri = new StringBuilder(JdbcDriver.START_URL).append(adapterName).append("://");
        if (StringUtils.isNotBlank(username)) {
            uri.append(percentEncode(username)).append(':').append(percentEncode(password)).append('@');
        }
        uri.append(normalizeSrvHost(host));
        return uri.toString();
    }

    public static String normalizeSrvHost(String value) {
        String host = StringUtils.trimToNull(value);
        if (host == null) {
            throw new IllegalArgumentException("MongoDB SRV host is required.");
        }
        if (StringUtils.containsAny(host, ":/?#@,")) {
            throw new IllegalArgumentException("MongoDB SRV host must be a single hostname without scheme, port, path or query parameters.");
        }

        return host;
    }

    private static String percentEncode(String value) {
        StringBuilder encoded = new StringBuilder();
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            int c = b & 0xff;
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '-' || c == '.' || c == '_' || c == '~') {
                encoded.append((char) c);
            } else {
                encoded.append('%');
                encoded.append(Character.toUpperCase(Character.forDigit((c >>> 4) & 0x0f, 16)));
                encoded.append(Character.toUpperCase(Character.forDigit(c & 0x0f, 16)));
            }
        }
        return encoded.toString();
    }
}
