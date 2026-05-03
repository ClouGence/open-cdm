package com.clougence.clouddm.console.web.util;

import java.util.List;

import com.clougence.clouddm.console.web.component.monitor.cgroup.ShellExecutor;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;

@Slf4j
public class DmConsoleHardwareUtil {

    public static final String  UNKNOWN            = "unknown";

    private static final String PRODUCT_UUID_SHELL = "sudo -n cat /sys/devices/virtual/dmi/id/product_uuid";

    public static String getLocalUUID() {
        String uuidByFile = getLocalUUIDByFile();
        if (!uuidByFile.equals(UNKNOWN)) {
            return uuidByFile.toLowerCase();
        }
        try {
            return new SystemInfo().getHardware().getComputerSystem().getHardwareUUID().toLowerCase();
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    private static String getLocalUUIDByFile() {
        try {
            List<String> res = ShellExecutor.exec(PRODUCT_UUID_SHELL, 5);
            if (CollectionUtils.isEmpty(res) || StringUtils.isBlank(res.get(0))) {
                return UNKNOWN;
            }
            return res.get(0);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
