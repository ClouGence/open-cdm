package com.clougence.clouddm.api.console.status;

import java.util.Date;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;

/**
 * @author bucketli 2021/1/16 11:44
 */
@RSocketApiClass
public interface StatusRService {

    WorkerState fetchStatusAndHeartbeat(WorkerIdentity identity, Date heartbeat);

    void reportStatus(WorkerIdentity identity, Date sendTime, WorkerState workerState);

    void reportAddress(WorkerIdentity identity, Date sendTime, String localIp, String externalIp);

    void reportMetric(WorkerIdentity identity, Date sendTime, MetricStats stats);
}
