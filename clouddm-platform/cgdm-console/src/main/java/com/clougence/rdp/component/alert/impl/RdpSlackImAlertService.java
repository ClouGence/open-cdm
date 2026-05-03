package com.clougence.rdp.component.alert.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.rdp.dal.model.RdpUserDO;
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
    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<RdpUserDO> users, boolean atAll) {
        Map<String, Object> params = new HashMap<>();
        params.put("text", msg);
        return JsonUtils.toJson(params);
    }
}
