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
package com.clougence.clouddm.team.provider.feishu.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;

import com.clougence.clouddm.sdk.approval.ApprovalForm;
import com.clougence.clouddm.sdk.approval.ApprovalInstanceCancelInfo;
import com.clougence.clouddm.sdk.approval.ApprovalTemplate;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.clouddm.team.provider.feishu.constants.FeishuI18nKeys2;
import com.clougence.clouddm.team.provider.feishu.domain.mo.FeishuTemplateInfo;
import com.clougence.clouddm.team.provider.feishu.domain.mo.FeishuWidget;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.response.BaseResponse;
import com.lark.oapi.service.approval.v4.model.*;
import com.lark.oapi.service.authen.v1.model.*;
import com.lark.oapi.service.contact.v3.model.*;

public class FeishuApi {

    private static final JavaType apiFeishuWidgetListType = JsonUtils.defaultObjectMapper().getTypeFactory().constructCollectionType(List.class, FeishuWidget.class);

    private final Logger          logger;
    private final FeishuClient    client;
    private static final String   USER_ID_TYPE            = "user_id";
    private final String          roleMap;

    public FeishuApi(FeishuClient client, String roleMap){
        this.logger = client.getLogger();
        this.client = client;
        this.roleMap = roleMap;
    }

    public String getRoleMap() { return this.roleMap; }

    public List<ApprovalTemplate> getTemplates(String templateList) throws ThirdPartyApiException {
        if (StringUtils.isEmpty(templateList)) {
            return Collections.emptyList();
        }

        List<ApprovalTemplate> list = new ArrayList<>();
        for (String code : templateList.split(",")) {
            if (StringUtils.isBlank(code)) {
                continue;
            }
            String template;
            try {
                template = this.client.callApi(client -> {
                    // 创建请求对象
                    GetApprovalReq req = GetApprovalReq.newBuilder().approvalCode(code.trim()).build();

                    GetApprovalResp resp = client.apiClient().approval().approval().get(req);
                    checkSuccess(resp, "View the specified approval definition", true);
                    return resp.getData().getApprovalName();
                });
            } catch (CallApiException e) {
                // code template not exists
                if (e.getCode() == 1390002 || e.getCode() == 99991663) {
                    throw ThirdPartyApiException.as().with(e, FeishuI18nKeys2.FEISHU_TEMPLATE_CODE_ERROR, code);
                }
                throw e;
            }

            ApprovalTemplate approvalTmp = new ApprovalTemplate();
            approvalTmp.setTemplateIdentity(code);
            approvalTmp.setApproTemplateName(template);
            list.add(approvalTmp);
        }

        return list;
    }

    public String getUserIdByPhone(final String phone) {
        return this.client.callApi(client -> {
            BatchGetIdUserReq req = BatchGetIdUserReq.newBuilder()
                .userIdType(USER_ID_TYPE)
                .batchGetIdUserReqBody(BatchGetIdUserReqBody.newBuilder().mobiles(new String[] { phone }).includeResigned(true).build())
                .build();

            BatchGetIdUserResp resp = client.apiClient().contact().user().batchGetId(req);
            checkSuccess(resp, "getUserIdByPhone", false);

            UserContactInfo userContactInfo = resp.getData().getUserList()[0];
            if (userContactInfo.getUserId() == null) {
                throw ThirdPartyApiException.as().with(FeishuI18nKeys2.FEISHU_NOT_FIND_USER_BY_PHONE);
            }
            return userContactInfo.getUserId();
        });
    }

    public FeishuTemplateInfo getApprovalActivities(final String identity) {
        return this.client.callApi(client -> {
            GetApprovalReq req = GetApprovalReq.newBuilder().approvalCode(identity).build();
            GetApprovalResp resp = client.apiClient().approval().approval().get(req);

            checkSuccess(resp, "View the specified approval definition", true);
            String formJson = resp.getData().getForm();
            FeishuTemplateInfo info = new FeishuTemplateInfo();
            info.setWidgets(JsonUtils.defaultObjectMapper().readValue(formJson, apiFeishuWidgetListType));
            info.setNodeInfoList(new ArrayList<>());

            for (int i = 0; i < resp.getData().getNodeList().length - 2; i++) {
                info.getNodeInfoList().add(0, resp.getData().getNodeList()[i]);
            }

            return info;
        });
    }

    public String createInstance(final ApprovalForm info, final String form, final String userid) {
        return this.client.callApi(client -> {
            CreateInstanceReq req = CreateInstanceReq.newBuilder()
                .instanceCreate(InstanceCreate.newBuilder().approvalCode(info.getTemplateIdentity()).userId(userid).form(form).build())
                .build();

            CreateInstanceResp resp = client.apiClient().approval().instance().create(req);

            checkSuccess(resp, "Create approval instance", true);
            return resp.getData().getInstanceCode();
        });
    }

    public void cancelInstance(final ApprovalInstanceCancelInfo info, final String userId) {
        this.client.callApi(client -> {
            // 创建请求对象
            CancelInstanceReq req = CancelInstanceReq.newBuilder()
                .userIdType(USER_ID_TYPE)
                .instanceCancel(InstanceCancel.newBuilder().instanceCode(info.getApprovalInstanceIdentity()).approvalCode(info.getApprovalTemplateCode()).userId(userId).build())
                .build();

            // 发起请求
            CancelInstanceResp resp = client.apiClient().approval().instance().cancel(req);

            checkSuccess(resp, "Withdrawal of approval instance", true);
            return null;
        });
    }

