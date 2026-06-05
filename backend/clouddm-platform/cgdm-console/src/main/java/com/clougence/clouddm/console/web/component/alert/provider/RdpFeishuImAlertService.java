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
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
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
// * @author bucketli 2020-01-30 16:43
// * @since 1.1.3
// */
//@Service(value = "RdpFeishuImAlertService")
//@Slf4j
//public class RdpFeishuImAlertService extends AbstractRdpImAlertService {
//
//    @Override
//    protected String fetchImAlertUrl() {
//        return rdpConfig.getFeishuAlertUrl();
//    }
//
//    @Override
//    protected String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<DmAuthUserDO> users, boolean atAll) {
//        Map<String, String> contents = new HashMap<>();
//        String fullMsg;
//        if (atAll) {
//            fullMsg = "<at user_id=\"all\">所有人</at>" + signName + msg;
//        } else {
//            fullMsg = signName + msg;
//        }
//
//        contents.put("text", fullMsg);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("msg_type", "text");
//        params.put("content", contents);
//        return JsonUtils.toJson(params);
//    }
//
//    @Override
//    protected ServiceResponse serviceResCheck(Response response) throws IOException {
//        String bodyString = super.requireRespBodyIsNotBlank(response);
//        FeiShuApiResBody feiShuApiResBody = RdpJacksonUtil.readJsonWithUnknown(bodyString, FeiShuApiResBody.class);
//        if (feiShuApiResBody.getCode() != 0) {
//            return ServiceResponse.buildFailure(feiShuApiResBody.getCode().toString(), feiShuApiResBody.getMsg());
//        }
//        return ServiceResponse.buildSuccess();
//    }
//
//    @Getter
//    @Setter
//    private static class FeiShuApiResBody {
//
//        private Integer code;
//        private String  msg;
//        private Object  data;
//    }
//}
