package com.clougence.clouddm.api.console.autoexec;

import java.util.List;

import com.clougence.clouddm.api.sidecar.autoexec.AutoExecJobDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;

@RSocketApiClass
public interface ExecJobRService {

    AutoExecJobDTO fetchJobInfo(WorkerIdentity identity, Long jobId);

    void reportExecMessage(WorkerIdentity identity, List<AutoExecMessageDTO> messages);

    void reportActiveJobs(WorkerIdentity identity, List<Long> jobIdList);

}
