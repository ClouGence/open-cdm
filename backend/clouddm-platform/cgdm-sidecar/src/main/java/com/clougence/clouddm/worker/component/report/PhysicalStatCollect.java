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
package com.clougence.clouddm.worker.component.report;

import java.math.BigDecimal;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.clougence.clouddm.api.console.status.CpuStats;
import com.clougence.clouddm.api.console.status.DiskStats;
import com.clougence.clouddm.api.console.status.MemStats;
import com.clougence.clouddm.api.console.status.SystemStats;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.NumberUtils;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Execute shell to get local system's info or using oshi library to collect system stat info
 *
 * @author wanshao create time is 2020/1/14
 **/
@Slf4j
public class PhysicalStatCollect {

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

            cpuStats.setPhysicalCoreCount(processor.getPhysicalProcessorCount());
            cpuStats.setLogicalCoreCount(processor.getLogicalProcessorCount());
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
        } catch (Throwable e) {
            String errMsg = "get cpu stat failed,but ignore,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }

        return cpuStats;
    }

    public static DiskStats getDiskStat() {
        DiskStats diskStats = new DiskStats();

        try {
            FileStore fileStore = Files.getFileStore(Paths.get("/"));
            AtomicLong totalDiskGb = new AtomicLong(fileStore.getTotalSpace() / 1024 / 1024 / 1024);
            AtomicLong freeSpaceGb = new AtomicLong(fileStore.getUsableSpace() / 1024 / 1024 / 1024);
            AtomicLong usedSpaceGb = new AtomicLong(totalDiskGb.longValue() - freeSpaceGb.longValue());
            BigDecimal diskUsage = NumberUtils.round(usedSpaceGb.doubleValue() / totalDiskGb.doubleValue() * 100, 2);

            diskStats.setTotalDiskGB(totalDiskGb);
            diskStats.setUsedDiskGB(usedSpaceGb);
            diskStats.setFreeDiskGB(freeSpaceGb);
            diskStats.setDiskUsage(diskUsage);
        } catch (Throwable e) {
            String errMsg = "get disk stat failed but ignore,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }

        return diskStats;
    }

    /**
     * get memory stat info with oshi
     */
    public static MemStats getMemStat() {
        MemStats memStats = new MemStats();
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            GlobalMemory memory = hal.getMemory();

            // default value is bytes, convert to GB/MB
            BigDecimal availableMb = NumberUtils.round(memory.getAvailable() / 1024d / 1024, 2);
            BigDecimal totalMb = NumberUtils.round(memory.getTotal() / 1024d / 1024d, 2);
            BigDecimal usedMb = totalMb.subtract(availableMb);

            BigDecimal availableGb = NumberUtils.round(availableMb.doubleValue() / 1024, 2);
            BigDecimal totalGb = NumberUtils.round(totalMb.doubleValue() / 1024, 2);
            BigDecimal usedGb = totalGb.subtract(availableGb);
            BigDecimal useRate = NumberUtils.round(usedGb.doubleValue() * 100 / totalGb.doubleValue(), 2);

            // fill stat dto
            memStats.setTotalMemoryGb(totalGb);
            memStats.setFreeMemoryGb(availableGb);
            memStats.setUsedMemoryGb(usedGb);
            memStats.setTotalMemoryMb(totalMb);
            memStats.setFreeMemoryMb(availableMb);
            memStats.setUsedMemoryMb(usedMb);
            memStats.setMemoryUsage(useRate);

            memStats.setJvmTotalMemoryMb(NumberUtils.round(Runtime.getRuntime().maxMemory() / 1024d / 1024d, 2));
            memStats.setJvmFreeMemoryMb(NumberUtils.round(Runtime.getRuntime().freeMemory() / 1024d / 1024d, 2));
            memStats.setJvmUsedMemoryMb(NumberUtils.round((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1024d / 1024d, 2));
            memStats.setJvmMemoryUsage(NumberUtils.round(memStats.getJvmUsedMemoryMb().doubleValue() * 100 / memStats.getJvmTotalMemoryMb().doubleValue(), 2));
        } catch (Throwable e) {
            String errMsg = "get memory stat failed but ignore,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }

        return memStats;
    }

    /**
     * get basic system info
     */
    public static SystemStats getSystemStat() {
        SystemStats systemStats = new SystemStats();
        try {
            Properties props = System.getProperties();
            String osName = props.getProperty("os.name");
            String osArch = props.getProperty("os.arch");

            systemStats.setOsName(osName);
            systemStats.setOsArch(osArch);
        } catch (Throwable e) {
            String errMsg = "get system stat failed but ignore,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }

        return systemStats;
    }
}
