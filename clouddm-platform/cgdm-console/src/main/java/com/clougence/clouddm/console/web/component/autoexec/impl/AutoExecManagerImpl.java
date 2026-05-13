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

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.sidecar.autoexec.AutoExecRService;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.auth.BizResOwnerCacheService;
import com.clougence.clouddm.console.web.component.auth.model.DsCacheEntry;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecManager;
import com.clougence.clouddm.console.web.constants.DmErrorCode;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.AutoExecJobStatus;
import com.clougence.clouddm.console.web.dal.enumeration.DmLogDependBizType;
import com.clougence.clouddm.console.web.dal.enumeration.Loglevel;
import com.clougence.clouddm.console.web.dal.mapper.DmAutoExecJobMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmBizLogMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmAutoExecJobDO;
import com.clougence.clouddm.console.web.dal.model.exec.DmBizLogDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.rdp.global.exception.ErrorMessageException;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecManagerImpl implements AutoExecManager {

    @Resource
    private DmAutoExecJobMapper     dmAutoExecJobMapper;
    @Resource
    private AutoExecRService        autoExecRService;
    @Resource
    private DmWorkerMapper          dmWorkerMapper;
    @Resource
    private BizResOwnerCacheService ownerCacheService;
    @Resource
    private DmBizLogMapper          dmBizLogMapper;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void dispatchJob(Long jobId) {
        DmAutoExecJobDO dmAutoExecJobDO = dmAutoExecJobMapper.queryByIdForUpdate(jobId);
        if (dmAutoExecJobDO.getStatus() != AutoExecJobStatus.INIT) {
            log.info("{} was dispatch by another console", jobId);
            return;
        }

        // dispatch
        DsCacheEntry dsCacheEntry = ownerCacheService.queryByDsId(dmAutoExecJobDO.getDataSourceId());
        if (dsCacheEntry.getClusterId() == null) {
            DmBizLogDO logDO = new DmBizLogDO(Loglevel.INFO,
                DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_DATASOURCE_ERROR_MESSAGE.name()),
                DmLogDependBizType.AUTO_EXEC_JOB,
                dmAutoExecJobDO.getBizId());
            dmBizLogMapper.insert(logDO);
            this.dmAutoExecJobMapper.updateJobStatus(dmAutoExecJobDO.getId(), AutoExecJobStatus.FAILED);
            return;
        }
        RSocketSendDTO dto = this.buildRSocketSendDTO(dsCacheEntry.getClusterId());
        autoExecRService.dispatchJob(dto, jobId);

        DmBizLogDO logDO = new DmBizLogDO(Loglevel.INFO,
            DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_START_MESSAGE.name(), dto.getWorkerIP()),
            DmLogDependBizType.AUTO_EXEC_JOB,
            dmAutoExecJobDO.getBizId());

        dmBizLogMapper.insert(logDO);

        dmAutoExecJobDO.setStatus(AutoExecJobStatus.WAIT_EXEC);
        dmAutoExecJobDO.setLastReportTime(new Date());
        dmAutoExecJobDO.setWorkerSeqNumber(dto.getWorkerSeqNumber());
        this.dmAutoExecJobMapper.updateById(dmAutoExecJobDO);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void stopJob(Long jobId, RdpUserDO user) {
        DmAutoExecJobDO job = dmAutoExecJobMapper.queryByIdForUpdate(jobId);

        AutoExecJobStatus status = job.getStatus();
        if (status == AutoExecJobStatus.INIT) {
            job.setStatus(AutoExecJobStatus.PAUSE);
            this.dmAutoExecJobMapper.updateById(job);

            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_DIRECT_PAUSE_MESSAGE.name(), user.getUsername(), user.getUid());
            DmBizLogDO logDO = new DmBizLogDO(Loglevel.INFO, message, DmLogDependBizType.AUTO_EXEC_JOB, job.getBizId());
            this.dmBizLogMapper.insert(logDO);
            return;
        }

        if (status == AutoExecJobStatus.PAUSE || status == AutoExecJobStatus.FAILED || status == AutoExecJobStatus.FINISH) {
            log.warn("{} was already stop", jobId);
            return;
        }

        if (status == AutoExecJobStatus.PAUSING) {
            return;
        }

        this.autoExecRService.pauseJob(buildRSocketSendDTO(job.getWorkerSeqNumber()), jobId);

        job.setStatus(AutoExecJobStatus.PAUSING);
        this.dmAutoExecJobMapper.updateById(job);

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_PAUSE_MESSAGE.name(), user.getUsername(), user.getUid());
        DmBizLogDO logDO = new DmBizLogDO(Loglevel.INFO, message, DmLogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        this.dmBizLogMapper.insert(logDO);
    }

    private RSocketSendDTO buildRSocketSendDTO(String wsn) {
        DmWorkerDO worker = dmWorkerMapper.getByWsn(wsn);

        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);

        return sendDTO;
    }

    private RSocketSendDTO buildRSocketSendDTO(long bindClusterId) {
        List<DmWorkerDO> workers = this.dmWorkerMapper.queryConnectedByClusterId(bindClusterId);
        if (workers.isEmpty()) {
            throw new ErrorMessageException(DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code(), MessageUtils.getClusterHaveNoWorksErrorMessage(bindClusterId));
        }

        DmWorkerDO worker = workers.get(new Random(System.currentTimeMillis()).nextInt(workers.size()));

        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);

        return sendDTO;
    }
}
