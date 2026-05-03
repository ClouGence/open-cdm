package com.clougence.clouddm.console.web.component.monitor.cgroup;

public interface CgroupCollector {

    long getUsedMemory();

    long getTotalMemory();

    int getCpuCount();
}
