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

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.util.RdpJacksonUtil;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.JsonUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

/**
 * @date: 2023/5/30 17:09
 * @description:
 */
@Service(value = "RdpDiscordImAlertService")
@Slf4j
public class RdpDiscordImAlertService extends AbstractRdpImAlertService {

    @Override
    protected String fetchImAlertUrl() {
        return rdpConfig.getDiscordAlertUrl();
    }

    @Override
    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<DmAuthUserDO> users, boolean atAll) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", msg);
        return JsonUtils.toJson(params);
    }

    @Override
    protected boolean httpResCheck(Response response, Proxy proxy) {
        return response.code() == HttpStatus.SC_OK || response.code() == HttpStatus.SC_NO_CONTENT;
    }

    @Override
    protected ServiceResponse serviceResCheck(Response response) throws IOException {
        if (response.code() == HttpStatus.SC_NO_CONTENT) {
            return ServiceResponse.buildSuccess();
        }

        String bodyString = super.requireRespBodyIsNotBlank(response);
        DiscordApiResponse discordResp = RdpJacksonUtil.readJsonWithUnknown(bodyString, DiscordApiResponse.class);
        if (discordResp.getCode() != 0) {
            log.error("Im send to discord has a fatal response with errors: {} ", discordResp.errors);
            return ServiceResponse.buildFailure(discordResp.code.toString(), discordResp.message);
        }
        return ServiceResponse.buildSuccess();
    }

    @Getter
    @Setter
    private static class DiscordApiResponse {

        private Integer code;
        // Actually, 'Discord' provides to many possibility about errors type,
        // we don't care about how to extract concise info from them, just print them enough
        private String  errors;
        private String  message;
    }

}
