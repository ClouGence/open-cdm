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
package com.clougence.clouddm.worker.component.autoexec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.console.autoexec.AutoExecTaskPackageInfo;
import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.api.console.autoexec.ExecJobRService;
import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecJobDTO;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.api.sidecar.session.execute.AsyncWaitResult;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultCount;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultMessage;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.worker.component.report.ReportUtils;
import com.clougence.clouddm.worker.component.resource.TaskDsResourceManager;
import com.clougence.clouddm.worker.component.session.SessionAgent;
import com.clougence.clouddm.worker.component.session.SessionManager;
import com.clougence.clouddm.worker.util.ZipUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.ThreadUtils;
import com.clougence.utils.io.FileUtils;

import jakarta.annotation.Resource;

@Service
@Scope("prototype")
public class AutoExecJob implements Runnable {
    private static final int         PACKAGE_READ_BLOCK_SIZE = 1024 * 1024;
    private static final Logger      log                     = LoggerFactory.getLogger("sql-audit");

    @Resource
    private TaskDsResourceManager    backgroundRM;
    @Resource
    private SessionManager           sessionManager;
    @Resource
    private ConfigRService           configRService;
    @Resource
    private ExecJobRService          execJobRService;
    private AutoExecJobDTO           job;
    private SessionAgent             sessionAgent;
    private List<AutoExecMessageDTO> messageList             = new LinkedList<>();
    private String                   runningQueryId;
    private WorkerIdentity           workerIdentity;
    private final AtomicBoolean      pauseRequested          = new AtomicBoolean(false);

    public void init(AutoExecJobDTO job) {
        this.job = job;
    }

