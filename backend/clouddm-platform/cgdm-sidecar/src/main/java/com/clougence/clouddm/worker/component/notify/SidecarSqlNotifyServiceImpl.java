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
package com.clougence.clouddm.worker.component.notify;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.console.sqlaudit.SqlAuditRService;
import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.console.sqlaudit.Type;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.sdk.execute.resultset.echo.*;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.worker.component.report.ReportUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SidecarSqlNotifyServiceImpl implements SidecarSqlNotifyService, UnifiedPostConstruct {

    private static final int       REPORT_BATCH_SIZE = 50;

    private Thread                 thread;
    private final Object           cacheLock         = new Object();
    private FileChannel            cacheChannel;
    private Path                   offsetFile;
    private long                   confirmedOffset;
    private List<SqlExecNotifyDTO> pendingReport     = Collections.emptyList();
    private long                   pendingReportEndOffset;
    private final AtomicBoolean    running           = new AtomicBoolean();

    @Resource
    private SqlAuditRService       auditRService;
    private WorkerIdentity         workerIdentity;

    private WorkerIdentity identity() throws Exception {
        if (this.workerIdentity == null) {
            this.workerIdentity = ReportUtils.getIdentity();
        }
        return this.workerIdentity;
    }

    @Override
    public void confirmSession(String sessionId) {
        SqlExecNotifyDTO sqlExecNotifyDTO = new SqlExecNotifyDTO();
        sqlExecNotifyDTO.setSessionId(sessionId);
        sqlExecNotifyDTO.setType(Type.COMMIT);
        sqlExecNotifyDTO.setTime(new Date());
        this.cache(sqlExecNotifyDTO);
    }

    @Override
    public void rollbackSession(String sessionId) {
        SqlExecNotifyDTO sqlExecNotifyDTO = new SqlExecNotifyDTO();
        sqlExecNotifyDTO.setSessionId(sessionId);
        sqlExecNotifyDTO.setType(Type.ROLLBACK);
        sqlExecNotifyDTO.setTime(new Date());
        this.cache(sqlExecNotifyDTO);
    }

    @Override
    public void startTransaction(String sessionId) {
        SqlExecNotifyDTO sqlExecNotifyDTO = new SqlExecNotifyDTO();
        sqlExecNotifyDTO.setSessionId(sessionId);
        sqlExecNotifyDTO.setType(Type.START_TRANSACTION);
        sqlExecNotifyDTO.setTime(new Date());
        this.cache(sqlExecNotifyDTO);
    }

    @Override
    public void finishForQuery(QueryRequest query, Result result, boolean waitConfirm) {
        SqlStatus successStatus = waitConfirm ? SqlStatus.WAIT_CONFIRM : SqlStatus.SUCCESS;
        switch (result.getResultType()) {
            case Phase: {
                if (result instanceof ResultPhase) {
                    if (((ResultPhase) result).getPhaseType() == ResultPhaseType.After) {
                        this.cacheQueryResult(query, result, successStatus, 0);
                    }
                }
                break;
            }
            // fail
            case Message: {
                ResultMessage resultMessage = (ResultMessage) result;
                if (!resultMessage.isNotify()) {
                    return;
                }
                if (resultMessage.getLevel() == MessageLevel.Error) {
                    cacheQueryResult(query, result, SqlStatus.FAILURE, 0);
                } else if (resultMessage.getLevel() == MessageLevel.Info) {
                    cacheQueryResult(query, result, successStatus, 0);
                }
                break;
            }
            case ResultCount: {
                ResultCount resultCount = (ResultCount) result;
                // create table .... count = -1
                cacheQueryResult(query, result, successStatus, Math.max(0, resultCount.getUpdateCount()));
                break;
            }
        }
    }

    private void cacheQueryResult(QueryRequest query, Result result, SqlStatus sqlStatus, long affectLine) {
        SqlExecNotifyDTO dto = new SqlExecNotifyDTO();
        dto.setSessionId(result.getSessionId());
        dto.setQueryId(query.getQueryId());
        dto.setMessage(result.getMessage());
        dto.setStatus(sqlStatus);
        dto.setAffectLine(affectLine);
        dto.setTime(new Date());
        dto.setType(Type.SQL_END);
        this.cache(dto);
    }

    @Override
    public void beginForQuery(QueryRequest query, String sessionId) {
        SqlExecNotifyDTO dto = new SqlExecNotifyDTO();
        dto.setTime(new Date());
        dto.setSessionId(sessionId);
        dto.setQueryId(query.getQueryId());
        dto.setClientIp(HostUtil.getHostIp());
        dto.setType(Type.SQL_START);
        dto.setStatus(SqlStatus.RUNNING);
        this.cache(dto);
    }

    @Override
    public void init() throws Exception {
        if (this.running.compareAndSet(false, true)) {
            Path cacheDirectory = Paths.get(GlobalConfUtils.getAppDataHome(), "sql-audit");
            Files.createDirectories(cacheDirectory);
            Path cacheFile = cacheDirectory.resolve("notify.cache");
            this.offsetFile = cacheDirectory.resolve("notify.offset");
            this.cacheChannel = FileChannel.open(cacheFile, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
            this.recoverCache();
            this.thread = ThreadUtils.daemonThread(this::loopSchedule);
            this.thread.setName("Sql Notify Thread");
            this.thread.start();
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            if (this.thread != null) {
                this.thread.interrupt();
            }
        }
    }

    protected void loopSchedule() {
        try {
            while (true) {
                try {
                    doReport();
                    if (!this.running.get()) {
                        log.warn("[SQL RECODE TASK] thread exit, (" + Thread.currentThread().getName() + ")");
                        return;
                    }
                    ThreadUtils.sleep(1000);
                } catch (Throwable e) {
                    log.error("[Sql RECODE TASK] error " + e.getMessage(), e);
                    ThreadUtils.sleep(5000);
                }
            }
        } finally {
            synchronized (this.cacheLock) {
                if (this.cacheChannel != null) {
                    try {
                        this.cacheChannel.force(false);
                        this.cacheChannel.close();
                    } catch (IOException e) {
                        log.warn("close SQL audit cache failed", e);
                    }
                }
            }
        }
    }

    private void doReport() throws Exception {
        while (true) {
            if (this.pendingReport.isEmpty()) {
                this.loadPendingReport();
                if (this.pendingReport.isEmpty()) {
                    return;
                }
            }
            this.auditRService.reportSqlAudit(identity(), new Date(), this.pendingReport);
            this.confirmPendingReport();
        }
    }

    private void cache(SqlExecNotifyDTO dto) {
        byte[] data = JsonUtils.toJson(dto).getBytes(StandardCharsets.UTF_8);
        synchronized (this.cacheLock) {
            try {
                this.cacheChannel.position(this.cacheChannel.size());
                ByteBuffer length = ByteBuffer.allocate(Integer.BYTES);
                length.putInt(data.length).flip();
                while (length.hasRemaining()) {
                    this.cacheChannel.write(length);
                }
                ByteBuffer content = ByteBuffer.wrap(data);
                while (content.hasRemaining()) {
                    this.cacheChannel.write(content);
                }
                this.cacheChannel.force(false);
            } catch (IOException e) {
                throw new IllegalStateException("cache SQL audit event failed", e);
            }
        }
    }

    private void loadPendingReport() throws IOException {
        synchronized (this.cacheLock) {
            long position = this.confirmedOffset;
            long fileSize = this.cacheChannel.size();
            List<SqlExecNotifyDTO> batch = new LinkedList<>();
            while (batch.size() < REPORT_BATCH_SIZE && position + Integer.BYTES <= fileSize) {
                ByteBuffer length = ByteBuffer.allocate(Integer.BYTES);
                if (!readFully(this.cacheChannel, length, position)) {
                    break;
                }
                length.flip();
                int dataLength = length.getInt();
                long nextPosition = position + Integer.BYTES + dataLength;
                if (dataLength < 0 || nextPosition > fileSize) {
                    break;
                }
                ByteBuffer content = ByteBuffer.allocate(dataLength);
                if (!readFully(this.cacheChannel, content, position + Integer.BYTES)) {
                    break;
                }
                batch.add(JsonUtils.toObj(new String(content.array(), StandardCharsets.UTF_8), SqlExecNotifyDTO.class));
                position = nextPosition;
            }
            this.pendingReport = batch;
            this.pendingReportEndOffset = position;
        }
    }

    private void confirmPendingReport() throws IOException {
        synchronized (this.cacheLock) {
            this.confirmedOffset = this.pendingReportEndOffset;
            this.writeConfirmedOffset();
            this.pendingReport = Collections.emptyList();
            this.pendingReportEndOffset = 0;

            if (this.confirmedOffset == this.cacheChannel.size()) {
                this.cacheChannel.truncate(0);
                this.cacheChannel.position(0);
                this.confirmedOffset = 0;
                this.writeConfirmedOffset();
            }
        }
    }

    private void recoverCache() throws IOException {
        long fileSize = this.cacheChannel.size();
        long validLength = 0;
        while (validLength + Integer.BYTES <= fileSize) {
            ByteBuffer length = ByteBuffer.allocate(Integer.BYTES);
            if (!readFully(this.cacheChannel, length, validLength)) {
                break;
            }
            length.flip();
            int dataLength = length.getInt();
            long nextPosition = validLength + Integer.BYTES + dataLength;
            if (dataLength < 0 || nextPosition > fileSize) {
                break;
            }
            validLength = nextPosition;
        }
        if (validLength != fileSize) {
            this.cacheChannel.truncate(validLength);
            fileSize = validLength;
        }

        this.confirmedOffset = 0;
        if (Files.isRegularFile(this.offsetFile)) {
            try {
                this.confirmedOffset = Long.parseLong(Files.readString(this.offsetFile, StandardCharsets.UTF_8).trim());
            } catch (Exception e) {
                log.warn("read SQL audit cache offset failed, report from beginning", e);
            }
        }
        if (this.confirmedOffset < 0 || this.confirmedOffset > fileSize) {
            this.confirmedOffset = 0;
        }
        this.writeConfirmedOffset();
    }

    private void writeConfirmedOffset() throws IOException {
        Path writingFile = this.offsetFile.resolveSibling(this.offsetFile.getFileName() + ".tmp");
        byte[] offsetBytes = Long.toString(this.confirmedOffset).getBytes(StandardCharsets.UTF_8);
        try (FileChannel channel = FileChannel.open(writingFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            ByteBuffer content = ByteBuffer.wrap(offsetBytes);
            while (content.hasRemaining()) {
                channel.write(content);
            }
            channel.force(false);
        }
        try {
            Files.move(writingFile, this.offsetFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(writingFile, this.offsetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static boolean readFully(FileChannel channel, ByteBuffer buffer, long position) throws IOException {
        while (buffer.hasRemaining()) {
            int read = channel.read(buffer, position);
            if (read < 0) {
                return false;
            }
            if (read == 0) {
                continue;
            }
            position += read;
        }
        return true;
    }
}
