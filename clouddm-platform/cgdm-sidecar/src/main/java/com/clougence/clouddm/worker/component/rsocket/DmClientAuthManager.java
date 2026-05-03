package com.clougence.clouddm.worker.component.rsocket;

import java.io.IOException;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.comm.component.client.RSocketClientAuthManager;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/14
 **/
@Slf4j
public class DmClientAuthManager implements RSocketClientAuthManager {

    private ConnAuthDTO authInfo = null;

    private ConnAuthDTO loadGlobalConf(boolean asServer) throws IOException {
        if (this.authInfo != null) {
            return this.authInfo;
        }

        synchronized (this) {
            if (this.authInfo != null) {
                return this.authInfo;
            }

            this.authInfo = GlobalConfUtils.loadGlobalConf();
            if (asServer) {
                if (StringUtils.isBlank(this.authInfo.getConsoleHost())) {
                    throw new IllegalArgumentException("properties console domain in global config (" + this.authInfo.getGlobalConfResource() + ") are empty.");
                }
            } else {
                if (StringUtils.isBlank(this.authInfo.getWsn()) || StringUtils.isBlank(this.authInfo.getAk()) || StringUtils.isBlank(this.authInfo.getSk())) {
                    throw new IllegalArgumentException("properties in global config (" + this.authInfo + ") are empty.");
                }
            }

            return this.authInfo;
        }
    }

    @Override
    public ConnAuthDTO acquireClientAuthInfo() throws IOException {
        return loadGlobalConf(false);
    }

    @Override
    public String acquireServerDomain() throws IOException {
        return loadGlobalConf(true).getConsoleHost();
    }
}
