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
package com.clougence.clouddm.api.sidecar.session.drivers;

import java.util.List;

import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class DriverUtils {

    public static DriverRef parseDriverRef(String driverSpec) {
        if (StringUtils.isBlank(driverSpec)) {
            throw new IllegalArgumentException("driverVersion is blank.");
        }

        String trimmed = driverSpec.trim();
        List<String> driverParts = JsonUtils.toListUseType(trimmed, String.class);
        if (driverParts == null || driverParts.size() < 2) {
            throw new IllegalArgumentException("driverVersion must use format [\"family\",\"/version\"]: " + trimmed);
        }

        String driverFamily = StringUtils.trimToNull(driverParts.get(0));
        String driverVersion = normalizeDriverVersion(driverParts.get(1));
        if (StringUtils.isBlank(driverFamily)) {
            throw new IllegalArgumentException("driver family is blank: " + trimmed);
        }
        if (StringUtils.isBlank(driverVersion)) {
            throw new IllegalArgumentException("driver version is blank: " + trimmed);
        }

        return new DriverRef(driverFamily, driverVersion);
    }

    protected static String normalizeDriverVersion(String driverVersion) {
        String normalized = StringUtils.trimToNull(driverVersion);
        while (normalized != null && normalized.startsWith("/")) {
            normalized = StringUtils.trimToNull(normalized.substring(1));
        }
        return normalized;
    }
}
