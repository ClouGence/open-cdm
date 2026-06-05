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
package com.clougence.clouddm.dsfamily.mysql.analysis.secrules;

public enum MyFlushType {

    PRIVILEGES,
    LOGS,
    HOSTS,
    STATUS,
    TABLES,
    USER_RESOURCE;

    public static MyFlushType valueOfString(String type) {
        if (type.equalsIgnoreCase("PRIVILEGES")) {
            return PRIVILEGES;
        } else if (type.equalsIgnoreCase("LOGS")) {
            return LOGS;
        } else if (type.equalsIgnoreCase("HOSTS")) {
            return HOSTS;
        } else if (type.equalsIgnoreCase("STATUS")) {
            return STATUS;
        } else if (type.toUpperCase().startsWith("TABLES")) {
            return TABLES;
        } else if (type.equalsIgnoreCase("USER_RESOURCES")) {
            return USER_RESOURCE;
        } else {
            throw new IllegalArgumentException("Not support: " + type);
        }
    }

}
