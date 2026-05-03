package com.clougence.rdp.component.alert.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpStatus;

import com.clougence.clouddm.base.metadata.rdp.enumeration.AlarmLevel;
import com.clougence.rdp.component.alert.RdpImAlertService;
import com.clougence.rdp.component.alert.model.SendMsgResult;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.RdpConsoleConfig;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.service.RdpAlertEventLogService;
import com.clougence.rdp.service.RdpSysConfigService;
import com.clougence.rdp.service.RdpUserConfigService;
import com.clougence.rdp.service.enumeration.AlertEventStatus;
import com.clougence.rdp.service.enumeration.AlertMediaType;
import com.clougence.rdp.util.HealthCheckInterceptor;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * @author bucketli 2021/9/30 09:49
 */
@Slf4j
public abstract class AbstractRdpImAlertService implements RdpImAlertService {

    public static final MediaType   JSON                   = MediaType.get("application/json; charset=utf-8");

    private final int               CONNECTION_POOL_SIZE   = 500;

    private final long              KEEP_ALIVE_TIME_MILLIS = 60000;

    @Resource
    @Setter
    protected RdpSysConfigService   rdpSysConfigService;

    @Resource
    @Setter
    protected RdpUserConfigService  rdpUserConfigService;

    @Resource
    @Setter
    protected RdpConsoleConfig      rdpConfig;

    @Resource
    private RdpAlertEventLogService rdpAlertEventLogService;

    private int                     ALARM_HTTP_TIMEOUT_MS  = 5000;

    protected abstract String fetchImAlertUrl();

    protected abstract String genParamsJsonStr(String signName, String msg, Map<String, Object> msgParams, List<RdpUserDO> users, boolean atAll);

    protected RdpUserKvBaseConfigDO fetchUserImAlertUrlConfig(String uid, AlarmLevel alarmLevel) {
        if (alarmLevel == AlarmLevel.Critical || alarmLevel == AlarmLevel.Blocker) {
            return rdpUserConfigService.getSpecifiedConfig(uid, UserDefinedConfig.Fields.criticalImAlertUrl);
        } else {
            return rdpUserConfigService.getSpecifiedConfig(uid, UserDefinedConfig.Fields.defaultImAlertUrl);
        }
    }

    protected Proxy queryProxyIfNecessary(String uid) {
        RdpUserKvBaseConfigDO proxyConfig = rdpUserConfigService.getSpecifiedConfig(uid, UserDefinedConfig.Fields.webHookProxyHost);
        if (proxyConfig == null || StringUtils.isBlank(proxyConfig.getConfigValue())) {
            return null;
        }

        return genProxyIfNecessary(proxyConfig.getConfigValue());
    }

