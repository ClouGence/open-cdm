package com.clougence.clouddm.comm.model;

/**
 * @author wanshao create time is 2021/1/15
 **/
public enum RSocketSendType {
    /**
     * Send to specified worker
     */
    SPECIFIED,
    /**
     * Choose a worker from cluster
     */
    CLUSTER;
}
