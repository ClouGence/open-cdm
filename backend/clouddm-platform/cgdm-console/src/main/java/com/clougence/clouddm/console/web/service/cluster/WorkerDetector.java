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
package com.clougence.clouddm.console.web.service.cluster;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.console.web.constants.HealthLevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-30 10:45
 * @since 1.1.3
 */
@Service
@Slf4j
public class WorkerDetector {

    private static final long HEARTBEAT_TIMEOUT_MS = 15_000L;

    public boolean isLooseAlive(DmSysWorkerDO workerDO) {
        return !isHeartbeatTimeout(workerDO);
    }

    public boolean isCriticalAlive(DmSysWorkerDO workerDO) {
        return !isHeartbeatTimeout(workerDO);
    }

    public HealthLevel getHealthLevel(DmSysWorkerDO workerDO) {
        WorkerState state = workerDO.getWorkerState();
        if (state == WorkerState.ONLINE && !isHeartbeatTimeout(workerDO)) {
            return HealthLevel.Health;
        } else if (state == WorkerState.WAIT_TO_ONLINE) {
            return HealthLevel.SubHealth;
        }

        return HealthLevel.Unhealthy;
    }

    protected boolean isHeartbeatTimeout(DmSysWorkerDO workerDO) {
        if (workerDO == null || workerDO.getLastHeartbeatReportMs() == null) {
            return true;
        }

        Duration duration = fromLastUpdataToNow(workerDO.getLastHeartbeatReportMs());
        return duration.toMillis() > HEARTBEAT_TIMEOUT_MS;
    }

    protected Duration fromLastUpdataToNow(Long lastHeartbeatReportMs) {
        return Duration.ofMillis(System.currentTimeMillis() - lastHeartbeatReportMs);
    }
}
