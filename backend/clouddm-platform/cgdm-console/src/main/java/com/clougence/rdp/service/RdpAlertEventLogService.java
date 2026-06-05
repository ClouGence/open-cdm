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
package com.clougence.rdp.service;

import java.util.List;

import com.clougence.clouddm.platform.dal.model.monitor.AlertEventStatus;
import com.clougence.clouddm.platform.dal.model.monitor.AlertMediaType;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonAlertEventLogDO;

/**
 * @author wanshao create time is 2020/4/15
 **/
public interface RdpAlertEventLogService {

    List<DmMonAlertEventLogDO> listAlertEventLogs(Long startTimeMillis, Long endTimeMillis, AlertEventStatus status, String uid, long startId, int pageSize);

    void save(AlertEventStatus alertEventStatus, String content, String errMsg, AlertMediaType alertMediaType, List<String> sendUids);
}
