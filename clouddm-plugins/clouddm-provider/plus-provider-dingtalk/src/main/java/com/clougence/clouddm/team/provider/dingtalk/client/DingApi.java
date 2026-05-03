package com.clougence.clouddm.team.provider.dingtalk.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;

import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.dingtalkworkflow_1_0.models.*;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult;
import com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesResponseBody.ListUserVisibleBpmsProcessesResponseBodyResult;
import com.aliyun.dingtalkworkflow_1_0.models.ListUserVisibleBpmsProcessesResponseBody.ListUserVisibleBpmsProcessesResponseBodyResultProcessList;
import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastRequest.ProcessForecastRequestFormComponentValues;
import com.aliyun.dingtalkworkflow_1_0.models.ProcessForecastResponseBody.ProcessForecastResponseBodyResult;
import com.aliyun.dingtalkworkflow_1_0.models.StartProcessInstanceRequest.StartProcessInstanceRequestFormComponentValues;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.clougence.clouddm.team.provider.dingtalk.constants.DingI18nKeys;
import com.clougence.clouddm.team.provider.dingtalk.domain.ro.api.DingApiRO;
import com.clougence.clouddm.team.provider.dingtalk.domain.ro.api.DingUidRO;
import com.clougence.clouddm.team.provider.dingtalk.domain.ro.api.DingUserDetailRO;
import com.clougence.clouddm.sdk.approval.ApprovalForm;
import com.clougence.clouddm.sdk.approval.form.AuthForm;
import com.clougence.clouddm.sdk.approval.form.ChangeForm;
import com.clougence.clouddm.sdk.approval.form.QueryForm;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.service.config.UserData;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;

public final class DingApi {

    private final Logger     logger;
    private final DingClient client;

    DingApi(DingClient client){
        this.client = client;
        this.logger = client.getLogger();
    }

    public DingClient getClient() { return this.client; }

    public List<ListUserVisibleBpmsProcessesResponseBodyResultProcessList> getApprovalTemplates() throws Exception {
        try {
            return this.client.retryCall((client, token) -> {
                ListUserVisibleBpmsProcessesHeaders headers = new ListUserVisibleBpmsProcessesHeaders();
                List<ListUserVisibleBpmsProcessesResponseBodyResultProcessList> templates = new ArrayList<>();
                ListUserVisibleBpmsProcessesRequest request = new ListUserVisibleBpmsProcessesRequest();
                long nextOffset = 0;
                while (true) {
                    request.setMaxResults(100L).setNextToken(nextOffset);
                    headers.setXAcsDingtalkAccessToken(token);

                    ListUserVisibleBpmsProcessesResponse rep = client.getFlowClient().listUserVisibleBpmsProcessesWithOptions(request, headers, new RuntimeOptions());
                    ListUserVisibleBpmsProcessesResponseBodyResult result = rep.getBody().getResult();
                    if (result.getNextToken() == null) {
                        List<ListUserVisibleBpmsProcessesResponseBodyResultProcessList> processList = result.getProcessList();
                        templates.addAll(processList);
                        break;
                    } else {
                        nextOffset = result.getNextToken();
                        List<ListUserVisibleBpmsProcessesResponseBodyResultProcessList> processList = result.getProcessList();
                        templates.addAll(processList);
                    }
                }
                logger.info("call dingtalk paid api listUserVisibleBpmsProcessesWithOptions success");
                return templates;
            });
        } catch (Exception e) {
            logger.error("call dingtalk paid api listUserVisibleBpmsProcessesWithOptions failed, msg is " + e.getMessage(), e);
            throw e;
        }
    }

