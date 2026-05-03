package com.clougence.rdp.component.alert.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2020-01-30 16:43
 * @since 1.1.3
 */
@Service(value = "RdpCustomAlertService")
@Slf4j
public class RdpCustomAlertService extends AbstractRdpImAlertService {

    @Override
    protected String fetchImAlertUrl() {
        return rdpConfig.getCustomAlertUrl();
    }

    @Override
    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<RdpUserDO> users, boolean atAll) {
        Map<String, Object> params = new HashMap<>();

        if (users != null && !users.isEmpty()) {
            params.put("alert_user", users.stream().map(RdpUserDO::getUsername).collect(Collectors.toList()));
        }

        if (msgParams != null) {
            params.putAll(msgParams);
        }

        params.put("content", signName + msg);

        return JsonUtils.toJson(params);
    }
}
