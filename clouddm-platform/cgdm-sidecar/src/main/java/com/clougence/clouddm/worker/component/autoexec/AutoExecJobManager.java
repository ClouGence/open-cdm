package com.clougence.clouddm.worker.component.autoexec;

public interface AutoExecJobManager {

    void submit(Long jobId);

    void pauseJob(Long jobId) throws Exception;
}