    public String createInstance(final ApprovalForm info, final String phone) throws Exception {
        try {
            return this.client.retryCall((client, token) -> {
                List<StartProcessInstanceRequestFormComponentValues> values;
                if (info instanceof QueryForm) {
                    values = DingApiUtils.getQueryFormParams((QueryForm) info);
                } else if (info instanceof AuthForm) {
                    values = DingApiUtils.getAuthFormParam((AuthForm) info);
                } else if (info instanceof ChangeForm) {
                    values = DingApiUtils.getChangeFormParam((ChangeForm) info);
                } else {
                    String message = String.format("Unsupported approval form type %s", info.getClass().getName());
                    throw ThirdPartyApiException.asRDP().with(ThirdPartyApiErrorType.OTHER, DingI18nKeys.DINGTALK_UNKNOWN_ERROR, message);
                }

                //
                String uidByPhone = this.getDingUidByPhone(client, token, phone);
                StartProcessInstanceRequest request = new StartProcessInstanceRequest().setOriginatorUserId(uidByPhone)
                    .setProcessCode(info.getTemplateIdentity())
                    .setDeptId(-1L)
                    .setFormComponentValues(values);

                //
                StartProcessInstanceHeaders headers = new StartProcessInstanceHeaders();
                headers.xAcsDingtalkAccessToken = token;
                StartProcessInstanceResponse response = client.getFlowClient().startProcessInstanceWithOptions(request, headers, new RuntimeOptions());
                logger.info("call dingtalk paid api startProcessInstanceWithOptions success");
                return response.body.getInstanceId();
            });
        } catch (TeaException e) {
            if (e.getCode().equals("processCodeError") || e.getCode().equals("processGetFailed")) {
                logger.error("call dingtalk paid api startProcessInstanceWithOptions failed, msg is " + e.getMessage(), e);
                throw ThirdPartyApiException.asRDP().with(e, ThirdPartyApiErrorType.APPROVAL_TEMPLATE_NOT_EXISTS, DingI18nKeys.DINGTALK_APPROVAL_TEMPLATE_NOT_EXISTS);
            }
            logger.error("call dingtalk paid api startProcessInstanceWithOptions failed, msg is " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("call dingtalk paid api startProcessInstanceWithOptions failed, msg is " + e.getMessage(), e);
            throw e;
        }
    }

    public GetProcessInstanceResponseBodyResult getApprovalDetail(final String instanceId) throws Exception {
        try {
            return this.client.retryCall((client, token) -> {
                GetProcessInstanceHeaders headers = new GetProcessInstanceHeaders();
                headers.xAcsDingtalkAccessToken = token;
                GetProcessInstanceRequest getProcessInstanceRequest = new GetProcessInstanceRequest().setProcessInstanceId(instanceId);

                GetProcessInstanceResponse options = client.getFlowClient().getProcessInstanceWithOptions(getProcessInstanceRequest, headers, new RuntimeOptions());
                logger.info("call dingtalk paid api getProcessInstanceWithOptions success");
                return options.getBody().getResult();
            });
        } catch (TeaException e) {
            if (e.getStatusCode().equals(400) && e.getCode().equals("processInstanceIdError")) {
                logger.error("dingtalk approval instance was deleted or not exists");
                throw ThirdPartyApiException.asRDP().with(DingI18nKeys.DINGTALK_APPROVAL_INSTANCE_NOT_EXISTS, "dingtalk approval instance was deleted or not exists");
            }
            logger.error("call dingtalk paid api getProcessInstanceWithOptions failed, msg is " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("call dingtalk paid api getProcessInstanceWithOptions failed, msg is " + e.getMessage(), e);
            throw e;
        }
    }

    public String getDingUidByPhone(final String phone) throws Exception {
        return this.client.retryCall((client, token) -> {
            return this.getDingUidByPhone(client, token, phone);
        });
    }

    private String getDingUidByPhone(final DingClient client, final String token, final String phone) throws Exception {
        try {
            OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
            req.setMobile(phone);

            OapiV2UserGetbymobileResponse rsp = client.getUserByMobileApiClient().execute(req, token);
            if (rsp.getErrcode() != 0) {
                throw new RuntimeException(rsp.getErrmsg());
            }
            DingUidRO dingUidRO = JsonUtils.toObj(rsp.getBody(), DingUidRO.class);
            DingApiRO.checkSuccess(dingUidRO);
            logger.info("call dingtalk api topapi/v2/user/getbymobile success");
            return dingUidRO.getDingUidInfo().getUserId();
        } catch (Exception e) {
            logger.error("call dingtalk api topapi/v2/user/getbymobile failed,phone is " + phone + ", msg is " + e.getMessage(), e);
            if (e.getMessage().contains("电话号码无效") || e.getMessage().contains("找不到该用户")) {
                throw ThirdPartyApiException.asRDP().with(DingI18nKeys.DINGTALK_USER_NOT_FIND, phone);
            }
            throw e;
        }
    }

    public ProcessForecastResponseBodyResult getApprovalActivities(final ApprovalForm instance) throws Exception {
        try {
            return this.client.retryCall((client, token) -> {
                List<ProcessForecastRequestFormComponentValues> values;
                if (instance instanceof QueryForm) {
                    values = DingApiUtils.getQueryActivityParams((QueryForm) instance);
                } else if (instance instanceof AuthForm) {
                    values = DingApiUtils.getAuthActivityParam((AuthForm) instance);
                } else if (instance instanceof ChangeForm) {
                    values = DingApiUtils.getChangeActivityParam((ChangeForm) instance);
                } else {
                    values = Collections.emptyList();
                }

                //
                String uidByPhone = this.getDingUidByPhone(client, token, instance.getTicketUserPhone());
                ProcessForecastRequest processForecastRequest = new ProcessForecastRequest().setProcessCode(instance.getTemplateIdentity())
                    .setDeptId(-1)
                    .setUserId(uidByPhone)
                    .setFormComponentValues(values);

                //
                ProcessForecastHeaders headers = new ProcessForecastHeaders();
                headers.xAcsDingtalkAccessToken = token;
                ProcessForecastResponse processForecastResponse = client.getFlowClient().processForecastWithOptions(processForecastRequest, headers, new RuntimeOptions());
                logger.info("call dingtalk paid api processForecastWithOptions success");
                return processForecastResponse.body.getResult();
            });
        } catch (TeaException e) {
            logger.error("call dingtalk paid api startProcessInstanceWithOptions failed, msg is " + e.getMessage(), e);
            if (e.getCode().equals("processCodeError") || e.getCode().equals("processGetFailed")) {
                throw ThirdPartyApiException.asRDP().with(ThirdPartyApiErrorType.APPROVAL_TEMPLATE_NOT_EXISTS, DingI18nKeys.DINGTALK_APPROVAL_TEMPLATE_NOT_EXISTS);
            }
            throw e;
        } catch (Exception e) {
            logger.error("call dingtalk paid api  processForecastWithOptions failed, msg is " + e.getMessage(), e);
            throw e;
        }
    }

    public DingUserDetailRO getUserInfoByUid(final String uid) throws Exception {
        if (StringUtils.equals(uid, "bpms_system")) {
            DingUserDetailRO ro = new DingUserDetailRO();
            ro.setRequestId("none");
            ro.setResultInfo(new DingUserDetailRO.ResultInfo());
            ro.getResultInfo().setUserName("bpms_system");
            return ro;
        }

        try {
            return this.client.retryCall((client, token) -> {
                OapiV2UserGetRequest req = new OapiV2UserGetRequest();
                req.setUserid(uid);
                OapiV2UserGetResponse rsp = client.getUserByUidApiClient().execute(req, token);
                DingUserDetailRO dingUidRO = JsonUtils.toObj(rsp.getBody(), DingUserDetailRO.class);
                DingApiRO.checkSuccess(dingUidRO);
                logger.info("call dingtalk api topapi/v2/user/get success");
                return dingUidRO;
            });
        } catch (Exception e) {
            logger.error("call dingtalk api topapi/v2/user/get error,msg is " + e.getMessage(), e);
            throw e;
        }
    }

    public void cancelApproval(final String uid, final String instanceId) throws Exception {
        try {
            this.client.retryCall((client, token) -> {
                TerminateProcessInstanceHeaders terminateProcessInstanceHeaders = new TerminateProcessInstanceHeaders();
                terminateProcessInstanceHeaders.xAcsDingtalkAccessToken = token;
                TerminateProcessInstanceRequest terminateProcessInstanceRequest = new TerminateProcessInstanceRequest().setProcessInstanceId(instanceId)
                    .setIsSystem(true)
                    .setOperatingUserId(uid);
                client.getFlowClient().terminateProcessInstanceWithOptions(terminateProcessInstanceRequest, terminateProcessInstanceHeaders, new RuntimeOptions());
                logger.info("call dingtalk paid api terminateProcessInstanceWithOptions success");
                return null;
            });
        } catch (TeaException e) {
            logger.error("call dingtalk paid api terminateProcessInstanceWithOptions error,msg is " + e.getMessage(), e);
            // if approval already close or not exists
            if (e.getCode().equals("aflowProcessInstStatusException") && e.getMessage().contains("提交审批后 15 秒内不允许撤销")) {
                throw ThirdPartyApiException.asRDP().with(DingI18nKeys.DINGTALK_APPROVAL_CLOSE_FAILED_TIME);
            } else if (e.getCode().equals("aflowProcessInstNotExist") || e.getCode().equals("aflowProcessInstStatusException")) {
                //
            } else {
                throw e;
            }
        } catch (Exception e) {
            logger.error("call dingtalk paid api terminateProcessInstanceWithOptions error,msg is " + e.getMessage(), e);
            throw e;
        }
    }

    public String getApprovalUrl(String processInstanceId) throws UnsupportedEncodingException {
        if (processInstanceId == null) {
            return null;
        }

        String redirectURL = "https://aflow.dingtalk.com/dingtalk/mobile/homepage.htm?" +//
                             "swfrom=oa&" +         //
                             "back=native&" +       //
                             "showmenu=false&" +    //
                             "dd_progress=false&" + //
                             "dd_share=false&" +    //
                             "dinghash=approval&" + //
                             "procInstId=" + processInstanceId;
        return "dingtalk://dingtalkclient/action/openapp?redirect_url=" + URLEncoder.encode(redirectURL, "UTF-8");
    }

    public String getLoginUrl(String status, String jumpUrl) throws Exception {
        return "https://login.dingtalk.com/oauth2/auth?" +  //
               "response_type=code" + //
               "&client_id=" + this.client.getAppKey() +//
               "&scope=openid" +//
               "&state=" + status +//
               "&redirect_uri=" + URLEncoder.encode(jumpUrl, "UTF-8") +//
               "&prompt=consent";
    }

    public String getUserAccessToken(String authCode) {
        if (StringUtils.isBlank(authCode)) {
            throw ThirdPartyApiException.asRDP().with(DingI18nKeys.DINGTALK_API_TOKEN_ERROR);
        }
        try {
            com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest userTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest()
                .setClientId(this.client.getAppKey())
                .setClientSecret(this.client.getAppSecret())
                .setCode(authCode)
                .setGrantType("authorization_code");
            GetUserTokenResponse userToken = this.client.getAuthClient().getUserToken(userTokenRequest);
            return userToken.getBody().getAccessToken();
        } catch (Exception e) {
            throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_UNKNOWN_ERROR, e.getMessage());
        }
    }

    public UserData getUserInfo(String accessToken) {
        try {
            com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders getUserHeaders = new com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders();
            getUserHeaders.xAcsDingtalkAccessToken = accessToken;
            GetUserResponseBody body = this.client.getConnectClient().getUserWithOptions("me", getUserHeaders, new RuntimeOptions()).getBody();
            UserData userData = new UserData();
            userData.setExternalUID(body.getUnionId());
            userData.setUserName(body.getNick());
            userData.setEmail(body.getEmail());
            userData.setPhone(body.getMobile());
            userData.setBindAccount(body.getUnionId());
            userData.setAccessToken(accessToken);
            return userData;
        } catch (Exception e) {
            throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_FETCH_USER_ERROR, e.getMessage());
        }
    }
}
