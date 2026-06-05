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

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.model.fo.cluster.OnOffWorkerAlertFO;
import com.clougence.clouddm.console.web.model.vo.AlertConfigVO;
import com.clougence.clouddm.platform.dal.access.MonitorDal;
import com.clougence.clouddm.platform.dal.model.monitor.DmMonAlertConfigDetailDO;
import com.clougence.clouddm.platform.dal.model.monitor.EventType;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Service
@Slf4j
public class AlertConfigServiceImpl implements AlertConfigService {
    @Resource
    private MonitorDal monitorDal;

    //    @Override
    //    public List<AlertConfigVO> listSystemAlertConfig() {
    //        List<DmMonAlertConfigDetailDO> configDetails = monitorDal.alertConfigDetailMapper().listByEventTypes(Lists.newArrayList(EventType.CONSOLE_EXCEPTION, EventType.WORKER_EXCEPTION));
    //        return convertToAlertConfigVO(configDetails);
    //    }

    //    @Override
    //    public void updateByIdAndUid(AlertConfigVO alertConfigVO) {
    //        DmMonAlertConfigDetailDO alertConfigDetailDO = convertToDetailDO(alertConfigVO);
    //        monitorDal.alertConfigDetailMapper().updateByIdAndUid(alertConfigDetailDO);
    //    }

    @Override
    public void onOffWorkerAlert(OnOffWorkerAlertFO configFo, String uid) {
        monitorDal.alertConfigDetailMapper().updateSwitchByWorker(configFo.isPhone(), configFo.isEmail(), configFo.isDingding(), configFo.isSms(), uid, configFo.getWorkerId());
    }

    @Override
    public void addAlertConfig(List<DmMonAlertConfigDetailDO> alertConfigVOList, EventType eventType) {
        for (DmMonAlertConfigDetailDO config : alertConfigVOList) {
            config.setEventType(eventType);
            monitorDal.alertConfigDetailMapper().insert(config);
        }
    }

    @Override
    public AlertConfigVO convertToAlertConfigVO(DmMonAlertConfigDetailDO alertConfigDetailDO) {
        AlertConfigVO vo = new AlertConfigVO();
        //        vo.setId(alertConfigDetailDO.getId());
        vo.setDingding(alertConfigDetailDO.isDingding());
        vo.setPhone(alertConfigDetailDO.isPhone());
        vo.setEmail(alertConfigDetailDO.isEmail());
        vo.setSms(alertConfigDetailDO.isSms());
        vo.setUid(alertConfigDetailDO.getUid());
        vo.setDuplicated(alertConfigDetailDO.isDuplicated());
        vo.setExpression(alertConfigDetailDO.getExpression());
        vo.setRuleName(alertConfigDetailDO.getRuleName());
        vo.setSendAdmin(alertConfigDetailDO.isSendAdmin());
        vo.setSendSystem(alertConfigDetailDO.isSendSystem());
        vo.setWorkerId(alertConfigDetailDO.getWorkerId());
        return vo;
    }

    //    @Override
    //    public AlertConfigVO getWorkerAlertConfig(long workerId, String uid) {
    //        DmMonAlertConfigDetailDO alertConfigDetailDO = monitorDal.alertConfigDetailMapper().listByWorkerId(workerId);
    //        if (alertConfigDetailDO == null) {
    //            AlertConfigVO alertConfigVO = workerService.generateDefaultWorkerAlertConfigVO(uid, workerId);
    //            this.addAlertConfig(Lists.newArrayList(alertConfigVO), EventType.WORKER_EXCEPTION);
    //            return alertConfigVO;
    //        } else {
    //            return convertToAlertConfigVO(alertConfigDetailDO);
    //        }
    //    }

    //    @Override
    //    public DmMonAlertConfigDetailDO getWorkerAlertConfigDO(long workerId) {
    //        return monitorDal.alertConfigDetailMapper().listByWorkerId(workerId);
    //    }

    @Override
    public void deleteByWorkerId(long workerId) {
        monitorDal.alertConfigDetailMapper().deleteByWorkerId(workerId);
    }

    private List<AlertConfigVO> convertToAlertConfigVO(List<DmMonAlertConfigDetailDO> alertConfigDetailDOList) {
        List<AlertConfigVO> alertConfigVOList = new ArrayList<>();
        for (DmMonAlertConfigDetailDO alertConfigDetailDO : alertConfigDetailDOList) {
            alertConfigVOList.add(convertToAlertConfigVO(alertConfigDetailDO));
        }
        return alertConfigVOList;
    }
}
