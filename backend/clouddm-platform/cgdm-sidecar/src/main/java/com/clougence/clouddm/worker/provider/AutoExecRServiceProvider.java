/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package com.clougence.clouddm.worker.provider;

import jakarta.annotation.Resource;

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
