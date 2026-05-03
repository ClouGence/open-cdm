package com.clougence.rdp.component.alert.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Service(value = "RdpFeishuImAlertService")
@Slf4j
public class RdpFeishuImAlertService extends AbstractRdpImAlertService {

    @Override
    protected String fetchImAlertUrl() {
        return rdpConfig.getFeishuAlertUrl();
    }

    @Override
    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<RdpUserDO> users, boolean atAll) {
        Map<String, String> contents = new HashMap<>();
        String fullMsg;
        if (atAll) {
            fullMsg = "<at user_id=\"all\">所有人</at>" + signName + msg;
        } else {
            fullMsg = signName + msg;
        }

        contents.put("text", fullMsg);

        Map<String, Object> params = new HashMap<>();
        params.put("msg_type", "text");
        params.put("content", contents);
        return JsonUtils.toJson(params);
    }

    @Override
    protected ServiceResponse serviceResCheck(Response response) throws IOException {
        String bodyString = super.requireRespBodyIsNotBlank(response);
        FeiShuApiResBody feiShuApiResBody = RdpJacksonUtil.readJsonWithUnknown(bodyString, FeiShuApiResBody.class);
        if (feiShuApiResBody.getCode() != 0) {
            return ServiceResponse.buildFailure(feiShuApiResBody.getCode().toString(), feiShuApiResBody.getMsg());
        }
        return ServiceResponse.buildSuccess();
    }

    @Getter
    @Setter
    private static class FeiShuApiResBody {

        private Integer code;
        private String  msg;
        private Object  data;
    }
}
