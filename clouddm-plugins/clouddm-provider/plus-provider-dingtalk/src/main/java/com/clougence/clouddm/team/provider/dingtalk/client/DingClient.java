package com.clougence.clouddm.team.provider.dingtalk.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaUnretryableException;
import com.aliyun.teaopenapi.models.Config;
import com.clougence.clouddm.team.provider.dingtalk.constants.DingI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.utils.RandomUtils;
import com.clougence.utils.ThreadUtils;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.taobao.api.ApiException;

import lombok.Getter;

@Getter
public class DingClient implements Closeable {

    private final Logger                           logger;
    private final String                           appKey;
    private final String                           appSecret;
    private final String                           roleMapping;
    private volatile String                        cacheToken;
    private final DingApi                          bindApi;

    private com.aliyun.dingtalkcontact_1_0.Client  contactClient;
    private com.aliyun.dingtalkoauth2_1_0.Client   authClient;
    private com.aliyun.dingtalkworkflow_1_0.Client flowClient;
    private DingTalkClient                         getByMobileApiClient;
    private DingTalkClient                         getByUidApiClient;

    public DingClient(Logger logger, String appKey, String appSecret){
        this.logger = (logger != null) ? logger : LoggerUtil.getLoggerAppender();
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.roleMapping = null;
        this.bindApi = new DingApi(this);
    }

    public DingClient(Logger logger, String appKey, String appSecret, String roleMapping){
        this.logger = (logger != null) ? logger : LoggerUtil.getLoggerAppender();
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.roleMapping = roleMapping;
        this.bindApi = new DingApi(this);
    }

    private Config getConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return config;
    }

    @Override
    public void close() {
        //
    }

    public com.aliyun.dingtalkcontact_1_0.Client getConnectClient() throws Exception {
        if (this.contactClient == null) {
            this.contactClient = new com.aliyun.dingtalkcontact_1_0.Client(getConfig());
        }
        return this.contactClient;
    }

    public com.aliyun.dingtalkoauth2_1_0.Client getAuthClient() throws Exception {
        if (this.authClient == null) {
            this.authClient = new com.aliyun.dingtalkoauth2_1_0.Client(getConfig());
        }
        return this.authClient;
    }

    public com.aliyun.dingtalkworkflow_1_0.Client getFlowClient() throws Exception {
        if (this.flowClient == null) {
            this.flowClient = new com.aliyun.dingtalkworkflow_1_0.Client(getConfig());
        }
        return this.flowClient;
    }

    public DingTalkClient getUserByMobileApiClient() {
        if (this.getByMobileApiClient == null) {
            this.getByMobileApiClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");
        }
        return this.getByMobileApiClient;
    }

    public DingTalkClient getUserByUidApiClient() {
        if (this.getByUidApiClient == null) {
            this.getByUidApiClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        }
        return this.getByUidApiClient;
    }

    public <T> T retryCall(DingApiCaller<T> caller) throws Exception {
        return this.callApi(caller, 0);
    }

    private <T> T callApi(DingApiCaller<T> caller, int retryCount) throws Exception {
        try {
            return caller.call(this, getToken());
        } catch (TeaUnretryableException e) {
            logger.error(e.getMessage());
            throw ThirdPartyApiException.asRDP().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, DingI18nKeys.DINGTALK_CONNECTION_ERROR);
        } catch (ApiException e) {
            if (e.getCause() instanceof IOException) {
                logger.error(e.getMessage());
                throw ThirdPartyApiException.asRDP().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, DingI18nKeys.DINGTALK_CONNECTION_ERROR);
            }
            throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_UNKNOWN_ERROR, e.getErrMsg());
        } catch (IOException e) {
            throw ThirdPartyApiException.asRDP().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, DingI18nKeys.DINGTALK_CONNECTION_ERROR);
        } catch (TeaException e) {
            if (e.statusCode.equals(403)) {
                if (e.getCode().equals("Forbidden.AccessDenied.ApiCountLimitForOrg")) {
                    throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_PAID_API_EXHAUSTION);
                } else if (e.getCode().equals("Forbidden.AccessDenied.AccessTokenPermissionDenied")) {
                    throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_ACCESS_PERMISSION_DENY);
                }
            }
            if (e.getStatusCode() == 500) {
                throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_SERVER_INNER_ERROR);
            }
            throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_UNKNOWN_ERROR, e.getMessage());
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            // for qps
            if (retryCount >= 5) {
                // handle as connect question,wait next execute
                if (e.getMessage().contains("qps流控")) {
                    throw ThirdPartyApiException.asRDP().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, DingI18nKeys.DINGTALK_QPS_EXHAUSTION);
                }
                throw e;
            }
            // token expire
            if (e.getMessage().contains("不合法的access_token")) {
                logger.error(e.getMessage(), e);
                this.cacheToken = null;
                return callApi(caller, retryCount + 1);
            }

            // request frequently
            if (e.getMessage().contains("qps流控")) {
                ThreadUtils.sleep(RandomUtils.nextInt(500, 2000), TimeUnit.MILLISECONDS);
                return callApi(caller, retryCount + 1);
            }

            throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_UNKNOWN_ERROR, e.getMessage());
        }
    }

    private String getToken() throws Exception {
        if (this.cacheToken == null) {
            synchronized (this) {
                if (this.cacheToken == null) {
                    this.cacheToken = this.getApiToken();
                }
            }
        }
        return this.cacheToken;
    }

    private <T extends GetAccessTokenRequest> T configRequest(T request) {
        request.setAppKey(this.appKey);
        request.setAppSecret(this.appSecret);
        return request;
    }

    public String getApiToken() throws Exception {
        try {
            GetAccessTokenRequest getAccessTokenRequest = this.configRequest(new GetAccessTokenRequest());
            GetAccessTokenResponse accessToken = this.getAuthClient().getAccessToken(getAccessTokenRequest);
            logger.info("call dingtalk api getAccessToken success");
            return accessToken.getBody().getAccessToken();
        } catch (TeaException e) {
            if (e.getCode().equals("invalidClientIdOrSecret")) {
                throw ThirdPartyApiException.asRDP().with(e, DingI18nKeys.DINGTALK_CONFIG_ERROR);
            }
            logger.error("call dingtalk api getAccessToken failed, msg is " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("call dingtalk api getAccessToken failed, msg is " + e.getMessage(), e);
            throw e;
        }
    }
}
