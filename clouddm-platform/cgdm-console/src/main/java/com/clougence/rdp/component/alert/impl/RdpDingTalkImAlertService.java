package com.clougence.rdp.component.alert.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.util.RdpJacksonUtil;
import com.clougence.utils.JsonUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

/**
 * @author bucketli 2020-01-30 16:43
 * @since 1.1.3
 */
@Service(value = "RdpDingTalkImAlertService")
@Slf4j
public class RdpDingTalkImAlertService extends AbstractRdpImAlertService {

    @Override
    protected String fetchImAlertUrl() {
        return rdpConfig.getDingTalkAlertUrl();
    }

    @Override
    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<RdpUserDO> users, boolean atAll) {
        Map<String, String> contents = new HashMap<>();
        contents.put("content", signName + msg);

        Map<String, Object> params = new HashMap<>();
        if (users != null && !users.isEmpty()) {
            Map<String, Object> ats = new HashMap<>();
            ats.put("isAtAll", atAll);
            ats.put("atMobiles", users.stream().map(RdpUserDO::getPhone).collect(Collectors.toList()));
            params.put("at", ats);
        }

        params.put("msgtype", "text");
        params.put("text", contents);
        return JsonUtils.toJson(params);
    }

    @Override
    protected ServiceResponse serviceResCheck(Response response) throws IOException {
        String bodyString = super.requireRespBodyIsNotBlank(response);
        DingTalkApiRes dingTalkApiRes = RdpJacksonUtil.readJsonWithUnknown(bodyString, DingTalkApiRes.class);
        if (!dingTalkApiRes.getErrcode().equals("0")) {
            return ServiceResponse.buildFailure(dingTalkApiRes.errcode, dingTalkApiRes.errmsg);
        }
        return ServiceResponse.buildSuccess();
    }

    @Getter
    @Setter
    private static class DingTalkApiRes {

        private String errmsg;
        private String errcode;
    }
}
