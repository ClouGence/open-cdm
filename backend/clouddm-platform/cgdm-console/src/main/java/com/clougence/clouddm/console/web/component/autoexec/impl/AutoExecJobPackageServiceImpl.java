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
package com.clougence.clouddm.console.web.component.autoexec.impl;

import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.console.autoexec.AutoExecTaskPackageInfo;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisFeature;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisOptions;
import com.clougence.clouddm.console.web.component.analysis.QueryAnalysisService;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecJobPackageService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.file.LocalFileService;
import com.clougence.clouddm.console.web.service.security.AuditService;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonGenerator;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecJobPackageServiceImpl implements AutoExecJobPackageService {
    private static final int     TASK_FETCH_BATCH_SIZE = 100;
    @Resource
    private ExecutionDal         execDal;
    @Resource
    private DataSourceDal        dsDal;
    @Resource
    private DmDsConfigService    configService;
    @Resource
    private QueryAnalysisService analysisService;
    @Resource
    private AuditService         auditService;
    @Resource
    private LocalFileService     localFileService;

    @Override
    public AutoExecTaskPackageInfo create(long jobId) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryById(jobId);
        if (job.getStatus() != AutoExecJobStatus.PACKAGING) {
            throw new IllegalStateException("Auto execution job is not ready for packaging, jobId: " + jobId);
        }

        Requester requester;
        if (job.getDependOnBizType() == SQLJobBizType.TICKET) {
            requester = Requester.TICKET;
        } else if (job.getDependOnBizType() == SQLJobBizType.CHANGE) {
            requester = Requester.CHANGE;
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + job.getDependOnBizType());
        }

        DmDsDO dsDO = this.dsDal.dsMapper().queryDsIdentityById(job.getDataSourceId());
        DataSourceConfig dsConfig = this.configService.fetchDsConfigFromExists(dsDO.getId());
        List<String> levels = new ArrayList<>();
        levels.add(dsDO.getDsEnvId().toString());
        levels.add(dsDO.getId().toString());
        levels.addAll(job.getLevels());
        QueryAnalysisOptions options = QueryAnalysisOptions.builder()
            .currentUid(job.getUid())
            .dataSourceId(dsDO.getId())
            .levels(this.configService.parseLevels(levels).levelsParam())
            .skip(QueryAnalysisFeature.REWRITE)
            .build();

        QueryRequest template = DmDsUtils.createRequestCtx(dsConfig);
        template.setRequester(requester);
        DmDsUtils.fillRequestConfig(Collections.singletonList(template), dsDO.getId());
        Long requestDsId = template.getDsId();
        QueryResultConf requestResultConf = template.getResultConf();

        String packageFileName = jobId + ".tasks.zip";
        Path writingFile = Path.of(GlobalConfUtils.getTempDataHome(), "exec", packageFileName + ".tmp");
        try {
            Files.createDirectories(writingFile.getParent());
            Files.deleteIfExists(writingFile);
            Files.createFile(writingFile);

            int maxExecOrder = this.execDal.autoTaskMapper().queryNeedExecTaskMaxOrder(jobId);
            int fileNameWidth = Math.max(1, String.valueOf(maxExecOrder).length());
            MessageDigest digest = MessageDigest.getInstance("MD5");

            try (DigestOutputStream digestOutput = new DigestOutputStream(Files.newOutputStream(writingFile, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE),
                digest); ZipOutputStream zipOutput = new ZipOutputStream(digestOutput, StandardCharsets.UTF_8)) {
                zipOutput.setLevel(Deflater.BEST_SPEED);

                int afterExecOrder = 0;
                while (true) {
                    List<Long> taskIds = this.execDal.autoTaskMapper().queryNeedExecTaskIdsBatch(jobId, afterExecOrder, TASK_FETCH_BATCH_SIZE);
                    if (taskIds.isEmpty()) {
                        break;
                    }

                    List<DmExecAutoTaskDO> tasks = this.execDal.autoTaskMapper().queryNeedExecTasksByIds(jobId, taskIds);
                    if (tasks.size() != taskIds.size()) {
                        throw new IllegalStateException("Auto execution tasks changed while creating package.");
                    }

                    ZipEntry entry = new ZipEntry(String.format(Locale.ROOT, "%0" + fileNameWidth + "d", tasks.get(0).getExecOrder()));
                    entry.setTime(0L);
                    zipOutput.putNextEntry(entry);
                    try (JsonGenerator jsonOutput = JsonUtils.defaultObjectMapper().getFactory().createGenerator(zipOutput)) {
                        jsonOutput.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
                        jsonOutput.setRootValueSeparator(null);
                        for (DmExecAutoTaskDO task : tasks) {
                            try (StringReader reader = new StringReader(task.getExecSql());
                                    Stream<QueryRequest> analyzed = this.analysisService.analysisRequestsStream(dsConfig, reader, Collections.emptyList(), 1, 0, options)) {
                                Iterator<QueryRequest> iterator = analyzed.iterator();
                                if (!iterator.hasNext()) {
                                    throw new IllegalStateException("Auto execution task must contain exactly one SQL statement.");
                                }

                                QueryRequest source = iterator.next();
                                if (iterator.hasNext()) {
                                    throw new IllegalStateException("Auto execution task must contain exactly one SQL statement.");
                                }

                                QueryRequest request = DmDsUtils.createRequestCtx(dsConfig);
                                request.setQueryId(task.getQueryId());
                                request.setQueryBody(source.getQueryBody());
                                request.setQueryArgs(source.getQueryArgs());
                                request.setQueryTypes(source.getQueryTypes());
                                request.setDsId(requestDsId);
                                request.setDsType(source.getDsType());
                                request.setRelations(source.getRelations());
                                request.setColumnList(source.getColumnList());
                                request.setUsingValueProcess(source.isUsingValueProcess());
                                request.setRequester(requester);
                                request.setRequestTime(Timestamp.valueOf(task.getGmtCreate()));
                                request.setResultConf(requestResultConf.clone());
                                this.auditService.prepareAudit(dsDO.getId(), job.getUid(), request);
                                JsonUtils.defaultObjectMapper().writeValue(jsonOutput, request);
                                jsonOutput.writeRaw('\n');
                            }
                        }
                    }
                    zipOutput.closeEntry();
                    afterExecOrder = tasks.get(tasks.size() - 1).getExecOrder();
                    if (this.execDal.autoJobMapper().heartbeatPackaging(jobId) != 1) {
                        throw new IllegalStateException("Auto execution job stopped while creating task package.");
                    }
                }
            }

            String md5 = HexFormat.of().formatHex(digest.digest());
            long fileSize = Files.size(writingFile);
            long attachmentId = this.localFileService.addAsEditing(job.getUid(), writingFile, packageFileName, SysAttachmentType.SQL_FILE_TASK);
            AutoExecTaskPackageInfo info = new AutoExecTaskPackageInfo();
            info.setAttachmentId(attachmentId);
            info.setFileSize(fileSize);
            info.setMd5(md5);
            return info;
        } catch (Exception e) {
            try {
                Files.deleteIfExists(writingFile);
            } catch (Exception deleteError) {
                log.warn("delete incomplete auto execution task package failed: {}", writingFile, deleteError);
            }
            throw new IllegalStateException("Create auto execution task package failed, jobId: " + jobId, e);
        }
    }

    @Override
    public byte[] read(long jobId, long attachmentId, long offset, int length) {
        if (offset < 0 || length <= 0) {
            throw new IllegalArgumentException("Invalid auto execution task package read range.");
        }
        DmExecAutoJobDO job = requireJob(jobId);
        return this.localFileService.consumeEditing(job.getUid(), attachmentId, packageFile -> {
            try (FileChannel channel = FileChannel.open(packageFile, StandardOpenOption.READ)) {
                long fileSize = channel.size();
                if (offset >= fileSize) {
                    this.localFileService.renewEditing(job.getUid(), attachmentId);
                    return new byte[0];
                }

                int readLength = (int) Math.min(length, fileSize - offset);
                ByteBuffer buffer = ByteBuffer.allocate(readLength);
                channel.position(offset);
                while (buffer.hasRemaining() && channel.read(buffer) >= 0) {
                    // Continue until the requested block is full or EOF is reached.
                }

                byte[] result;
                if (buffer.position() == readLength) {
                    result = buffer.array();
                } else {
                    result = new byte[buffer.position()];
                    buffer.flip();
                    buffer.get(result);
                }
                this.localFileService.renewEditing(job.getUid(), attachmentId);
                return result;
            }
        });
    }

    @Override
    public void delete(long attachmentId) {
        this.localFileService.deleteRecord(attachmentId);
    }

    private DmExecAutoJobDO requireJob(long jobId) {
        DmExecAutoJobDO job = this.execDal.autoJobMapper().queryById(jobId);
        if (job == null) {
            throw new IllegalStateException("Auto execution job does not exist, jobId: " + jobId);
        }
        return job;
    }
}
