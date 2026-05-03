package com.clougence.clouddm.api.common.rpc;

import lombok.Getter;

/**
 * @author wanshao create time is 2020/11/9
 **/
public enum SoDataDirectionType {

    CONSOLE_TO_SIDECAR("[console->sidecar]"),

    SIDECAR_TO_CONSOLE("[sidecar->console]"),

    SIDECAR_TO_TASK("[sidecar->task]"),

    TASK_TO_SIDECAR("[task->sidecar]");

    /**
     * when print async process log will use this log prefix to specify the request direction
     */
    @Getter
    private final String logPrefix;

    SoDataDirectionType(String logPrefix){
        this.logPrefix = logPrefix;
    }
}
