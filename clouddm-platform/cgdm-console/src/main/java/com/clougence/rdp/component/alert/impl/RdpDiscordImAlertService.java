package com.clougence.rdp.component.alert.impl;

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.util.RdpJacksonUtil;
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
    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<RdpUserDO> users, boolean atAll) {
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
