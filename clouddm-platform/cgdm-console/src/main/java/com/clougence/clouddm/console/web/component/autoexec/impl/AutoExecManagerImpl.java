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

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.autoexec.AutoExecRService;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecManager;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.access.entry.DsCacheEntry;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonBizLogDO;
import com.clougence.clouddm.platform.dal.model.monitor.LogDependBizType;
import com.clougence.clouddm.platform.dal.model.monitor.Loglevel;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecManagerImpl implements AutoExecManager {
    @Resource
    private SystemDal        systemDal;
    @Resource
    private MonitorDal       monitorDal;
    @Resource
    private ExecutionDal     executionDal;
    @Resource
    private ObjectCacheDao   objectCacheDao;
    @Resource
    private AutoExecRService autoExecRService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void dispatchJob(Long jobId) {
        DmExecAutoJobDO dmAutoExecJobDO = executionDal.autoJobMapper().queryByIdForUpdate(jobId);
        if (dmAutoExecJobDO.getStatus() != AutoExecJobStatus.INIT) {
            log.info("{} was dispatch by another console", jobId);
            return;
        }

        // dispatch
        DsCacheEntry dsCacheEntry = objectCacheDao.queryByDsId(dmAutoExecJobDO.getDataSourceId());
        if (dsCacheEntry.getClusterId() == null) {
            DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO,
                DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_DATASOURCE_ERROR_MESSAGE.name()),
                LogDependBizType.AUTO_EXEC_JOB,
                dmAutoExecJobDO.getBizId());
            monitorDal.bizLogMapper().insert(logDO);
            this.executionDal.autoJobMapper().updateJobStatus(dmAutoExecJobDO.getId(), AutoExecJobStatus.FAILED);
            return;
        }
        RSocketSendDTO dto = this.buildRSocketSendDTO(dsCacheEntry.getClusterId());
        autoExecRService.dispatchJob(dto, jobId);

        DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO,
            DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_START_MESSAGE.name(), dto.getWorkerIP()),
            LogDependBizType.AUTO_EXEC_JOB,
            dmAutoExecJobDO.getBizId());

        monitorDal.bizLogMapper().insert(logDO);

        dmAutoExecJobDO.setStatus(AutoExecJobStatus.WAIT_EXEC);
        dmAutoExecJobDO.setLastReportTime(new Date());
        dmAutoExecJobDO.setWorkerSeqNumber(dto.getWorkerSeqNumber());
        this.executionDal.autoJobMapper().updateById(dmAutoExecJobDO);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void stopJob(Long jobId, DmAuthUserDO user) {
        DmExecAutoJobDO job = executionDal.autoJobMapper().queryByIdForUpdate(jobId);

        AutoExecJobStatus status = job.getStatus();
        if (status == AutoExecJobStatus.INIT) {
            job.setStatus(AutoExecJobStatus.PAUSE);
            this.executionDal.autoJobMapper().updateById(job);

            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_DIRECT_PAUSE_MESSAGE.name(), user.getUsername(), user.getUid());
            DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
            this.monitorDal.bizLogMapper().insert(logDO);
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
        this.executionDal.autoJobMapper().updateById(job);

        String message = DmI18nUtils.getMessage(I18nDmMsgKeys.AUTO_EXEC_JOB_CONSOLE_PAUSE_MESSAGE.name(), user.getUsername(), user.getUid());
        DmMonBizLogDO logDO = new DmMonBizLogDO(Loglevel.INFO, message, LogDependBizType.AUTO_EXEC_JOB, job.getBizId());
        this.monitorDal.bizLogMapper().insert(logDO);
    }

    private RSocketSendDTO buildRSocketSendDTO(String wsn) {
        DmSysWorkerDO worker = systemDal.workerMapper().getByWsn(wsn);

        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);

        return sendDTO;
    }

    private RSocketSendDTO buildRSocketSendDTO(long bindClusterId) {
        List<DmSysWorkerDO> workers = this.systemDal.workerMapper().queryConnectedByClusterId(bindClusterId);
        if (workers.isEmpty()) {
            throw new ErrorMessageException(DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code(), MessageUtils.getClusterHaveNoWorksErrorMessage(bindClusterId));
        }

        DmSysWorkerDO worker = workers.get(new Random(System.currentTimeMillis()).nextInt(workers.size()));

        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);

        return sendDTO;
    }
}
