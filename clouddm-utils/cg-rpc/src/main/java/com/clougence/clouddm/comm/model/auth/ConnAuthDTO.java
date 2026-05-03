package com.clougence.clouddm.comm.model.auth;

import java.net.URL;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/1/7
 **/
@Getter
@Setter
public class ConnAuthDTO {

    private String ak;

    private String sk;

    private String ip;

    /**
     * sidecar's unique identify
     */
    private String wsn;

    private long   sendAuthTimeMs;

    private String consoleHost;
    private String consolePort;

    private URL    globalConfResource;
}
