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

import com.clougence.clouddm.console.web.component.alert.model.SendMsgResult;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;

/**
 * @author bucketli 2020-01-31 11:59
 * @since 1.1.3
 */
public interface RdpSmsAlertService {

    /**
     * Use owner config (SMS service ak / sk / code)
     */
    SendMsgResult sendUserMsg(List<DmAuthUserDO> sendUsers, String msgTemplateCode, Map<String, String> templateParams, String ownerUid);

    /**
     * Use CloudCanal system config (SMS service ak / sk / code...)
     */
    SendMsgResult sendSysMsg(List<String> phones, String msgTemplateCode, Map<String, String> templateParams);
}
