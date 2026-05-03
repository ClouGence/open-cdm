package com.clougence.clouddm.comm.model;

import lombok.Getter;

/**
 * @author wanshao create time is 2020/11/9
 **/
public enum RSocketDirectionType {

    /**
     * console to sidecar direction
     */
    SERVER_TO_CLIENT("[server->client]"),
    /**
     * sidecar to console direction
     */
    CLIENT_TO_SERVER("[client->server]");

    RSocketDirectionType(String logPrefix){
        this.logPrefix = logPrefix;
    }

    /**
     * when print async process log will use this log prefix to specify the request direction
     */
    @Getter
    private final String logPrefix;
}
