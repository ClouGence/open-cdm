package com.clougence.clouddm.api.common.rpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoData {

    protected String               requestId;

    protected SoDataDirectionType  rSocketDirectionType;
    /**
     * Sidecar need to know taskId to send back rsocket request
     */
    protected long                 taskId;

    protected SoDataWorkerIdentity workerIdentity;
    /**
     * If is task->sidecar->console, we need to know this case. Although direction type is RSocketDirectionType. CONSOLE->SIDECAR we need send back to task
     */
    private boolean                sendBackToTask;

    public SoDataWorkerIdentity getWorkerIdentity() { return workerIdentity; }

}
