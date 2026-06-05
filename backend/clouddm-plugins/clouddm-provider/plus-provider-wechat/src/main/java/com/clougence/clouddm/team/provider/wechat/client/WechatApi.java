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
package com.clougence.clouddm.team.provider.wechat.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import org.slf4j.Logger;

import com.alibaba.fastjson2.JSONObject;
import com.clougence.clouddm.team.provider.wechat.constants.WechatI18nKey2;
import com.clougence.clouddm.sdk.approval.ApprovalTemplate;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import cn.felord.WeComException;
import cn.felord.domain.GenericResponse;
import cn.felord.domain.approval.*;
import cn.felord.domain.common.TemplateId;
import cn.felord.domain.contactbook.user.Mobile;
import cn.felord.domain.contactbook.user.UserInfoResponse;
import lombok.Getter;
import okhttp3.*;

@Getter
public class WechatApi {

    private final Logger       log;
    private final WechatClient client;
    private final String       token;
    private final String       encodingAesKey;

    public WechatApi(WechatClient client){
        this.log = client.getLogger();
        this.client = client;
        this.token = null;
        this.encodingAesKey = null;
    }

    public WechatApi(WechatClient client, String token, String encodingAesKey){
        this.log = client.getLogger();
        this.client = client;
        this.token = token;
        this.encodingAesKey = encodingAesKey;
    }

    public String getUserNameById(final String userId) {
        try {
            return this.client.retryCall(client -> {
                UserInfoResponse user = client.getUserApi().getUser(userId);
                log.info("call wechat api 'getUser' success");
                return user.getName();
            });
        } catch (WeComException e) {
            if (e.getErrcode().equals(60111)) {
                log.warn("wechat not find user,user does not exist or has resigned, user id is " + userId, e);
                return "[已离职用户]";
            }
            throw e;
        } catch (Exception e) {
            log.error("call wechat api 'createInstance' failed，message：" + e.getMessage());
            throw e;
        }
    }

    public String createInstance(final String templateId, final String userId, final List<ContentDataValue> dataValues,//
                                 final List<TmpControl<? extends ControlConfig>> controls, final List<Summary> summaryList) {
        try {
            return this.client.retryCall(client -> {
                ProcessApplyRequest request = ProcessApplyRequest.backendMode(userId, templateId, controls, dataValues, summaryList);
                GenericResponse<String> response = this.client.getApprovalApi().applyEvent(request);
                log.info("call wechat api 'createInstance' success");
                return response.getData();
            });
        } catch (Exception e) {
            log.error("call wechat api 'createInstance' failed，message：" + e.getMessage());
            throw e;
        }
    }

    public String getUserIdByPhone(final String phone) {
        try {
            return this.client.retryCall(client -> {
                GenericResponse<String> response = this.client.getUserApi().getUserIdByMobile(new Mobile(phone));
                log.info("call wechat api 'getUserIdByPhone' success");
                return response.getData();
            });
        } catch (WeComException e) {
            log.error("call wechat api 'getUserIdByPhone' failed，message: " + e.getMessage());
            if (e.getErrcode().equals(46004)) {
                throw ThirdPartyApiException.as().with(e, WechatI18nKey2.WECHAT_NOT_FIND_USER_BY_PHONE, phone);
            }
            throw e;
        }
    }

    public ApprovalDetail getInstanceDetail(final String instanceId) {
        try {
            return this.client.retryCall(client -> {
                GenericResponse<ApprovalDetail> detail = this.client.getApprovalApi().getApprovalDetail(ApprovalSpNo.from(instanceId));
                log.info("call wechat api 'getInstanceDetail' success");
                return detail.getData();
            });
        } catch (WeComException e) {
            log.error("call wechat api 'getInstanceDetail' failed，message: " + e.getMessage());
            if (e.getErrcode().equals(301026)) {
                throw ThirdPartyApiException.as().with(e, WechatI18nKey2.WECHAT_NOT_FIND_APPROVAL_INSTANCE, instanceId);
            }
            throw e;
        }
    }

    public List<ApprovalTemplate> getTemplates(String templateList, String templateLang) {
        if (StringUtils.isEmpty(templateList)) {
            return Collections.emptyList();
        }

        int cnt = 0;
        Map<String, List<ApprovalTemplate>> groupByLang = new LinkedHashMap<>();
        Map<String, List<ApprovalTemplate>> groupByCode = new LinkedHashMap<>();
        for (String code : templateList.split(",")) {
            if (StringUtils.isEmpty(code)) {
                continue;
            }

            ApprovalTmpDetailResponse template = this.getTemplate(code);;
            for (ApprovalTitle t : template.getTemplateNames()) {
                if (StringUtils.isBlank(t.getText())) {
                    continue;
                }

                List<ApprovalTemplate> byLang = groupByLang.computeIfAbsent(t.getLang(), s -> new ArrayList<>());
                List<ApprovalTemplate> byCode = groupByCode.computeIfAbsent(code, s -> new ArrayList<>());

                ApprovalTemplate tempForLang = new ApprovalTemplate();
                tempForLang.setTemplateIdentity(code);
                tempForLang.setApproTemplateName(t.getText());

                ApprovalTemplate tempForCode = new ApprovalTemplate();
                tempForCode.setTemplateIdentity(code);
                tempForCode.setApproTemplateName(t.getText() + " [" + t.getLang() + "]");

                byLang.add(tempForLang);
                byCode.add(tempForCode);
                cnt++;
            }
        }

        if (cnt == 0) {
            return Collections.emptyList();
        } else if (groupByLang.size() == 1) {
            return groupByLang.entrySet().iterator().next().getValue();
        } else {
            if (StringUtils.isNotBlank(templateLang)) {
                return groupByLang.get(templateLang);
            } else {
                return groupByLang.entrySet().iterator().next().getValue();
            }
        }
    }

