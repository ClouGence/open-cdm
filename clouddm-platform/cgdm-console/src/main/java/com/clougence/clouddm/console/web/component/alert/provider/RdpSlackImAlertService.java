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
package com.clougence.clouddm.console.web.component.alert.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Service(value = "RdpSlackImAlertService")
@Slf4j
public class RdpSlackImAlertService extends AbstractRdpImAlertService {

    @Override
    protected String fetchImAlertUrl() {
        return rdpConfig.getSlackAlertUrl();
    }

    @Override
    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<DmAuthUserDO> users, boolean atAll) {
        Map<String, Object> params = new HashMap<>();
        params.put("text", msg);
        return JsonUtils.toJson(params);
    }
}
