package com.clougence.clouddm.console.web.component.autoexec;

import com.clougence.rdp.dal.model.RdpUserDO;

public interface AutoExecManager {

    void dispatchJob(Long jobId);

    void stopJob(Long jobId, RdpUserDO user);
}
