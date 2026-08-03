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
package com.clougence.clouddm.console.web.provider;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.clougence.clouddm.api.sidecar.autoexec.AutoExecMessageDTO;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelper;
import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelperService;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecAutoJobMapper;
import com.clougence.clouddm.platform.dal.mapper.execution.DmExecAutoTaskMapper;
import com.clougence.clouddm.platform.dal.mapper.monitor.DmMonBizLogMapper;
import com.clougence.clouddm.platform.dal.model.execution.AutoExecJobStatus;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoJobDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAutoTaskDO;
import com.clougence.clouddm.platform.dal.model.execution.SQLJobBizType;

public class ExecJobRServiceProviderTest {

    private DmExecAutoJobMapper     jobMapper;
    private DmMonBizLogMapper       bizLogMapper;
    private AutoExecHelper          changeHelper;
    private ExecJobRServiceProvider provider;

    @Before
    public void setUp() {
        ExecutionDal executionDal = mock(ExecutionDal.class);
        jobMapper = mock(DmExecAutoJobMapper.class);
        DmExecAutoTaskMapper taskMapper = mock(DmExecAutoTaskMapper.class);
        when(executionDal.autoJobMapper()).thenReturn(jobMapper);
        when(executionDal.autoTaskMapper()).thenReturn(taskMapper);

        MonitorDal monitorDal = mock(MonitorDal.class);
        bizLogMapper = mock(DmMonBizLogMapper.class);
        when(monitorDal.bizLogMapper()).thenReturn(bizLogMapper);

        AutoExecHelperService helperService = mock(AutoExecHelperService.class);
        changeHelper = mock(AutoExecHelper.class);
        when(helperService.getHelper(SQLJobBizType.CHANGE)).thenReturn(changeHelper);

        DmExecAutoJobDO job = new DmExecAutoJobDO();
        job.setId(6L);
        job.setBizId("change-job");
        job.setDependOnBizType(SQLJobBizType.CHANGE);
        job.setStatus(AutoExecJobStatus.EXECUTING);
        when(jobMapper.selectById(6L)).thenReturn(job);

        DmExecAutoTaskDO task = new DmExecAutoTaskDO();
        task.setId(9L);
        task.setAutoExecJobId(6L);
        task.setExecOrder(1);
        task.setExecSql("select 1");
        when(taskMapper.selectById(9L)).thenReturn(task);
        when(jobMapper.markJobFailedIfActive(6L)).thenReturn(1);
        when(jobMapper.finishJobIfActive(6L)).thenReturn(1);
        when(jobMapper.pauseJobIfActive(6L)).thenReturn(1);

        provider = new ExecJobRServiceProvider();
        ReflectionTestUtils.setField(provider, "execDal", executionDal);
        ReflectionTestUtils.setField(provider, "monitorDal", monitorDal);
        ReflectionTestUtils.setField(provider, "execHelperService", helperService);
    }

    @Test
    public void shouldNotifyChangeWhenJobFails() {
        ReflectionTestUtils.invokeMethod(provider, "jobFailed", AutoExecMessageDTO.jobFailedMessage(6L, 9L));

        verify(jobMapper).markJobFailedIfActive(6L);
        verify(changeHelper).execFailed(SQLJobBizType.CHANGE, "change-job");
    }

    @Test
    public void shouldIgnoreDuplicateJobFailureAfterTerminalTransition() {
        when(jobMapper.markJobFailedIfActive(6L)).thenReturn(0);

        ReflectionTestUtils.invokeMethod(provider, "jobFailed", AutoExecMessageDTO.jobFailedMessage(6L, 9L));

        verify(changeHelper, never()).execFailed(any(), anyString());
        verifyNoInteractions(bizLogMapper);
    }

    @Test
    public void shouldIgnoreJobFailureForTaskFromAnotherJob() {
        DmExecAutoTaskDO foreignTask = new DmExecAutoTaskDO();
        foreignTask.setId(9L);
        foreignTask.setAutoExecJobId(7L);
        when(providerTaskMapper().selectById(9L)).thenReturn(foreignTask);

        ReflectionTestUtils.invokeMethod(provider, "jobFailed", AutoExecMessageDTO.jobFailedMessage(6L, 9L));

        verify(jobMapper, never()).markJobFailedIfActive(anyLong());
        verifyNoInteractions(changeHelper, bizLogMapper);
    }

    @Test
    public void shouldNotifyChangeWhenJobFinishes() {
        ReflectionTestUtils.invokeMethod(provider, "jobFinish", AutoExecMessageDTO.jobFinishMessage(6L));

        verify(jobMapper).finishJobIfActive(6L);
        verify(changeHelper).execCompleted(SQLJobBizType.CHANGE, "change-job");
    }

    @Test
    public void shouldIgnoreJobFinishAfterAnotherTerminalTransition() {
        when(jobMapper.finishJobIfActive(6L)).thenReturn(0);

        ReflectionTestUtils.invokeMethod(provider, "jobFinish", AutoExecMessageDTO.jobFinishMessage(6L));

        verify(changeHelper, never()).execCompleted(any(), anyString());
        verifyNoInteractions(bizLogMapper);
    }

    @Test
    public void shouldIgnoreJobPauseAfterTerminalTransition() {
        when(jobMapper.pauseJobIfActive(6L)).thenReturn(0);

        ReflectionTestUtils.invokeMethod(provider, "jobPause", AutoExecMessageDTO.jobPauseMessage(6L));

        verify(jobMapper).pauseJobIfActive(6L);
        verifyNoInteractions(bizLogMapper);
    }

    private DmExecAutoTaskMapper providerTaskMapper() {
        ExecutionDal executionDal = (ExecutionDal) ReflectionTestUtils.getField(provider, "execDal");
        return executionDal.autoTaskMapper();
    }
}
