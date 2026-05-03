package com.clougence.clouddm.team.provider.wechat.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.alibaba.fastjson2.JSONObject;
import com.clougence.clouddm.team.provider.wechat.constants.WechatI18nKey2;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiErrorType;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.utils.StringUtils;
import com.clougence.utils.timer.HashedWheelTimer;
import com.clougence.utils.timer.Timer;

import cn.felord.AgentDetails;
import cn.felord.DefaultAgent;
import cn.felord.WeComException;
import cn.felord.WeComTokenCacheable;
import cn.felord.api.ApprovalApi;
import cn.felord.api.AuthApi;
import cn.felord.api.UserApi;
import cn.felord.api.WorkWeChatApi;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Getter
public class WechatClient implements Closeable {

    private final Logger              logger;
    private final AgentDetails        agentDetails;
    private final String              roleMapping;
    private final WechatApi           bindApi;

    private final WorkWeChatApi       wechatApi;
    private final WeComTokenCacheable wechatCache;
    private UserApi                   userApi;
    private AuthApi                   authApi;
    private ApprovalApi               approvalApi;
    private final OkHttpClient        httpClient;
    private final Timer               timer;

    public WechatClient(Logger logger, String corpId, String agentId, String secret, String roleMapping){
        this.logger = (logger != null) ? logger : LoggerUtil.getLoggerAppender();
        this.agentDetails = DefaultAgent.of(corpId, secret, agentId);
        this.roleMapping = roleMapping;
        this.wechatCache = new WechatCache();
        this.wechatApi = new WorkWeChatApi(this.wechatCache);
        this.bindApi = new WechatApi(this);
        this.httpClient = new OkHttpClient.Builder().readTimeout(6, TimeUnit.SECONDS).build();
        this.timer = new HashedWheelTimer();
    }

    public WechatClient(Logger logger, String corpId, String agentId, String secret, String token, String encodingAesKey){
        this.logger = (logger != null) ? logger : LoggerUtil.getLoggerAppender();
        this.agentDetails = DefaultAgent.of(corpId, secret, agentId);
        this.roleMapping = SecSysRole.DEV_ROLE_NAME;
        this.wechatCache = new WechatCache();
        this.wechatApi = new WorkWeChatApi(this.wechatCache);
        this.bindApi = new WechatApi(this, token, encodingAesKey);
        this.httpClient = null;
        this.timer = new HashedWheelTimer();
    }

    public AgentDetails getAgentDetails() { return this.agentDetails; }

    public Logger getLogger() { return logger; }

    public UserApi getUserApi() {
        if (this.userApi == null) {
            this.userApi = this.wechatApi.contactBookManager(this.agentDetails).userApi();
        }
        return this.userApi;
    }

    public AuthApi getAuthApi() {
        if (this.authApi == null) {
            this.authApi = this.wechatApi.authApi(this.agentDetails);
        }
        return this.authApi;
    }

    public ApprovalApi getApprovalApi() {
        if (this.approvalApi == null) {
            this.approvalApi = this.wechatApi.approvalApi(this.agentDetails);
        }
        return this.approvalApi;
    }

    @Override
    public void close() throws IOException {
        WechatClientKiller.close(this.logger, this.httpClient);
    }

    public WechatApi getBindApi() { return this.bindApi; }

    public String getAccessToken() {
        String corpId = this.agentDetails.getCorpId();
        String secret = this.agentDetails.getSecret();
        String token = this.wechatCache.getAccessToken(corpId, secret);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }

        try {
            // see https://developer.work.weixin.qq.com/document/path/91039
            String tokenEndpoint = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?" +//
                                   "corpid=" + URLEncoder.encode(corpId, "UTF-8") +//
                                   "&corpsecret=" + URLEncoder.encode(secret, "UTF-8");

            OkHttpClient httpClient = this.httpClient;
            Request request = new Request.Builder().url(tokenEndpoint).get().build();
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw ThirdPartyApiException.asRDP().with(WechatI18nKey2.WECHAT_API_TOKEN_ERROR, response.code() + ":" + response.body().string());
            }

            JSONObject object = JSONObject.parseObject(response.body().string());
            String accessToken = object.getString("access_token");
            this.wechatCache.putAccessToken(corpId, secret, accessToken);

            //
            int expiresSec = object.getIntValue("expires_in");
            timer.newTimeout(t -> {
                this.wechatCache.clearAccessToken(corpId, secret);
            }, expiresSec, TimeUnit.SECONDS);

            return accessToken;
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_API_TOKEN_ERROR, e.getMessage());
        }
    }

    public <T> T retryCall(WechatApiCaller<T> caller) {
        return retryCall(caller, 1);
    }

    private <T> T retryCall(WechatApiCaller<T> caller, int count) {
        try {
            return caller.call(this);
        } catch (WeComException e) {
            if (e.getCause() instanceof IOException) {
                throw ThirdPartyApiException.asRDP().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, WechatI18nKey2.WECHAT_CALL_API_NETWORK_ERROR);
            }

            switch (e.getErrcode()) {
                case 40014: {
                    if (count >= 5) {
                        throw ThirdPartyApiException.asRDP().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, WechatI18nKey2.WECHAT_CALL_API_NETWORK_ERROR);
                    }

                    this.wechatCache.clearAccessToken(this.agentDetails.getCorpId(), this.agentDetails.getAgentId());
                    return retryCall(caller, count + 1);
                }
                case 40001:
                case 40013: {
                    throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_CONFIG_ERROR);
                }
                case 301055: {
                    throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_APP_NO_APPROVAL_ACCESS);
                }
                case 45009: {
                    // too frequence
                    throw ThirdPartyApiException.asRDP().with(e, ThirdPartyApiErrorType.CONNECTION_ERROR, WechatI18nKey2.WECHAT_REQUEST_TOO_FREQUENT);
                }
                case 60020: {
                    throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_SOURCE_IP_NOT_SAFE);
                }
            }

            throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_CALL_API_UNKNOWN_ERROR, e.getMessage());
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            throw ThirdPartyApiException.asRDP().with(e, WechatI18nKey2.WECHAT_CALL_API_UNKNOWN_ERROR, e.getMessage());
        }
    }
}
