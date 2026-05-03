package com.clougence.clouddm.console.web.provider;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.status.MetricStats;
import com.clougence.clouddm.api.console.status.StatusRService;
import com.clougence.clouddm.api.console.status.WorkerState;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.console.web.component.auth.model.WorkerCacheEntry;
import com.clougence.clouddm.console.web.dal.enumeration.WorkerHeartbeatType;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerHeartbeatDO;
import com.clougence.clouddm.console.web.service.cluster.WorkerService;
import com.clougence.rdp.dal.enumeration.LifeCycleState;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/16 11:54
 */
@Slf4j
@Service
@RSocketApiClass
public class StatusRServiceProvider extends AbstractBasicProvider implements StatusRService {

    @Resource
    private WorkerService  workerService;
    @Resource
    private RdpUserService rdpUserService;

    @Override
    public WorkerState fetchStatusAndHeartbeat(WorkerIdentity identity, Date heartbeat) {
        if (!this.checkAccessKey(identity) || heartbeat == null) {
            return WorkerState.ABNORMAL;
        }

        DmWorkerDO workerDO = this.workerService.getWorkerByWsn(identity.getWorkerSeqNumber());
        if (workerDO == null) {
            return WorkerState.NOT_EXIST;
        } else {
            return checkAndMaintainHb(workerDO, heartbeat, identity, WorkerHeartbeatType.fetchAndHeartbeat);
        }
    }

    @Override
    public void reportStatus(WorkerIdentity identity, Date sendTime, WorkerState workerState) {
        if (!this.checkAccessKey(identity) || workerState == null) {
            return;
        }

        DmWorkerDO workerDO = this.workerService.getWorkerByWsn(identity.getWorkerSeqNumber());
        if (workerDO == null) {
            return;
        }

        WorkerState targetState = workerState;
        WorkerState currentState = workerDO.getWorkerState();

        if (this.isUpdateToOnline(targetState, currentState) || this.isUpdateToOffline(targetState, currentState)) {
            this.workerService.updateStatus(workerDO.getId(), targetState);

            if (workerDO.getLifeCycleState() != LifeCycleState.CREATED) {
                this.workerService.updateLifecycleState(workerDO.getId(), LifeCycleState.CREATED);
            }
        }

        this.checkAndMaintainHb(workerDO, sendTime, identity, WorkerHeartbeatType.updateWorkerIps);
    }

    private boolean isUpdateToOnline(WorkerState targetState, WorkerState currentState) {
        return targetState == WorkerState.ONLINE && (currentState == WorkerState.WAIT_TO_ONLINE || currentState == WorkerState.ABNORMAL);
    }

    private boolean isUpdateToOffline(WorkerState targetState, WorkerState currentState) {
        return targetState == WorkerState.OFFLINE && currentState == WorkerState.WAIT_TO_OFFLINE;
    }

    private WorkerState checkAndMaintainHb(DmWorkerDO workerDO, Date sendDate, WorkerIdentity identity, WorkerHeartbeatType heartbeatType) {
        RdpUserDO userDO = this.rdpUserService.getUserByAk(identity.getAccessKey());
        if (!workerDO.getUid().equals(userDO.getUid())) {
            log.error("worker (" + identity.getWorkerSeqNumber() + ") not belone user (" + identity.getAccessKey() + ")");
            return WorkerState.NOT_EXIST;
        }

        log.debug("receive worker request,date:" + sendDate);
        DmWorkerHeartbeatDO heartbeatDO = new DmWorkerHeartbeatDO();
        heartbeatDO.setWorkerIp(identity.getLocalIp());
        heartbeatDO.setWorkerSendTime(sendDate);
        heartbeatDO.setWorkerSeqNumber(identity.getWorkerSeqNumber());
        heartbeatDO.setHeartbeatType(heartbeatType);
        this.workerService.upsertWorkerHeartbeat(heartbeatDO);
        return workerDO.getWorkerState();
    }

    @Override
    public void reportAddress(WorkerIdentity identity, Date sendTime, String localIp, String externalIp) {
        if (!this.checkAccessKey(identity) || (StringUtils.isBlank(localIp) && StringUtils.isBlank(externalIp))) {
            return;
        }

        WorkerCacheEntry workerDO = this.ownerCacheService.queryByWsn(identity.getWorkerSeqNumber());
        if (workerDO == null) {
            return;
        }

        this.workerService.updateWorkerIp(workerDO.getWorkerNumId(), localIp, externalIp);
    }

    @Override
    public void reportMetric(WorkerIdentity identity, Date sendTime, MetricStats stats) {
        if (!this.checkAccessKey(identity) || stats == null) {
            return;
        }

        WorkerCacheEntry workerDO = this.ownerCacheService.queryByWsn(identity.getWorkerSeqNumber());
        if (workerDO == null) {
            return;
        }

        log.debug("receive worker metric request,date:" + sendTime);
        this.workerService.updateWorkerMetric(workerDO.getWorkerNumId(), stats);
    }
}
