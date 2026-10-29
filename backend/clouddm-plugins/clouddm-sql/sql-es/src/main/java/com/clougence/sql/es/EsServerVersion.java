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
package com.clougence.sql.es;

import java.util.regex.Pattern;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

public record EsServerVersion(String number, int major, int minor, int patch) {

    private static final Pattern VERSION = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(?:-[A-Za-z0-9.-]+)?");

    public static EsServerVersion parse(String number) {
        var match = VERSION.matcher(number);
        if (!match.matches()) {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_SERVER_VERSION_INVALID);
        }

        try {
            int major = Integer.parseInt(match.group(1));
            int minor = Integer.parseInt(match.group(2));
            int patch = Integer.parseInt(match.group(3));
            if (major < 7 || major > 9 || (major == 7 && minor < 10)) {
                throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_VERSION_UNSUPPORTED, number);
            }
            return new EsServerVersion(number, major, minor, patch);
        } catch (NumberFormatException e) {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_SERVER_VERSION_INVALID);
        }
    }

    public EsVersionCapabilities capabilities() {
        Integer compatibleApiMajor = null;
        if (major >= 8) {
            compatibleApiMajor = major - 1;
        }
        return new EsVersionCapabilities(true,
            major > 7 || minor >= 12,
            major == 7,
            compatibleApiMajor,
            major == 9 || (major == 8 && minor >= 4),
            major == 9 || (major == 8 && minor >= 14),
            major == 8);
    }
}
