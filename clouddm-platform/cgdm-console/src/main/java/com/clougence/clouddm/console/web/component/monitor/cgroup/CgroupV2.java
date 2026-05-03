package com.clougence.clouddm.console.web.component.monitor.cgroup;

import java.util.List;
import java.util.Map;

import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CgroupV2 implements CgroupCollector {

    private final static String cpuCountShell    = "sudo -n cat /sys/fs/cgroup/cpu.max";

    private final static String memoryTotalShell = "sudo -n cat /sys/fs/cgroup/memory.max";

    private final static String memoryUsedShell  = "sudo -n cat /sys/fs/cgroup/memory.current";

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
            Map<String, String> cpuMaxAndSoft = ShellExecutor.exec(cpuCountShell, 5, "\\s+");
            if (CollectionUtils.isEmpty(cpuMaxAndSoft)) {
                return -1;
            }

            int all = Integer.parseInt(cpuMaxAndSoft.keySet().iterator().next());
            if (all <= 0) {
                return -1;
            }
            return all / 100000;
        } catch (Exception e) {
            return -1;
        }
    }
}
