package com.clougence.rdp.service.enumeration;

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
    CLUSTER
}
