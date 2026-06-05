///*
// * Copyright 2026 杭州开云集致科技有限公司
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.clougence.clouddm.console.web.component.alert.provider;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import com.clougence.clouddm.console.web.util.RdpJacksonUtil;
//import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
//import com.clougence.utils.JsonUtils;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.Response;
//
///**
// * @author bucketli 2021/9/30 09:42
// */
//@Service(value = "RdpWeixinImAlertService")
//@Slf4j
//public class RdpWeixinImAlertService extends AbstractRdpImAlertService {
//
//    @Override
//    protected String fetchImAlertUrl() {
//        return rdpConfig.getWeixinAlertUrl();
//    }
//
//    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<DmAuthUserDO> users, boolean atAll) {
//        Map<String, Object> contents = new HashMap<>();
//        contents.put("content", signName + msg);
//        if (atAll) {
//            contents.put("mentioned_list", Collections.singletonList("@all"));
//        } else if (users != null && !users.isEmpty()) {
//            contents.put("mentioned_mobile_list", users.stream().map(DmAuthUserDO::getPhone).collect(Collectors.toList()));
//        }
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("msgtype", "text");
//        params.put("text", contents);
//        return JsonUtils.toJson(params);
//    }
//
//    @Override
//    protected ServiceResponse serviceResCheck(Response response) throws IOException {
//        String bodyString = super.requireRespBodyIsNotBlank(response);
//        WeixinApiResBody weixinApiResBody = RdpJacksonUtil.readJsonWithUnknown(bodyString, WeixinApiResBody.class);
//        if (weixinApiResBody.getErrcode() != 0) {
//            return ServiceResponse.buildFailure(weixinApiResBody.getErrcode().toString(), weixinApiResBody.getErrmsg());
//        }
//        return ServiceResponse.buildSuccess();
//    }
//
//    @Getter
//    @Setter
//    private static class WeixinApiResBody {
//
//        private Integer errcode;
//        private String  errmsg;
//    }
//}
