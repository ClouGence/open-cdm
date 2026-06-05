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
 */
package com.clougence.clouddm.api.sidecar.autoexec;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoExecMessageDTO {

    private AutoExecMessageType type;
    private Date                time       = new Date();

    private Long                jobId;
    private Long                taskId;

    // task
    private long                affectLine = 0;
    private int                 execCount;

    // job
    private String              queryId;

    private String              message;

    public static AutoExecMessageDTO taskStartMessage(Long taskId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.TASK_START);
        dto.setTaskId(taskId);
        return dto;
    }

    public static AutoExecMessageDTO taskFailMessage(Long taskId, String message, int execCount) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.TASK_FAILED);
        dto.setTaskId(taskId);
        dto.setMessage(message);
        dto.setExecCount(execCount);
        return dto;
    }

    public static AutoExecMessageDTO taskFinishMessage(Long taskId, Long affectLine, int execCount) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.TASK_FINISH);
        dto.setTaskId(taskId);
        dto.setAffectLine(affectLine);
        dto.setExecCount(execCount);
        return dto;
    }

    public static AutoExecMessageDTO taskWaitConfirmMessage(Long taskId, Long affectLine, int execCount) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.TASK_WAIT_CONFIRM);
        dto.setAffectLine(affectLine);
        dto.setTaskId(taskId);
        dto.setExecCount(execCount);
        return dto;
    }

    public static AutoExecMessageDTO transactionFinishMessage(Long jobId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.TRANSACTION_FINISH);
        dto.setJobId(jobId);
        return dto;
    }

    public static AutoExecMessageDTO transactionRollbackMessage(Long jobId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.TRANSACTION_ROLLBACK);
        dto.setJobId(jobId);
        return dto;
    }

    public static AutoExecMessageDTO taskSkipMessage(Long jobId, Long taskId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.TASK_SKIP);
        dto.setJobId(jobId);
        dto.setTaskId(taskId);
        return dto;
    }

    public static AutoExecMessageDTO jobFinishMessage(Long jobId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.JOB_FINISH);
        dto.setJobId(jobId);
        return dto;
    }

    public static AutoExecMessageDTO jobPauseMessage(Long jobId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.JOB_PAUSE);
        dto.setJobId(jobId);
        return dto;
    }

    public static AutoExecMessageDTO jobFailedMessage(Long jobId, Long taskId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.JOB_FAILED);
        dto.setTaskId(taskId);
        dto.setJobId(jobId);
        return dto;
    }

    public static AutoExecMessageDTO createSessionFailed(Long jobId, String message) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.CREATE_SESSION_FAILED);
        dto.setMessage(message);
        dto.setJobId(jobId);
        return dto;
    }

    public static AutoExecMessageDTO createQueryIdMessage(Long jobId, String queryId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.QUERY_ID);
        dto.setQueryId(queryId);
        dto.setJobId(jobId);
        return dto;
    }

    public static AutoExecMessageDTO taskRetryMessage(Long taskId) {
        AutoExecMessageDTO dto = new AutoExecMessageDTO();
        dto.setType(AutoExecMessageType.TASK_RETRY);

        dto.setTaskId(taskId);
        return dto;
    }
}