    public void run() {
        Path taskDirectory = null;
        Path taskPackageFile = null;
        boolean completed = false;
        try {
            // dog
            if (this.pauseRequested.get()) {
                sendMessage(AutoExecMessageDTO.jobPauseMessage(this.job.getJobId()), true);
                log.info("job paused");
                return;
            }

            // pull package
            try {
                AutoExecTaskPackageInfo taskPackage = this.job.getTaskPackage();
                if (taskPackage == null) {
                    throw new IllegalStateException("Auto execution task package metadata is missing.");
                }

                Path execDirectory = Paths.get(GlobalConfUtils.getTempDataHome(), "exec");
                Files.createDirectories(execDirectory);
                taskPackageFile = execDirectory.resolve(this.job.getJobId() + ".tasks.zip");
                prepareTaskPackage(taskPackageFile, taskPackage);
                taskDirectory = extractTaskPackage(taskPackageFile);
            } catch (Throwable e) {
                if (this.pauseRequested.get()) {
                    sendMessage(AutoExecMessageDTO.jobPauseMessage(job.getJobId()), true);
                    log.info("job paused while pulling tasks");
                } else {
                    sendMessage(AutoExecMessageDTO.jobPrepareFailed(job.getJobId(), e.getMessage()), true);
                    log.error("prepare auto execution task file failed, jobId: " + job.getJobId(), e);
                }
                return;
            }
            if (this.pauseRequested.get()) {
                sendMessage(AutoExecMessageDTO.jobPauseMessage(job.getJobId()), true);
                log.info("job paused after pulling tasks");
                return;
            }

            // create session
            try {
                DataSourceConfig dsConfig = this.configRService.fetchDsConfig(this.job.getDsId());
                this.sessionAgent = this.sessionManager.createSession(backgroundRM, dsConfig, this.job.getContextDTO());
                String currentQueryId = this.sessionAgent.getCurrentQueryId();
                sendMessage(AutoExecMessageDTO.createQueryIdMessage(this.job.getJobId(), currentQueryId), true);
                log.info("create session success,query id: " + currentQueryId);
            } catch (Throwable e) {
                sendMessage(AutoExecMessageDTO.createSessionFailed(this.job.getJobId(), e.getMessage()), true);
                log.error("create session failed", e);
                return;
            }

            // exec job
            try {
                log.info("job start");
                JobResult result = jobWrap(taskDirectory);
                switch (result) {
                    case SUCCESS:
                        log.info("job success");
                        sendMessage(AutoExecMessageDTO.jobFinishMessage(this.job.getJobId(), this.job.getTaskPackage().getAttachmentId()), true);
                        completed = true;
                        break;
                    case FAILED:
                        log.error("job failed");
                        sendMessage(AutoExecMessageDTO.jobFailedMessage(this.job.getJobId(), this.runningQueryId), true);
                        break;
                    case PAUSED:
                        log.warn("job paused");
                        sendMessage(AutoExecMessageDTO.jobPauseMessage(this.job.getJobId()), true);
                        break;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } finally {
            if (this.sessionAgent != null) {
                try {
                    this.sessionAgent.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            this.deleteTaskPath(taskDirectory);
            if (completed) {
                this.deleteTaskPath(taskPackageFile);
            }
        }
    }

    //
    // pull package
    //

    private void prepareTaskPackage(Path packageFile, AutoExecTaskPackageInfo expected) throws Exception {
        if (Files.isRegularFile(packageFile)) {
            try {
                if (Files.size(packageFile) == expected.getFileSize() && expected.getMd5().equals(fileMd5(packageFile))) {
                    log.info("reuse local auto execution task package, jobId: {}, md5: {}", this.job.getJobId(), expected.getMd5());
                    return;
                }
            } catch (Exception e) {
                log.warn("check local auto execution task package failed, download again, jobId: {}", this.job.getJobId(), e);
            }
            Files.deleteIfExists(packageFile);
        }

        Path downloading = packageFile.resolveSibling(packageFile.getFileName() + ".downloading");
        Files.deleteIfExists(downloading);

        Exception lastError = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                long offset = 0;
                try (OutputStream output = Files.newOutputStream(downloading, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                    while (offset < expected.getFileSize()) {
                        if (this.pauseRequested.get()) {
                            throw new IllegalStateException("Auto execution job stopped while downloading task package.");
                        }
                        int length = (int) Math.min(PACKAGE_READ_BLOCK_SIZE, expected.getFileSize() - offset);
                        byte[] block = this.execJobRService.readPackage(identity(), this.job.getJobId(), expected.getAttachmentId(), offset, length);
                        if (block.length == 0 || block.length > length) {
                            throw new IllegalStateException("Invalid auto execution task package block at offset: " + offset);
                        }
                        output.write(block);
                        offset += block.length;
                    }
                }

                if (Files.size(downloading) == expected.getFileSize() && expected.getMd5().equals(fileMd5(downloading))) {
                    Files.move(downloading, packageFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                    return;
                }
                log.warn("auto execution task package MD5 mismatch, jobId: {}, attempt: {}", this.job.getJobId(), attempt);
            } catch (Exception e) {
                lastError = e;
                if (this.pauseRequested.get()) {
                    throw e;
                }
                log.warn("download auto execution task package failed, jobId: {}, attempt: {}", this.job.getJobId(), attempt, e);
            } finally {
                Files.deleteIfExists(downloading);
            }
        }

        throw new IllegalStateException("Download auto execution task package failed after retries, jobId: " + this.job.getJobId(), lastError);
    }

    private Path extractTaskPackage(Path packageFile) throws Exception {
        Path execDirectory = Paths.get(GlobalConfUtils.getTempDataHome(), "exec");
        Files.createDirectories(execDirectory);
        Path taskDirectory = execDirectory.resolve(this.job.getJobId().toString());
        deleteTaskPath(taskDirectory);
        try {
            ZipUtils.extract(packageFile, taskDirectory);
        } catch (IOException e) {
            deleteTaskPath(taskDirectory);
            throw e;
        }
        return taskDirectory;
    }

    private String fileMd5(Path file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[64 * 1024];
        try (InputStream input = Files.newInputStream(file)) {
            int read;
            while ((read = input.read(buffer)) >= 0) {
                if (read > 0) {
                    digest.update(buffer, 0, read);
                }
            }
        }
        return HexFormat.of().formatHex(digest.digest());
    }

    //
    // run job
    //

    private JobResult jobWrap(Path taskDirectory) {
        boolean transaction = job.isEnableTransactional();
        try {
            if (transaction) {
                log.info("transaction start");
                sessionAgent.setAutoCommit(false);
            }

            if (!jobRun(taskDirectory)) {
                if (transaction) {
                    sessionAgent.rollback();
                    log.warn("transaction rollback");
                    sendMessage(AutoExecMessageDTO.transactionRollbackMessage(job.getJobId()), false);
                }
                return JobResult.PAUSED;
            }

            if (pauseRequested.get()) {
                if (transaction) {
                    sessionAgent.rollback();
                    log.warn("transaction rollback");
                    sendMessage(AutoExecMessageDTO.transactionRollbackMessage(job.getJobId()), false);
                }
                return JobResult.PAUSED;
            }

            if (transaction) {
                sessionAgent.commit();
                log.info("transaction group commit");
                sendMessage(AutoExecMessageDTO.transactionFinishMessage(job.getJobId()), false);
            }
            return JobResult.SUCCESS;
        } catch (Throwable e) {
            if (transaction) {
                sessionAgent.rollback();
                log.warn("transaction rollback");
                sendMessage(AutoExecMessageDTO.transactionRollbackMessage(job.getJobId()), false);
            }
            if (pauseRequested.get()) {
                return JobResult.PAUSED;
            }
            log.error("auto execution job failed, jobId: " + job.getJobId(), e);
            return JobResult.FAILED;
        } finally {
            sessionAgent.setAutoCommit(true);
        }
    }

    private boolean jobRun(Path taskDirectory) throws IOException {
        try (Stream<Path> taskFiles = Files.list(taskDirectory)) {
            Iterator<Path> iterator = taskFiles.filter(Files::isRegularFile).sorted(Comparator.comparing(file -> file.getFileName().toString())).iterator();
            while (iterator.hasNext()) {
                try (BufferedReader reader = Files.newBufferedReader(iterator.next(), StandardCharsets.UTF_8)) {
                    String taskJson;
                    while ((taskJson = reader.readLine()) != null) {
                        QueryRequest request = JsonUtils.toObj(taskJson, QueryRequest.class);
                        if (!jobRunItem(request)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean jobRunItem(QueryRequest request) {
        String queryId = request.getQueryId();
        int retryCount = 0;
        while (true) {
            if (pauseRequested.get()) {
                return false;
            }

            this.runningQueryId = queryId;
            log.info("sql start exec, query id: {}", queryId);
            sendMessage(AutoExecMessageDTO.taskStartMessage(queryId), true);
            try {
                AsyncWaitResult submitted = sessionAgent.submitQueries("autoexec-" + queryId + "-" + retryCount, Collections.singletonList(request));
                if (!submitted.isSuccess()) {
                    throw new IllegalStateException(submitted.getMessage());
                }

                long affectLine = 0;
                String errorMessage = null;
                do {
                    for (Result result : sessionAgent.popList()) {
                        if (result instanceof ResultCount) {
                            affectLine += Math.max(0, ((ResultCount) result).getUpdateCount());
                        } else if (result instanceof ResultMessage && ((ResultMessage) result).getLevel() == MessageLevel.Error) {
                            errorMessage = result.getMessage();
                        }
                    }
                    if (pauseRequested.get() && sessionAgent.isExecuting()) {
                        sessionAgent.cancel();
                    }
                    if (sessionAgent.isExecuting()) {
                        ThreadUtils.safeSleep(20);
                    }
                } while (sessionAgent.isExecuting() || sessionAgent.hasMore());

                if (pauseRequested.get()) {
                    return false;
                }
                if (errorMessage != null) {
                    throw new IllegalStateException(errorMessage);
                }
                log.info("sql exec success,affect line: {}", affectLine);
                if (job.isEnableTransactional()) {
                    sendMessage(AutoExecMessageDTO.taskWaitConfirmMessage(queryId, affectLine, retryCount + 1), false);
                } else {
                    sendMessage(AutoExecMessageDTO.taskFinishMessage(queryId, affectLine, retryCount + 1), false);
                }
                return true;
            } catch (Throwable e) {
                if (pauseRequested.get()) {
                    return false;
                }
                if (job.getErrorStrategy() == ErrorStrategy.RETRY && retryCount < job.getRetryCount()) {
                    log.warn("sql exec failed,wait next retry,retry count :{},error msg:{}", retryCount + 1, e.getMessage());
                    sendMessage(AutoExecMessageDTO.taskRetryMessage(queryId), true);
                    ThreadUtils.safeSleep(job.getRetryWaitTime() * 1000);
                    retryCount++;
                    continue;
                }
                if (job.getErrorStrategy() == ErrorStrategy.SKIP) {
                    log.info("sql skipped, query id: {}", queryId);
                    sendMessage(AutoExecMessageDTO.taskSkipMessage(job.getJobId(), this.runningQueryId), false);
                    return true;
                }
                log.error("sql exec failed, query id: {}, error msg:{}", queryId, e.getMessage());
                sendMessage(AutoExecMessageDTO.taskFailMessage(queryId, e.getMessage(), retryCount + 1), false);
                throw e;
            }
        }
    }

    //
    // life and utils
    //

    private void deleteTaskPath(Path taskPath) {
        if (taskPath == null || !Files.exists(taskPath)) {
            return;
        }
        try {
            if (Files.isDirectory(taskPath)) {
                FileUtils.deleteDirectory(taskPath.toFile());
            } else {
                Files.deleteIfExists(taskPath);
            }
        } catch (IOException e) {
            log.warn("delete auto execution task path failed: {}", taskPath, e);
        }
    }

    public void pause() throws Exception {
        if (!this.pauseRequested.compareAndSet(false, true)) {
            return;
        }
        log.warn("job start pause");
        if (this.sessionAgent != null && this.sessionAgent.isExecuting()) {
            sessionAgent.cancel();
        }
    }

    private WorkerIdentity identity() throws Exception {
        if (this.workerIdentity == null) {
            this.workerIdentity = ReportUtils.getIdentity();
        }
        return this.workerIdentity;
    }

    private void sendMessage(AutoExecMessageDTO message, boolean immediately) {
        this.messageList.add(message);
        if (immediately) {
            while (true) {
                try {
                    this.execJobRService.reportMessage(identity(), this.messageList);
                    this.messageList = new LinkedList<>();
                    return;
                } catch (Exception e) {
                    log.error("reportExecMessage error", e);
                    // wait next
                    ThreadUtils.sleep(5000);
                }
            }
        }
    }
}
