/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.worker.component.session.ssh;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.utils.HostAndPort;
import com.jcraft.jsch.JSchException;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SshTunnelManager {

    @Resource
    private SshConnectionManager sshConnectionManager;

    public SshTunnelHandle open(DataSourceConfig dsConfig) throws Exception {
        HostAndPort target = HostAndPort.parse(dsConfig.getHost());
        if (target.getPort() < 0) {
            throw new IllegalArgumentException("datasource host must include target host and port when SSH proxy is enabled.");
        }
        // Metadata calls can reuse this config after a previous tunnel has already closed.
        DataSourceConfig tunnelConfig = BeanUtils.instantiateClass(dsConfig.getClass());
        BeanUtils.copyProperties(dsConfig, tunnelConfig);
        com.jcraft.jsch.Session sshSession = this.sshConnectionManager.openTunnelSession(dsConfig.getSshConfigId());

        try {
            int localPort = sshSession.setPortForwardingL(0, target.getHost(), target.getPort());
            tunnelConfig.setHost("127.0.0.1:" + localPort);
            log.info("finish open ssh tunnel for datasource, dsType={}, sshConfigId={}, targetHost={}, targetPort={}, localHost=127.0.0.1, localPort={}",//
                    dsConfig.getDataSourceType(), dsConfig.getSshConfigId(), target.getHost(), target.getPort(), localPort);
            return new SshTunnelHandle(tunnelConfig, sshSession);
        } catch (JSchException | RuntimeException e) {
            log.warn("open ssh tunnel for datasource failed, dsType={}, sshConfigId={}, targetHost={}, targetPort={}",//
                    dsConfig.getDataSourceType(), dsConfig.getSshConfigId(), target.getHost(), target.getPort(), e);
            if (sshSession.isConnected()) {
                sshSession.disconnect();
            }
            throw e;
        }
    }

    public boolean isEnabled(DataSourceConfig dsConfig) {
        return Boolean.TRUE.equals(dsConfig.getSshProxyEnabled()) && dsConfig.getSshConfigId() != null;
    }

}
