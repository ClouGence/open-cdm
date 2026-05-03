package com.clougence.clouddm.worker.provider;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.autoexec.AutoExecRService;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.worker.component.autoexec.AutoExecJobManager;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class AutoExecRServiceProvider implements AutoExecRService {

    @Resource
    private AutoExecJobManager autoExecJobManager;

    @Override
    public void dispatchJob(RSocketSendDTO dto, Long jobId) {
        this.autoExecJobManager.submit(jobId);
    }

    public void pauseJob(RSocketSendDTO dto, Long jobId) {
        try {
            this.autoExecJobManager.pauseJob(jobId);
        } catch (Exception e) {
            String message = "Failed to pause job, jobId = " + jobId + ", msg: = " + ExceptionUtils.getRootCauseMessage(e);
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
