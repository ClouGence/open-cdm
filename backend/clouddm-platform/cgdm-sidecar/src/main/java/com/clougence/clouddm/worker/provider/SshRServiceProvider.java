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
package com.clougence.clouddm.worker.provider;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.ssh.SshRService;
import com.clougence.clouddm.api.sidecar.session.ssh.TestResultDTO;
import com.clougence.clouddm.base.metadata.ds.SshConfig;
import com.clougence.clouddm.base.metadata.ds.SshKnownHost;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.worker.component.session.ssh.SshConnectionManager;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.Session;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class SshRServiceProvider implements SshRService {

    @Resource
    private SshConnectionManager connectionManager;

    private void disconnect(Session session) {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    @Override
    public List<SshKnownHost> probeKnownHosts(RSocketSendDTO sendDTO, SshConfig config) {
        Session session = null;
        try {
            session = this.connectionManager.openSession(config, false, true);
            HostKey hostKey = session.getHostKey();

            SshKnownHost item = new SshKnownHost();
            item.setHost(config.getHost());
            item.setPort(config.getPort());
            item.setType(hostKey.getType());
            item.setKey(hostKey.getKey());
            return Collections.singletonList(item);
        } catch (Exception e) {
            throw new IllegalStateException("probe ssh known hosts failed: " + e.getMessage(), e);
        } finally {
            disconnect(session);
        }
    }

    @Override
    public TestResultDTO testConnection(RSocketSendDTO sendDTO, SshConfig config) {
        long start = System.currentTimeMillis();
        String wsnString = (sendDTO == null) ? null : sendDTO.getWorkerSeqNumber();
        Session session = null;

        try {
            session = this.connectionManager.openSession(config, true, false);
            TestResultDTO result = new TestResultDTO();
            result.setSuccess(true);
            result.setMessage("connection successful");
            result.setCostMs(System.currentTimeMillis() - start);
            log.info("finish test ssh connection in sidecar, workerSeqNumber={}, host={}, port={}, success=true, costMs={}",//
                    wsnString, config.getHost(), config.getPort(), result.getCostMs());
            return result;
        } catch (Exception e) {
            long costMs = System.currentTimeMillis() - start;
            log.warn("test ssh connection failed in sidecar, workerSeqNumber={}, host={}, port={}, costMs={}",//
                    wsnString, config.getHost(), config.getPort(), costMs, e);
            TestResultDTO result = new TestResultDTO();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setCostMs(costMs);
            return result;
        } finally {
            disconnect(session);
        }
    }
}
