package com.clougence.clouddm.console.web.component.monitor.cgroup;

import java.util.List;

import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CgroupV1 implements CgroupCollector {

    private final static String cpuCountShell    = "sudo -n cat /sys/fs/cgroup/cpu/cpu.cfs_quota_us";
    private final static String memoryTotalShell = "sudo -n cat /sys/fs/cgroup/memory/memory.limit_in_bytes";
    private final static String memoryUsedShell  = "sudo -n cat /sys/fs/cgroup/memory/memory.usage_in_bytes";

    public long getUsedMemory() {
        try {
            List<String> res = ShellExecutor.exec(memoryUsedShell, 5);
            if (CollectionUtils.isEmpty(res) || !StringUtils.isNumeric(res.get(0))) {
                return -1;
            }
            return Long.parseLong(res.get(0));
        } catch (Exception e) {
            return -1;
        }
    }

    public long getTotalMemory() {
        try {
            List<String> res = ShellExecutor.exec(memoryTotalShell, 5);
            if (CollectionUtils.isEmpty(res) || !StringUtils.isNumeric(res.get(0))) {
                return -1;
            }
            return Long.parseLong(res.get(0));
        } catch (Exception e) {
            return -1;
        }
    }

    public int getCpuCount() {
        try {
            List<String> res = ShellExecutor.exec(cpuCountShell, 5);
            if (CollectionUtils.isEmpty(res) || !StringUtils.isNumeric(res.get(0))) {
                return -1;
            }
            return Integer.parseInt(res.get(0)) / 100000;
        } catch (Exception e) {
            return -1;
        }
    }
}
