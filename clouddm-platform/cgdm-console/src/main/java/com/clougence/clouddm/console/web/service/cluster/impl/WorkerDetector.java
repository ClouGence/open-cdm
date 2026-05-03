package com.clougence.clouddm.console.web.service.cluster.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.console.web.constants.HealthLevel;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerHeartbeatMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerStatusMapper;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerHeartbeatDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerStatusDO;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-30 10:45
 * @since 1.1.3
 */
@Service
@Slf4j
public class WorkerDetector {

    private static final double     HEARTBEAT_TIMEOUT_MAGNIFICATION = 2.5;

    @Resource
    private DmConsoleConfig         dmConfig;
    @Resource
    private DmWorkerStatusMapper    sidecarStatusMapper;
    @Resource
    private DmWorkerHeartbeatMapper heartbeatMapper;

    public boolean isLooseAlive(DmWorkerDO workerDO) {
        return isWorkerConnected(workerDO.getWorkerSeqNumber()) || !isHeartbeatTimeout(workerDO.getWorkerSeqNumber());
    }

    public boolean isCriticalAlive(DmWorkerDO workerDO) {
        return isWorkerConnected(workerDO.getWorkerSeqNumber()) && !isHeartbeatTimeout(workerDO.getWorkerSeqNumber());
    }

    public HealthLevel getHealthLevel(DmWorkerDO workerDO) {
        WorkerState state = workerDO.getWorkerState();
        if (state == WorkerState.ONLINE) {
            boolean connected = isWorkerConnected(workerDO.getWorkerSeqNumber());
            boolean hbTimeout = isHeartbeatTimeout(workerDO.getWorkerSeqNumber());
            if (connected && !hbTimeout) {
                return HealthLevel.Health;
            } else if (connected || !hbTimeout) {
                return HealthLevel.SubHealth;
            }
        } else if (state == WorkerState.WAIT_TO_ONLINE) {
            return HealthLevel.SubHealth;
        }

        return HealthLevel.Unhealthy;
    }

    protected boolean isWorkerConnected(String wsn) {
        DmWorkerStatusDO workerStatusDO = sidecarStatusMapper.queryByWsn(wsn);
        if (workerStatusDO == null) {
            return false;
        }

        return (workerStatusDO.getWorkerConnStatus() == WorkerConnStatus.CONNECTED);
    }

    protected boolean isHeartbeatTimeout(String wsn) {
        DmWorkerHeartbeatDO heartbeatDO = heartbeatMapper.queryHeartbeatByWsn(wsn);
        if (heartbeatDO == null) {
            return false;
        }

        Duration duration = fromLastUpdataToNow(heartbeatDO);
        return duration.getSeconds() > (15 * HEARTBEAT_TIMEOUT_MAGNIFICATION);
    }

    protected Duration fromLastUpdataToNow(DmWorkerHeartbeatDO heartbeatDO) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastUpdated = LocalDateTime.ofInstant(heartbeatDO.getWorkerSendTime().toInstant(), ZoneId.systemDefault());

        return Duration.between(lastUpdated, now);
    }
}