    protected Proxy genProxyIfNecessary(String proxyAddr) {
        if (StringUtils.isBlank(proxyAddr)) {
            return null;
        }

        String[] host = proxyAddr.split(":");
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host[0], Integer.parseInt(host[1])));
    }

    @Override
    public SendMsgResult sendMsg(String signName, String msg, Map<String, Object> msgParams, RdpUserDO owner, List<RdpUserDO> receivers, AlarmLevel alarmLevel, boolean atAll) {
        checkParams(msg, owner);

        String paramStr = genParamsJsonStr(signName, msg, msgParams, receivers, atAll);
        List<String> sendUids = null;
        if (CollectionUtils.isNotEmpty(receivers)) {
            sendUids = receivers.stream().map(RdpUserDO::getUid).collect(Collectors.toList());
        }

        RdpUserKvBaseConfigDO userConfig = fetchUserImAlertUrlConfig(owner.getUid(), alarmLevel);
        Proxy proxy = queryProxyIfNecessary(owner.getUid());

        if (userConfig != null && StringUtils.isNotBlank(userConfig.getConfigValue())) {
            return post(userConfig.getConfigValue(), paramStr, proxy, sendUids);
        } else {
            return post(fetchImAlertUrl(), paramStr, proxy, sendUids);
        }
    }

    @Override
    public SendMsgResult sendMsgWithWebHook(String webHook, String proxyAddr, String signName, String msg, Map<String, Object> msgParams, RdpUserDO owner,
                                            List<RdpUserDO> receivers, AlarmLevel alarmLevel, boolean atAll) {
        checkParams(msg, owner);

        String paramStr = genParamsJsonStr(signName, msg, msgParams, receivers, atAll);
        List<String> sendUids = null;
        if (CollectionUtils.isNotEmpty(receivers)) {
            sendUids = receivers.stream().map(RdpUserDO::getUid).collect(Collectors.toList());
        }

        Proxy proxy = genProxyIfNecessary(proxyAddr);

        if (StringUtils.isNotBlank(webHook)) {
            return post(webHook, paramStr, proxy, sendUids);
        } else {
            return post(fetchImAlertUrl(), paramStr, proxy, sendUids);
        }
    }

    private void checkParams(String msg, RdpUserDO owner) {
        if (StringUtils.isBlank(msg)) {
            throw new IllegalArgumentException("MSG IS EMPTY when alert by aliyun dingtalk.");
        }

        if (owner == null) {
            throw new IllegalArgumentException("send msg to user is empty.");
        }
    }

    @Override
    public SendMsgResult sendMsgToOwner(String signName, String msg, Map<String, Object> msgParams, RdpUserDO owner, AlarmLevel alarmLevel, boolean atAll) {
        List<RdpUserDO> receivers = new ArrayList<>();
        receivers.add(owner);
        return sendMsg(signName, msg, msgParams, owner, receivers, alarmLevel, atAll);
    }

    @Override
    public SendMsgResult sendMsgToSys(String signName, String msg, Map<String, Object> msgParams, AlarmLevel alarmLevel, String uid, boolean atAll) {
        String paramStr = genParamsJsonStr(signName, msg, msgParams, null, atAll);
        Proxy proxy = queryProxyIfNecessary(uid);
        return post(fetchImAlertUrl(), paramStr, proxy, null);
    }

    //    protected String genContent(String signName, String msg) {
    //        if (rdpConfig.getDeployEnv() != null) {
    //            return signName + "【" + rdpConfig.getDeployEnv().name() + "】" + msg;
    //        } else {
    //            return signName + msg;
    //        }
    //    }

    protected SendMsgResult post(String url, String content, Proxy proxy, List<String> sendUids) {
        Response response = null;
        SendMsgResult result;
        try {
            if (StringUtils.isBlank(url)) {
                String errMsg = "[Rdp Im Alert Service] Alert failed because send url is empty.";
                log.error(errMsg);
                result = new SendMsgResult(false, errMsg, content, AlertMediaType.IM, sendUids);
                saveSendLog(result);
                return result;
            }

            if (StringUtils.isNotBlank(rdpConfig.getAlertImTimeoutMs())) {
                this.ALARM_HTTP_TIMEOUT_MS = Integer.parseInt(rdpConfig.getAlertImTimeoutMs());
            }

            OkHttpClient.Builder httpBuilder = new OkHttpClient().newBuilder()
                .addInterceptor(new HealthCheckInterceptor())
                .connectTimeout(ALARM_HTTP_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(CONNECTION_POOL_SIZE, KEEP_ALIVE_TIME_MILLIS, TimeUnit.MILLISECONDS))
                .writeTimeout(ALARM_HTTP_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .readTimeout(ALARM_HTTP_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            if (proxy != null) {
                httpBuilder.proxy(proxy);
                System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
            }

            OkHttpClient client = httpBuilder.build();

            RequestBody body = RequestBody.create(JSON, content);
            Request request = new Request.Builder().url(url).post(body).build();
            response = client.newCall(request).execute();
            if (httpResCheck(response, proxy)) {
                // check service code in response body if needed.
                ServiceResponse serviceRes = serviceResCheck(response);
                if (serviceRes.isSuccess()) {
                    log.info("[Rdp Im Alert Service] Im group chat robot msg send success");
                    result = new SendMsgResult(true, null, content, AlertMediaType.IM, sendUids);
                } else {
                    String msg = "[" + serviceRes.getServiceCode() + "] " + serviceRes.getServiceMsg();
                    log.error("[Rdp Im Alert Service] Im group chat robot msg send failed with Service Error.MsgContent: {}", msg);
                    result = new SendMsgResult(false, msg, content, AlertMediaType.IM, sendUids);
                }
            } else {
                log.error("[Rdp Im Alert Service] Im group chat robot msg send FAIL.MsgContent:" + response.message());
                result = new SendMsgResult(false, response.message(), content, AlertMediaType.IM, sendUids);
            }
        } catch (SSLHandshakeException ignore) {
            log.warn("[Rdp Im Alert Service] Proxy send post request maybe have some error in SSL , but not effect program");
            result = new SendMsgResult(true, null, content, AlertMediaType.IM, sendUids);
        } catch (Exception e) {
            String errMsg = "[Rdp Im Alert Service] Im group chat send ERROR.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
            result = new SendMsgResult(false, errMsg, content, AlertMediaType.IM, sendUids);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        saveSendLog(result);
        return result;
    }

    protected void saveSendLog(SendMsgResult result) {
        if (result == null) {
            return;
        }

        AlertEventStatus status = result.isSuccess() ? AlertEventStatus.SUCCESS : AlertEventStatus.ERROR;
        rdpAlertEventLogService.save(status, result.getContent(), result.getErrMsg(), result.getMediaType(), result.getSendUids());
    }

    protected String requireRespBodyIsNotBlank(Response response) throws IOException {
        ResponseBody respBody = response.body();
        String bodyString;
        if (respBody == null || StringUtils.isBlank(bodyString = respBody.string())) {
            throw new IOException("Response body is null");
        }
        return bodyString;
    }

    protected boolean httpResCheck(Response response, Proxy proxy) {
        return response.code() == HttpStatus.SC_OK || (proxy != null && response.code() == HttpStatus.SC_NO_CONTENT);
    }

    protected ServiceResponse serviceResCheck(Response response) throws IOException {
        return ServiceResponse.buildSuccess();
    }

    @Getter
    @Setter
    protected static class ServiceResponse {

        private boolean success = true;

        private String  serviceCode;

        private String  serviceMsg;

        private ServiceResponse(){
        }

        public static ServiceResponse buildSuccess() {
            return new ServiceResponse();
        }

        public static ServiceResponse buildFailure(String serviceCode, String serviceMsg) {
            ServiceResponse serviceResponse = new ServiceResponse();
            serviceResponse.success = false;
            serviceResponse.serviceCode = serviceCode;
            serviceResponse.serviceMsg = serviceMsg;
            return serviceResponse;
        }
    }
}
