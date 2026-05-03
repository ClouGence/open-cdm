package com.clougence.rdp.component.alert.impl;

import java.io.IOException;
import java.util.Collections;
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
 * @author bucketli 2021/9/30 09:42
 */
@Service(value = "RdpWeixinImAlertService")
@Slf4j
public class RdpWeixinImAlertService extends AbstractRdpImAlertService {

    @Override
    protected String fetchImAlertUrl() {
        return rdpConfig.getWeixinAlertUrl();
    }

    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<RdpUserDO> users, boolean atAll) {
        Map<String, Object> contents = new HashMap<>();
        contents.put("content", signName + msg);
        if (atAll) {
            contents.put("mentioned_list", Collections.singletonList("@all"));
        } else if (users != null && !users.isEmpty()) {
            contents.put("mentioned_mobile_list", users.stream().map(RdpUserDO::getPhone).collect(Collectors.toList()));
        }

        Map<String, Object> params = new HashMap<>();
        params.put("msgtype", "text");
        params.put("text", contents);
        return JsonUtils.toJson(params);
    }

    @Override
    protected ServiceResponse serviceResCheck(Response response) throws IOException {
        String bodyString = super.requireRespBodyIsNotBlank(response);
        WeixinApiResBody weixinApiResBody = RdpJacksonUtil.readJsonWithUnknown(bodyString, WeixinApiResBody.class);
        if (weixinApiResBody.getErrcode() != 0) {
            return ServiceResponse.buildFailure(weixinApiResBody.getErrcode().toString(), weixinApiResBody.getErrmsg());
        }
        return ServiceResponse.buildSuccess();
    }

    @Getter
    @Setter
    private static class WeixinApiResBody {

        private Integer errcode;
        private String  errmsg;
    }
}
