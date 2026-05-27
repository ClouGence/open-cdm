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
package com.clougence.clouddm.console.web.component.alert;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.rdp.enumeration.AlarmLevel;
import com.clougence.clouddm.console.web.component.alert.model.SendMsgResult;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;

/**
 * @author bucketli 2020-01-30 16:40
 * @since 1.1.3
 */
public interface RdpImAlertService {

    //    String signName = "【CloudCanal】";

    /**
     * use IM to send alert to one or more Chat Group e.g.,WeChat,DingDing and so on...
     * or use custom Url to send alert
     */
    SendMsgResult sendMsg(String signName, String msg, Map<String, Object> msgParams, DmAuthUserDO owner, List<DmAuthUserDO> receivers, AlarmLevel alarmLevel, boolean atAll);

    SendMsgResult sendMsgWithWebHook(String webHook, String proxyAddr, String signName, String msg, Map<String, Object> msgParams, DmAuthUserDO owner, List<DmAuthUserDO> receivers,
                                     AlarmLevel alarmLevel, boolean atAll);

    SendMsgResult sendMsgToOwner(String signName, String msg, Map<String, Object> msgParams, DmAuthUserDO owner, AlarmLevel alarmLevel, boolean atAll);

    SendMsgResult sendMsgToSys(String signName, String msg, Map<String, Object> msgParams, AlarmLevel alarmLevel, String uid, boolean atAll);
}
