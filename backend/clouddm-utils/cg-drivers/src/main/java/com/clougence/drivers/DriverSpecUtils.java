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
package com.clougence.drivers;

import java.util.List;

import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public final class DriverSpecUtils {

    private DriverSpecUtils(){
    }

    public static String resolveDriverFamily(String driverSpec) {
        String trimmed = StringUtils.trimToNull(driverSpec);
        if (trimmed == null) {
            return null;
        }

        if (trimmed.startsWith("[")) {
            try {
                List<String> driverParts = JsonUtils.toListUseType(trimmed, String.class);
                if (driverParts != null && !driverParts.isEmpty()) {
                    return StringUtils.trimToNull(driverParts.get(0));
                }
            } catch (Exception ignored) {
                // fall through
            }
        }

        int slashIndex = trimmed.lastIndexOf('/');
        return slashIndex > 0 ? StringUtils.trimToNull(trimmed.substring(0, slashIndex)) : null;
    }

    public static boolean matchesDriverFamily(String driverSpec, String driverFamily) {
        return StringUtils.equals(resolveDriverFamily(driverSpec), driverFamily);
    }

    public static String resolveDriverVersion(String driverSpec) {
        String trimmed = StringUtils.trimToNull(driverSpec);
        if (trimmed == null) {
            return null;
        }

        if (trimmed.startsWith("[")) {
            try {
                List<String> driverParts = JsonUtils.toListUseType(trimmed, String.class);
                if (driverParts != null && driverParts.size() >= 2) {
                    return normalizeDriverVersion(driverParts.get(1));
                }
            } catch (Exception ignored) {
                // fall through
            }
        }

        int slashIndex = trimmed.lastIndexOf('/');
        if (slashIndex >= 0) {
            trimmed = trimmed.substring(slashIndex + 1);
        }
        return normalizeDriverVersion(trimmed);
    }

    public static String normalizeDriverVersion(String driverVersion) {
        String normalized = StringUtils.trimToNull(driverVersion);
        while (normalized != null && normalized.startsWith("/")) {
            normalized = StringUtils.trimToNull(normalized.substring(1));
        }
        return normalized;
    }

}
