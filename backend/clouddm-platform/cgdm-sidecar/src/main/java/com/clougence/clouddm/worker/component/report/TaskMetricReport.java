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
package com.clougence.clouddm.worker.component.report;

import java.util.Date;

import com.clougence.clouddm.api.console.status.ConsoleStatusRService;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.console.status.MetricStats;
import com.clougence.clouddm.api.console.status.UsageStats;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.worker.component.session.SessionManager;
import com.clougence.clouddm.worker.component.tools.ToolSessionManager;
import com.clougence.clouddm.worker.global.config.DmSidecarConfig;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * collect worker stat and report to console
 *
 * @author wanshao create time is 2020/1/22
 **/
@Slf4j
@Component
public class TaskMetricReport implements Runnable {

    @Resource
    private ConsoleStatusRService statusRService;
    @Resource
    private SessionManager        sessionManager;
    @Resource
    private ToolSessionManager    toolSessionManager;
    @Resource
    private DmSidecarConfig       dmConfig;
    private WorkerIdentity        workerIdentity;

    private long                  lastMetricReportMs     = 0L;
    private long                  lastMetricReportCounts = 0L;

    private WorkerIdentity identity() throws Exception {
        if (this.workerIdentity == null) {
            this.workerIdentity = ReportUtils.getIdentity();
        }
        return this.workerIdentity;
    }

    @Override
    public void run() {
        if (!this.dmConfig.isMetricReport()) {
            log.info("report metric ignore.");
            return;
        }

        try {
            requestAndHandle();
        } catch (Exception e) {
            log.error("report metric failed, msg:" + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    protected void requestAndHandle() throws Exception {
        long timeoutMs = this.lastMetricReportMs + (60 * 1000L);
        if (System.currentTimeMillis() > timeoutMs) {
            log.info("report metric to console. count=" + (this.lastMetricReportCounts + 1));
            this.lastMetricReportCounts = 0;
            this.lastMetricReportMs = System.currentTimeMillis();
        }

        this.statusRService.reportMetric(this.identity(), new Date(), this.buildMetricStats());
        this.lastMetricReportCounts++;
    }

    protected MetricStats buildMetricStats() {
        MetricStats stats = new MetricStats();
        stats.setCpuStats(PhysicalStatCollect.getCpuStat());
        stats.setMemStats(PhysicalStatCollect.getMemStat());
        stats.setDiskStats(PhysicalStatCollect.getDiskStat());
        stats.setSystemStats(PhysicalStatCollect.getSystemStat());
        stats.setUsageStats(this.buildUsageStats());
        return stats;
    }

    protected UsageStats buildUsageStats() {
        UsageStats sessionDTO = new UsageStats();
        sessionDTO.setSessionPoolMax(this.sessionManager.getMaxSessionCount());
        sessionDTO.setSessionPoolUse(this.sessionManager.getSessionCount());
        sessionDTO.setToolsPoolMax(this.toolSessionManager.getMaxSessionCount());
        sessionDTO.setToolsPoolUse(this.toolSessionManager.getSessionCount());
        return sessionDTO;
    }
}
