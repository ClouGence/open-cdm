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
