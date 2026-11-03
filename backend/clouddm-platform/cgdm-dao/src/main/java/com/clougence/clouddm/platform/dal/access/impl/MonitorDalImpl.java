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
package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.mapper.monitor.DmMonAlertConfigDetailMapper;
import com.clougence.clouddm.platform.dal.mapper.monitor.DmMonAlertEventLogMapper;
import com.clougence.clouddm.platform.dal.mapper.monitor.DmMonBizLogMapper;
import com.clougence.clouddm.platform.dal.mapper.monitor.DmMonOpAuditMapper;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MonitorDalImpl implements MonitorDal {

    @Resource
    private DmMonAlertConfigDetailMapper alertConfigDetailMapper;
    @Resource
    private DmMonAlertEventLogMapper     alertEventLogMapper;
    @Resource
    private DmMonBizLogMapper            bizLogMapper;
    @Resource
    private DmMonOpAuditMapper           opAuditMapper;

    @Override
    public DmMonAlertConfigDetailMapper alertConfigDetailMapper() {
        return alertConfigDetailMapper;
    }

    @Override
    public DmMonAlertEventLogMapper alertEventLogMapper() {
        return alertEventLogMapper;
    }

    @Override
    public DmMonBizLogMapper bizLogMapper() {
        return bizLogMapper;
    }

    @Override
    public DmMonOpAuditMapper opAuditMapper() {
        return opAuditMapper;
    }

    // ---------- dal service methods ----------
}
