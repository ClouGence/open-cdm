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
package com.clougence.sql.mysql.analysis.security.domain;

import java.util.Locale;

public enum MyFlushType {

    BINARY_LOGS,
    DES_KEY_FILE,
    ENGINE_LOGS,
    ERROR_LOGS,
    GENERAL_LOGS,
    PRIVILEGES,
    LOGS,
    HOSTS,
    OPTIMIZER_COSTS,
    QUERY_CACHE,
    RELAY_LOGS,
    SLOW_LOGS,
    STATUS,
    TABLES,
    USER_RESOURCE;

    public static MyFlushType valueOfString(String type) {
        String normalized = type.replace("_", "").replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
        if (normalized.startsWith("TABLE")) {
            return TABLES;
        }
        return switch (normalized) {
            case "BINARYLOGS" -> BINARY_LOGS;
            case "DESKEYFILE" -> DES_KEY_FILE;
            case "ENGINELOGS" -> ENGINE_LOGS;
            case "ERRORLOGS" -> ERROR_LOGS;
            case "GENERALLOGS" -> GENERAL_LOGS;
            case "PRIVILEGES" -> PRIVILEGES;
            case "LOGS" -> LOGS;
            case "HOSTS" -> HOSTS;
            case "OPTIMIZERCOSTS" -> OPTIMIZER_COSTS;
            case "QUERYCACHE" -> QUERY_CACHE;
            case "RELAYLOGS", "RELAYLOGSFORCHANNEL" -> RELAY_LOGS;
            case "SLOWLOGS" -> SLOW_LOGS;
            case "STATUS" -> STATUS;
            case "USERRESOURCES" -> USER_RESOURCE;
            default -> {
                if (normalized.startsWith("RELAYLOGSFORCHANNEL")) {
                    yield RELAY_LOGS;
                }
                throw new IllegalArgumentException("Not support: " + type);
            }
        };
    }

}
