package com.clougence.clouddm.console.web.component.monitor.cgroup;

public enum CgroupVersion {

    V1("cgroup"),
    V2("cgroup2");

    public final String name;

    CgroupVersion(String name){
        this.name = name;
    }
}
