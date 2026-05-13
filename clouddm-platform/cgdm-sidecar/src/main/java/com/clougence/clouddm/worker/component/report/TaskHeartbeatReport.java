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
package com.clougence.clouddm.worker.component.report;

import java.util.Date;

import com.clougence.clouddm.api.console.status.ConsoleStatusRService;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * collect worker stat and report to console
 *
 * @author wanshao create time is 2020/1/22
 **/
@Slf4j
@Component
public class TaskHeartbeatReport implements Runnable {

    @Resource
    private ConsoleStatusRService statusRService;
    private WorkerIdentity        workerIdentity;

    private WorkerIdentity identity() throws Exception {
        if (this.workerIdentity == null) {
            this.workerIdentity = ReportUtils.getIdentity();
        }
        return this.workerIdentity;
    }

    @Override
    public void run() {
        try {
            requestAndHandle();
        } catch (Exception e) {
            log.error("heartbeat requestAndHandle failed, msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    private void requestAndHandle() throws Exception {
        Date now = new Date();
        WorkerState state = this.statusRService.fetchStatusAndHeartbeat(this.identity(), now);
        switch (state) {
            case WAIT_TO_OFFLINE: {
                log.error("receive state is WAIT_TO_OFFLINE,try to update state to OFFLINE and then exist.");
                this.statusRService.reportStatus(this.identity(), now, WorkerState.OFFLINE);
                ReportUtils.existSystem();
                break;
            }
            case ONLINE:
            case WAIT_TO_ONLINE:
            case ABNORMAL: {
                log.info("receive state is " + state + ",heartbeat success.");
                if (state != WorkerState.ONLINE) {
                    this.statusRService.reportStatus(this.identity(), now, WorkerState.ONLINE);
                }
                this.statusRService.reportAddress(this.identity(), now, ReportUtils.tryFetchLocalIp(), ReportUtils.tryFetchExternalIp());
                break;
            }
            case NOT_EXIST:
            case OFFLINE:
                log.info("receive state is OFFLINE.try to exist.");
                ReportUtils.existSystem();
                break;
            default:
                throw new IllegalArgumentException("WorkerState is " + state + ",unexpected. ");
        }
    }
}
