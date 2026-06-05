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
package com.clougence.clouddm.console.web.service.system;

import java.util.List;

import com.clougence.clouddm.console.web.model.fo.cluster.OnOffWorkerAlertFO;
import com.clougence.clouddm.console.web.model.vo.AlertConfigVO;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonAlertConfigDetailDO;
import com.clougence.clouddm.platform.dal.model.monitor.EventType;

/**
 * @author wanshao create time is 2020/4/13
 **/
public interface AlertConfigService {

    //    List<AlertConfigVO> listSystemAlertConfig();

    //    void updateByIdAndUid(AlertConfigVO alertConfigVO);

    void onOffWorkerAlert(OnOffWorkerAlertFO configFo, String uid);

    void addAlertConfig(List<DmMonAlertConfigDetailDO> alertConfigVOList, EventType eventType);

    //    List<DmMonAlertConfigDetailDO> getAlertConfigByEventType(EventType eventType);

    AlertConfigVO convertToAlertConfigVO(DmMonAlertConfigDetailDO alertConfigDetailDO);

    //    AlertConfigVO getWorkerAlertConfig(long workerId, String uid);

    //    DmMonAlertConfigDetailDO getWorkerAlertConfigDO(long workerId);

    void deleteByWorkerId(long workerId);
}