    public GetInstanceRespBody getLastInfo(final String templateIdentity) {
        return this.client.callApi(client -> {
            GetInstanceReq req = GetInstanceReq.newBuilder().instanceId(templateIdentity).build();
            GetInstanceResp resp = client.apiClient().approval().instance().get(req);

            checkSuccess(resp, "Approval instance details", true);
            return resp.getData();
        });
    }

    public String getUserInfoById(final String userId) {
        try {
            return this.client.callApi(client -> {
                GetUserReq req = GetUserReq.newBuilder().userId(userId).userIdType(USER_ID_TYPE).build();
                GetUserResp resp = client.apiClient().contact().user().get(req);
                checkSuccess(resp, "Get user info by user id", false);

                return resp.getData().getUser().getName();
            });
        } catch (CallApiException e) {
            if (e.getCode() == 41012) {
                logger.warn("feishu not find user,user does not exist or has resigned, user id is " + userId, e);
                return "[已离职用户]";
            } else {
                throw e;
            }
        }
    }

    private void checkSuccess(final BaseResponse response, final String apiName, final boolean paid) {
        if (!response.success()) {
            logger.error("call feishu api failed: '" + apiName + "'", response.getMsg());
            throw new CallApiException(response.getCode(), response.getMsg());
        }

        if (paid) {
            logger.info("call feishu paid api success: '" + apiName + "'");
        } else {
            logger.info("call feishu api success: '" + apiName + "'");
        }
    }

    public String getPcUrl(final String processInstanceId) {
        if (processInstanceId == null) {
            return null;
        }
        return "https://applink.feishu.cn/client/mini_program/open?" +//
               "appId=cli_9cb844403dbb9108" +// this app id is Feishu built-in app ID (审批 应用)
               "&mode=appCenter" +//
               "&path=pc%2Fpages%2Fin-process%2Findex%3FenableTrusteeship%3Dtrue%26instanceId%3D" + processInstanceId + "%26source%3Dapproval_bot" +//
               "&relaunch=true";
    }

    public String getLoginUrl(String status, String jumpUrl) throws UnsupportedEncodingException {
        return "https://passport.feishu.cn/suite/passport/oauth/authorize?" +//
               "response_type=code" +   //
               "&state=" + URLEncoder.encode(status, "UTF-8") + //
               "&client_id=" + this.client.getAppId() + //
               "&redirect_uri=" + URLEncoder.encode(jumpUrl, "UTF-8");
    }

    public String getUserAccessToken(String authCode) {
        if (StringUtils.isBlank(authCode)) {
            throw ThirdPartyApiException.as().with(FeishuI18nKeys2.FEISHU_API_TOKEN_ERROR);
        }
        return this.client.callApi(client -> {
            CreateOidcAccessTokenReq req = CreateOidcAccessTokenReq.newBuilder()
                .createOidcAccessTokenReqBody(CreateOidcAccessTokenReqBody.newBuilder().grantType("authorization_code").code(authCode).build())
                .build();
            CreateOidcAccessTokenResp resp = this.client.apiClient().authen().oidcAccessToken().create(req);
            return resp.getData().getAccessToken();
        });
    }

    public UserData getUserInfo(String accessToken) {
        return this.client.callApi(client -> {
            GetUserInfoResp resp = client.apiClient().authen().userInfo().get(RequestOptions.newBuilder().userAccessToken(accessToken).build());
            GetUserInfoRespBody data = resp.getData();
            UserData userData = new UserData();
            userData.setExternalUID(data.getUnionId());
            userData.setUserName(data.getName());
            if (StringUtils.isNotBlank(data.getEnterpriseEmail())) {
                userData.setEmail(data.getEnterpriseEmail());
            } else {
                userData.setEmail(data.getEmail());
            }
            userData.setPhone(data.getMobile());
            userData.setBindAccount(data.getUnionId());
            userData.setAccessToken(accessToken);
            return userData;
        });
    }

    public String subscribeTemplate(String templateId) {
        if (StringUtils.isBlank(templateId)) {
            return null;
        }
        return this.client.callApi(client -> {
            SubscribeApprovalReq req = SubscribeApprovalReq.newBuilder().approvalCode(templateId).build();
            SubscribeApprovalResp resp = client.apiClient().approval().v4().approval().subscribe(req);
            if (!resp.success()) {
                if (resp.getCode() == 1390007) {
                    return "ok."; // templateId subscription existed.
                } else if (resp.getCode() == 1390002) {
                    return "ok."; // templateId approval code not found.
                } else {
                    logger.error("call subscribeTemplate failed of " + templateId + " with error " + resp.getCode() + resp.getMsg());
                    throw new CallApiException(resp.getCode(), resp.getMsg());
                }
            } else {
                logger.info("call subscribeTemplate finish of " + templateId);
                return "ok.";
            }
        });
    }

    public void unSubscribeTemplate(String templateId) {
        if (StringUtils.isBlank(templateId)) {
            return;
        }
        this.client.callApi(client -> {
            UnsubscribeApprovalReq req = UnsubscribeApprovalReq.newBuilder().approvalCode(templateId).build();
            UnsubscribeApprovalResp resp = client.apiClient().approval().v4().approval().unsubscribe(req);
            if (!resp.success()) {
                if (resp.getCode() == 1390007) {
                    return "ok."; // templateId subscription existed.
                } else if (resp.getCode() == 1390002) {
                    return "ok."; // templateId approval code not found.
                } else if (resp.getCode() == 60008) {
                    return "ok."; // subscription not found.
                } else {
                    logger.error("call unSubscribeTemplate failed of " + templateId + " with error " + resp.getCode() + resp.getMsg());
                    throw new CallApiException(resp.getCode(), resp.getMsg());
                }
            } else {
                logger.info("call unSubscribeTemplate finish of " + templateId);
            }
            return "";
        });
    }
}
