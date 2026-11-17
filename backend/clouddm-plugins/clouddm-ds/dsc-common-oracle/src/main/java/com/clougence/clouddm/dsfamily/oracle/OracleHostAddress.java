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
package com.clougence.clouddm.dsfamily.oracle;

import com.clougence.utils.HostAndPort;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public class OracleHostAddress {
    private final HostAndPort address;
    private final String      databaseName;

    private OracleHostAddress(HostAndPort address, String databaseName){
        this.address = address;
        this.databaseName = databaseName;
    }

    public static OracleHostAddress parse(String value) {
        String host = StringUtils.trimToNull(value);
        if (host == null) {
            throw new IllegalArgumentException("Oracle host is required");
        }

        int hostEnd = 0;
        if (host.startsWith("[")) {
            hostEnd = host.indexOf(']') + 1;
        }

        int portSeparator = host.indexOf(':', hostEnd);
        int suffixSeparator = -1;
        if (portSeparator >= 0) {
            int next = host.indexOf(':', portSeparator + 1);
            if (next >= 0 && (hostEnd > 0 || (host.indexOf(':', next + 1) < 0 && host.substring(portSeparator + 1, next).matches("[0-9]+")))) {
                suffixSeparator = next;
            }
        }

        String databaseName = null;
        if (suffixSeparator >= 0) {
            databaseName = StringUtils.trimToNull(host.substring(suffixSeparator + 1));
            host = host.substring(0, suffixSeparator);
        }
        return new OracleHostAddress(HostAndPort.parse(host, 1521), databaseName);
    }
}
