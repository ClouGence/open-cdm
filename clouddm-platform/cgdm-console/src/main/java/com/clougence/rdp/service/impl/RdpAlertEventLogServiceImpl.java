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
package com.clougence.rdp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.model.monitor.AlertEventStatus;
import com.clougence.clouddm.platform.dal.model.monitor.AlertMediaType;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonAlertEventLogDO;
import com.clougence.rdp.service.RdpAlertEventLogService;
import com.clougence.utils.CollectionUtils;

import jakarta.annotation.Resource;

@Service
public class RdpAlertEventLogServiceImpl implements RdpAlertEventLogService {

    @Resource
    private MonitorDal      monitorDal;
    @Resource
    private DmConsoleConfig rdpConfig;

    @Override
    public List<DmMonAlertEventLogDO> listAlertEventLogs(Long startTimeMillis, Long endTimeMillis, AlertEventStatus status, String uid, long startId, int pageSize) {
        Date startTime = null;
        if (startTimeMillis != null) {
            startTime = new Date(startTimeMillis);
        }

        Date endTime = null;
        if (endTimeMillis != null) {
            endTime = new Date(endTimeMillis);
        }

        return monitorDal.alertEventLogMapper().queryPageAlertEventLogs(startId, pageSize, startTime, endTime, status, uid);
    }

    @Override
    public void save(AlertEventStatus alertEventStatus, String content, String errMsg, AlertMediaType alertMediaType, List<String> sendUids) {
        DmMonAlertEventLogDO alertLogDO = new DmMonAlertEventLogDO();
        alertLogDO.setStatus(alertEventStatus);
        alertLogDO.setContent(content);
        alertLogDO.setIp(rdpConfig.getConsolePackageMode().getLocalIpOrHostName());

        alertLogDO.setErrMsg(errMsg);
        alertLogDO.setAlertMediaType(alertMediaType);

        if (CollectionUtils.isNotEmpty(sendUids)) {
            for (String uid : sendUids) {
                alertLogDO.setId(null);
                alertLogDO.setUid(uid);
                monitorDal.alertEventLogMapper().insert(alertLogDO);
            }
        } else {
            monitorDal.alertEventLogMapper().insert(alertLogDO);
        }
    }
}