    public ApprovalTmpDetailResponse getTemplate(final String code) {
        try {
            return this.client.retryCall(client -> {
                ApprovalTmpDetailResponse templateDetail = client.getApprovalApi().getTemplateDetail(TemplateId.of(code));
                log.info("call wechat api 'getTemplateDetail' success");
                return templateDetail;
            });
        } catch (WeComException e) {
            log.error("call wechat api 'getTemplateDetail' failed，message: " + e.getMessage());
            if (e.getErrcode().equals(301025)) {
                throw ThirdPartyApiException.as().with(e, ThirdPartyApiErrorType.APPROVAL_TEMPLATE_NOT_EXISTS, WechatI18nKey2.WECHAT_APPROVAL_TEMPLATE_NOT_EXISTS, code);
            }
            throw e;
        }
    }

    public String getLoginUrl(String status, String jumpUrl) throws UnsupportedEncodingException {
        return "https://open.weixin.qq.com/connect/oauth2/authorize?" +//
               "appid=" + URLEncoder.encode(this.getClient().getAgentDetails().getCorpId(), "UTF-8") +//
               "&response_type=code" +//
               "&scope=snsapi_privateinfo" +//
               "&state=" + URLEncoder.encode(status, "UTF-8") +//
               "&agentid=" + URLEncoder.encode(this.getClient().getAgentDetails().getAgentId(), "UTF-8") +//
               "&redirect_uri=" + URLEncoder.encode(jumpUrl, "UTF-8") +//
               "#wechat_redirect";
    }

    public String getUserTicket(String authCode) {
        try {
            String accessToken = this.client.getAccessToken();

            //https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?access_token=ACCESS_TOKEN&code=CODE
            String tokenEndpoint = "https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?" +//
                                   "access_token=" + URLEncoder.encode(accessToken, "UTF-8") +//
                                   "&code=" + URLEncoder.encode(authCode, "UTF-8");
            OkHttpClient httpClient = this.getClient().getHttpClient();
            Request request = new Request.Builder().url(tokenEndpoint).get().build();
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw ThirdPartyApiException.as().with(WechatI18nKey2.WECHAT_API_USERTICKET_ERROR, response.code() + ":" + response.body().string());
            }
            JSONObject object = JSONObject.parseObject(response.body().string());
            int errcode = object.getIntValue("errcode");
            if (errcode != 0) {
                throw ThirdPartyApiException.as().with(WechatI18nKey2.WECHAT_API_USERTICKET_ERROR, object.getString("errmsg"));
            }

            // test user role
            String userTicket = object.getString("user_ticket"); // OPENID or USERID
            if (StringUtils.isNotBlank(userTicket)) {
                return userTicket;
            } else {
                throw ThirdPartyApiException.as().with(WechatI18nKey2.WECHAT_IS_OPEN_USER_ERROR);
            }
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("wechat getUserinfo failed, " + e.getMessage());
            throw ThirdPartyApiException.as().with(e, WechatI18nKey2.WECHAT_API_USERTICKET_ERROR, e.getMessage());
        }
    }

    public UserData getUserInfo(String userTicket) {
        try {
            // see https://developer.work.weixin.qq.com/document/path/95833
            String tokenEndpoint = "https://qyapi.weixin.qq.com/cgi-bin/auth/getuserdetail?" +//
                                   "access_token=" + URLEncoder.encode(this.client.getAccessToken(), "UTF-8");
            String jsonData = JsonUtils.toJson(CollectionUtils.asMap("user_ticket", userTicket));

            MediaType jsonType = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(jsonType, jsonData);
            Request request = new Request.Builder().url(tokenEndpoint).post(body).build();
            OkHttpClient httpClient = this.getClient().getHttpClient();
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw ThirdPartyApiException.as().with(WechatI18nKey2.WECHAT_GETUSERINFO_ERROR, response.code() + ":" + response.body().string());
            }

            JSONObject object = JSONObject.parseObject(response.body().string());
            int errcode = object.getIntValue("errcode");
            if (errcode != 0) {
                throw ThirdPartyApiException.as().with(WechatI18nKey2.WECHAT_GETUSERINFO_ERROR, object.getString("errmsg"));
            }

            UserData userData = new UserData();
            userData.setExternalUID(object.getString("userid"));
            userData.setUserName(object.getString("userid"));
            if (StringUtils.isNotBlank(object.getString("biz_mail"))) {
                userData.setEmail(object.getString("biz_mail"));
            } else {
                userData.setEmail(object.getString("email"));
            }
            userData.setPhone(object.getString("mobile"));
            userData.setBindAccount(object.getString("userid"));
            userData.setAccessToken(userTicket);
            return userData;
        } catch (Exception e) {
            log.error("wechat getUserInfo failed, " + e.getMessage());
            throw ThirdPartyApiException.as().with(e, WechatI18nKey2.WECHAT_GETUSERINFO_ERROR, e.getMessage());
        }
    }
}
