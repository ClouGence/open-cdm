package com.clougence.clouddm.comm.component.client;

import java.io.IOException;

import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;

/**
 * Decide how client acquire basic auth info to connecto to server
 *
 * @author wanshao create time is 2021/1/7
 **/
public interface RSocketClientAuthManager {

    ConnAuthDTO acquireClientAuthInfo() throws IOException;

    String acquireServerDomain() throws IOException;
}
