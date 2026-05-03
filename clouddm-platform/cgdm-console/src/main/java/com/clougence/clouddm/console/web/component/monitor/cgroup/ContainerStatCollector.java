package com.clougence.clouddm.console.web.component.monitor.cgroup;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.clougence.clouddm.api.console.status.CpuStats;
import com.clougence.clouddm.api.console.status.MemStats;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.ExecutingCommand;
import oshi.util.FileUtil;

@Slf4j
public class ContainerStatCollector {

    final public static String          DEFAULT_CGROUP_PATH = "/sys/fs/cgroup";

    final public static CgroupCollector cgroupCollector;

    static {
        CgroupVersion cgourpVersion = getCgroupVersion();
        log.info("container cgroup version: " + cgourpVersion);
        if (cgourpVersion == CgroupVersion.V1) {
            cgroupCollector = new CgroupV1();
        } else {
            cgroupCollector = new CgroupV2();
        }
    }

    public static CpuStats getCpuStat() {
        CpuStats cpuStats = new CpuStats();

        try {
            SystemInfo systemInfo = new SystemInfo();
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            long[] prevTicks = processor.getSystemCpuLoadTicks();

            // sleep 1 seconds for sample
            TimeUnit.SECONDS.sleep(1);

            long[] ticks = processor.getSystemCpuLoadTicks();
            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

            double loadRatio = processor.getSystemCpuLoadBetweenTicks(prevTicks);
            double[] loadAverage = processor.getSystemLoadAverage(3);

            int cpuCount = cgroupCollector.getCpuCount();
            int fatherLogicCpu = processor.getLogicalProcessorCount();
            if (cpuCount > fatherLogicCpu || cpuCount <= 0) {
                cpuCount = fatherLogicCpu;
            }
            cpuStats.setLogicalCoreCount(cpuCount);

            cpuStats.setPhysicalCoreCount(processor.getPhysicalProcessorCount());
            cpuStats.setUserRatio(NumberUtils.round(100d * user * 1.0 / totalCpu, 2));
            cpuStats.setNiceRatio(NumberUtils.round(100d * user * 1.0 / totalCpu, 2));
            cpuStats.setSysRatio(NumberUtils.round(100d * sys * 1.0 / totalCpu, 2));
            cpuStats.setIdleRatio(NumberUtils.round(100d * idle * 1.0 / totalCpu, 2));
            cpuStats.setIoWaitRatio(NumberUtils.round(100d * iowait * 1.0 / totalCpu, 2));
            cpuStats.setIrqRatio(NumberUtils.round(100d * irq * 1.0 / totalCpu, 2));
            cpuStats.setSoftIrqRatio(NumberUtils.round(100d * softirq * 1.0 / totalCpu, 2));
            cpuStats.setStealRatio(NumberUtils.round(100d * steal * 1.0 / totalCpu, 2));
            cpuStats.setAvgUsageRatio(NumberUtils.round(100d * (1.0 - (idle * 1.0 / totalCpu)), 2));
            cpuStats.setOneMinuteAvgLoad(NumberUtils.round(loadAverage[0], 2));
            cpuStats.setFiveMinuteAvgLoad(NumberUtils.round(loadAverage[1], 2));
            cpuStats.setFifteenMinuteAvgLoad(NumberUtils.round(loadAverage[2], 2));
            cpuStats.setAvgLoadRatio(NumberUtils.round(loadRatio, 2));
        } catch (Exception e) {
            String errMsg = "get cpu stat failed,but ignore,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }

        return cpuStats;
    }

    public static MemStats getMemStat() {
        MemStats memStats = new MemStats();

        try {
            long usedMemory = cgroupCollector.getUsedMemory();
            long totalMemory = cgroupCollector.getTotalMemory();

            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            GlobalMemory memory = hal.getMemory();

            long fatherTotalMemory = memory.getTotal();
            long fatherUsedMemory = memory.getTotal() - memory.getAvailable();

            if (totalMemory > fatherTotalMemory || usedMemory > fatherTotalMemory || totalMemory <= 0 || usedMemory <= 0) {
                totalMemory = fatherTotalMemory;
                usedMemory = fatherUsedMemory;
            }

            BigDecimal usedMb = NumberUtils.round(usedMemory / 1024d / 1024, 2);
            BigDecimal totalMb = NumberUtils.round(totalMemory / 1024d / 1024d, 2);
            BigDecimal availableMb = totalMb.subtract(usedMb);

            BigDecimal availableGb = NumberUtils.round(availableMb.doubleValue() / 1024, 2);
            BigDecimal totalGb = NumberUtils.round(totalMb.doubleValue() / 1024, 2);
            BigDecimal usedGb = totalGb.subtract(availableGb);

            BigDecimal useRate;
            if (totalGb.longValue() == 0L) {
                useRate = new BigDecimal(0);
            } else {
                useRate = NumberUtils.round(usedGb.doubleValue() * 100 / totalGb.doubleValue(), 2);
            }

            // fill stat dto
            memStats.setTotalMemoryGb(totalGb);
            memStats.setFreeMemoryGb(availableGb);
            memStats.setUsedMemoryGb(usedGb);
            memStats.setTotalMemoryMb(totalMb);
            memStats.setFreeMemoryMb(availableMb);
            memStats.setUsedMemoryMb(usedMb);
            memStats.setMemoryUsage(useRate);
        } catch (Exception e) {
            String errMsg = "get memory stat failed,but ignore,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }

        return memStats;
    }

    private static CgroupVersion getCgroupVersion() {
        List<String> res = ExecutingCommand.runNative("stat -fc %T " + DEFAULT_CGROUP_PATH);

        if (res.size() > 0 && StringUtils.equalsIgnoreCase("cgroup2fs", res.get(0))) {
            return CgroupVersion.V2;
        }

        if (StringUtils.isNotEmpty(FileUtil.getStringFromFile(DEFAULT_CGROUP_PATH + "cgroup.type"))) {
            return CgroupVersion.V2;
        }

        return CgroupVersion.V1;
    }
}
