package com.clougence.clouddm.api.console.status;

/**
 * @author bucketli 2020-01-10 20:43
 * @since 1.1.3
 */
public enum WorkerState {
    WAIT_TO_ONLINE,
    ONLINE,
    WAIT_TO_OFFLINE,
    OFFLINE,
    ABNORMAL,
    NOT_EXIST
}
