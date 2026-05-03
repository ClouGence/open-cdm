package com.clougence.clouddm.worker.component.report;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.console.status.StatusRService;
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
    private StatusRService statusRService;
    private WorkerIdentity workerIdentity;

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
                log.info("receive state is " + state + ",switch to ONLINE status");

                this.statusRService.reportStatus(this.identity(), now, WorkerState.ONLINE);
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
