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
//package com.clougence.rdp.service.impl;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import jakarta.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//
//import com.clougence.rdp.component.jwtsession.RdpJwtService;
//import com.clougence.rdp.constant.I18nRdpMsgKeys;
//import com.clougence.rdp.controller.model.vo.LogViewVO;
//import com.clougence.clouddm.console.web.dal.mapper.RdpOpAuditMapper;
//import com.clougence.clouddm.console.web.dal.model.RdpOpAuditDO;
//import com.clougence.rdp.global.config.RdpConsoleConfig;
//import com.clougence.rdp.service.RdpLogViewService;
//import com.clougence.rdp.service.RdpUserService;
//import com.clougence.rdp.util.DmI18nUtils;
//import com.clougence.rdp.util.RdpJacksonUtil;
//import com.clougence.utils.JsonUtils;
//import com.clougence.utils.StringUtils;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author wanshao create time is 2020/10/22
// **/
//@Service
//@Slf4j
//public class RdpLogViewServiceImpl implements RdpLogViewService {
//
//    private static final String AUDIT_LOG_I18_PREFIX       = "audit_detail_label_";
//    private static final String OPERATION_LOG              = "user_audit_detail.log";
//    private static final String GREP_OPERATION_FORWARD_URL = "/rdp/console/api/v1/logview/grep/operationlog";
//    //    private static final String OPERATION_LOG_DEFAULT_PATH = System.getProperties().getProperty("user.home") + "/logs/cloudcanal/console/user_audit_detail.log";
//    private String              jwtToken;
//    @Resource
//    private RdpJwtService       rdpJwtService;
//
//    @Resource
//    private RdpUserService      rdpUserService;
//
//    @Resource
//    private RdpOpAuditMapper    opAuditMapper;
//
//    @Resource
//    private RdpConsoleConfig    rdpConfig;
//
//    @Override
//    public LogViewVO grepOperationLogs(Long operationId) {
//        RdpOpAuditDO operationAuditDO = opAuditMapper.selectById(operationId);
//
//        if (operationAuditDO == null) {
//            throw new IllegalArgumentException("operation log (" + operationId + ") is not exist.");
//        }
//
//        LogViewVO logViewVO = new LogViewVO();
//        logViewVO.setPath(operationAuditDO.getLogPath());
//
//        logViewVO.setFileName(OPERATION_LOG);
//        logViewVO.setDesc(DmI18nUtils.getMessage(I18nRdpMsgKeys.RDP_CONSOLE_OPERATION_LOG_DESC.name()));
//        logViewVO.setContent(buildContent(operationAuditDO.getLogInfo()));
//        return logViewVO;
//    }
//
//    //    @SneakyThrows
//    //    private LogViewVO forwardRequest(String consoleIp, Long operationId) {
//    //        String url = "http://" + consoleIp + ":8111" + GREP_OPERATION_FORWARD_URL;
//    //
//    //        refreshJwtTokenIfNeed();
//    //
//    //        Map<String, String> headers = new HashMap<>();
//    //        headers.put("jwt_token", jwtToken);
//    //        headers.put(RdpJwtManager.CSRF_TOKEN_NAME, jwtToken);
//    //        GrepOperationLogFO forwardDTO = new GrepOperationLogFO();
//    //        forwardDTO.setOperationId(operationId);
//    //        ResWebData<?> responseData = RdpHttpClient.post(url, new ObjectMapper().writeValueAsString(forwardDTO), headers);
//    //
//    //        LogViewVO logViewVO = new ObjectMapper().readValue(String.valueOf(responseData.getData()), new TypeReference<LogViewVO>() {
//    //        });
//    //
//    //        return logViewVO;
//    //    }
//
//    private boolean ifNeedForward(String consoleIp) {
//        return !rdpConfig.getConsolePackageMode().getLocalIpOrHostName().equals(consoleIp);
//    }
//
//    protected void refreshJwtTokenIfNeed() {
//        if (StringUtils.isBlank(jwtToken) || rdpJwtService.verifyJwtToken(jwtToken) == null) {
//            this.jwtToken = rdpJwtService.genJwtToken(rdpUserService.getInnerUser());
//        }
//    }
//
//    public static String buildContent(String log) {
//        Object parseObj = null;
//        try {
//            parseObj = RdpJacksonUtil.toObj(log, Object.class);
//        } catch (Exception e) {
//            return log;
//        }
//        if (parseObj instanceof Map) {
//            Map jsonObject = (Map) parseObj;
//            if (jsonObject.isEmpty()) {
//                return "{}";
//            }
//            Map<String, Object> newJsonObject = new LinkedHashMap<>();
//            for (Object key : jsonObject.keySet()) {
//                String message = DmI18nUtils.getMessage(AUDIT_LOG_I18_PREFIX + key.toString());
//                if (message.startsWith(AUDIT_LOG_I18_PREFIX)) {
//                    message = key.toString();
//                }
//                newJsonObject.put(message, jsonObject.get(key));
//            }
//            return JsonUtils.toJson(newJsonObject);
//        } else if (parseObj instanceof List) {
//            List<Object> jsonArray = (List) parseObj;
//            if (jsonArray.isEmpty()) {
//                return "[]";
//            }
//            List<Map<String, Object>> result = new java.util.ArrayList<>();
//            for (Object map : jsonArray) {
//                Map<String, Object> jsonMap = (LinkedHashMap<String, Object>) map;
//                Map<String, Object> newMap = new LinkedHashMap<>();
//                for (Object key : jsonMap.keySet()) {
//                    String message = DmI18nUtils.getMessage(AUDIT_LOG_I18_PREFIX + key.toString());
//                    if (message.startsWith(AUDIT_LOG_I18_PREFIX)) {
//                        message = key.toString();
//                    }
//                    newMap.put(message, jsonMap.get(key));
//                }
//                result.add(newMap);
//            }
//            return JsonUtils.toJson(result);
//        } else {
//            return log;
//        }
//    }
//
//}
