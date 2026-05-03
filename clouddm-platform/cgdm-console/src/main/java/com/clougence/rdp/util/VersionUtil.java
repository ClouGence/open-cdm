package com.clougence.rdp.util;

import com.clougence.utils.StringUtils;

public class VersionUtil {

    /**
     * @return Three situations: 1. current Version > online Version return -1; 2. current version = online version returns 0; 3. current version < online version returns 1;
     */
    public static int compareVersion(String onlineVersion, String currentVersion) {
        onlineVersion = substring(onlineVersion);
        currentVersion = substring(currentVersion);
        if (onlineVersion.equals(currentVersion)) {
            return 0;
        }
        String[] version1Array = onlineVersion.split("\\.");
        String[] version2Array = currentVersion.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    private static String substring(String version) {
        if (StringUtils.isBlank(version)) {
            throw new IllegalArgumentException("version params can be not blank");
        }
        if (!version.contains("_")) {
            return version;
        } else {
            return version.substring(0, version.indexOf("_"));
        }
    }
}
