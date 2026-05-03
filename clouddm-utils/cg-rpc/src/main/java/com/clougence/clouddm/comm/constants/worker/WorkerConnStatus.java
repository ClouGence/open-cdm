package com.clougence.clouddm.comm.constants.worker;

/**
 * @author wanshao create time is 2021/1/7
 **/
public enum WorkerConnStatus {
    /**
     * sidecar's rsocket connection is connected
     */
    CONNECTED,
    /**
     * sidecar's socket connection is disconnected
     */
    DISCONNECTED,
    /**
     * new status means the sidecar never connect to console, usually it is newly added and never start
     */
    NEW
}
