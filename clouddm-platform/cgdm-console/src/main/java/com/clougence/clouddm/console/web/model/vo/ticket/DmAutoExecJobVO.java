package com.clougence.clouddm.console.web.model.vo.ticket;

import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.console.web.dal.enumeration.AutoExecJobStatus;
import com.clougence.clouddm.console.web.dal.enumeration.DmAutoExecType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmAutoExecJobVO {

    private Long              id;
    private AutoExecJobStatus status;
    private String            lastReportTime;
    private DmAutoExecType    execType;
    private String            execTime;

    // use for kill query
    private String            queryId;

    private String            workerSeqNumber;
    private String            workerIP;
    private WorkerConnStatus  workerStatus;

    private boolean           canRestart;
    private boolean           canRetry;
    private boolean           canPause;
    private boolean           canEnd;

    private boolean           normal = true;
    private String            message;
    private boolean           enableTransactional;
}
