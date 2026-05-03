package com.clougence.clouddm.api.sidecar.autoexec;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;

@RSocketApiClass
public interface AutoExecRService {

    void dispatchJob(RSocketSendDTO dto, Long jobId);

    void pauseJob(RSocketSendDTO dto, Long jobId);
}
