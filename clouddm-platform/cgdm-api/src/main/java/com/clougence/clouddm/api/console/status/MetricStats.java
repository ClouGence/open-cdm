package com.clougence.clouddm.api.console.status;

import lombok.Getter;
import lombok.Setter;

/**
 * worker stat
 *
 * @author wanshao create time is 2020/1/22
 **/
@Getter
@Setter
public class MetricStats {

    private CpuStats    cpuStats;

    private MemStats    memStats;

    private DiskStats   diskStats;

    private SystemStats systemStats;

    private UsageStats  usageStats;
}
