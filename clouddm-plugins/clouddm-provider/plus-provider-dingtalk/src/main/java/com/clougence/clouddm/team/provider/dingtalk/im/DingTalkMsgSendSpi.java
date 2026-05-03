package com.clougence.clouddm.team.provider.dingtalk.im;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.clougence.clouddm.sdk.messenger.*;
import com.clougence.clouddm.team.provider.dingtalk.constants.DingTalkI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.StringUtils;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DingTalkMsgSendSpi implements MsgSendSpi {

    @Override
    public String name() {
        return MsgProviderType.DingTalk.name();
    }

    @Override
    public String getServiceUrl() { return "https://www.dingtalk.com/"; }

    @Override
    public String getHelpUrl() { return "https://www.cdmgr.com/docs/devops/provider/devops_im_dingtalk"; }

    @SneakyThrows
    @Override
    public MsgSendResult sendMessage(MsgSendConfig config, MsgContent message) {
        String robotToken = null;
        try {
            if (StringUtils.isBlank(config.getWebhookUrl())) {
                throw ThirdPartyApiException.asRDP().with(DingTalkI18nKeys.DINGTALK_IM_CONFIG_MISSING_WEBHOOK_URL);
            }

            URL webHookUrl = new URL(config.getWebhookUrl());
            String query = webHookUrl.getQuery();
            Map<String, String> params = StringUtils.toMap(query, "&", "=");
            robotToken = params.get("access_token");
            if (StringUtils.isBlank(robotToken)) {
                throw ThirdPartyApiException.asRDP().with(DingTalkI18nKeys.DINGTALK_IM_CONFIG_BAD_WEBHOOK_URL);
            }
        } catch (Exception e) {
            if (e instanceof ThirdPartyApiException) {
                throw (ThirdPartyApiException) e;
            } else {
                throw ThirdPartyApiException.asRDP().with(e, DingTalkI18nKeys.DINGTALK_IM_CONFIG_ERROR, e.getMessage());
            }
        }

        try {
            long timestamp = System.currentTimeMillis();
            String secret = config.getSecret();
            String signPart = "";
            if (StringUtils.isNotBlank(secret)) {
                String stringToSign = timestamp + "\n" + secret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                String sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");
                signPart = "&sign=" + sign;
            }

            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?timestamp=" + timestamp + signPart);
            OapiRobotSendRequest req = null;
            switch (message.getType()) {
                case Text:
                    req = textMessage(message);
                    break;
                default:
                    throw ThirdPartyApiException.asRDP().with(DingTalkI18nKeys.DINGTALK_IM_UNSUPPORT_TYPE, message.getType());
            }

            OapiRobotSendResponse rsp = client.execute(req, robotToken);
            if (rsp.getErrcode() != 0) {
                return MsgSendResult.failed(message.getMessageId(), rsp.getErrmsg());
            } else {
                return MsgSendResult.success(message.getMessageId(), "OK.");
            }
        } catch (Exception e) {
            if (e instanceof ThirdPartyApiException) {
                throw (ThirdPartyApiException) e;
            } else {
                throw ThirdPartyApiException.asRDP().with(e, DingTalkI18nKeys.DINGTALK_IM_SEND_ERROR, e.getMessage());
            }
        }
    }

    private OapiRobotSendRequest textMessage(MsgContent message) {
        String body = message.getBody();
        body = StringUtils.replace(body, "<b>", " ");
        body = StringUtils.replace(body, "</b>", " ");

        OapiRobotSendRequest req = new OapiRobotSendRequest();
        OapiRobotSendRequest.Text test = new OapiRobotSendRequest.Text();
        test.setContent(body);

        //定义 @ 对象
        //OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        //at.setAtUserIds(Arrays.asList(USER_ID));

        //设置消息类型
        req.setMsgtype("text");
        req.setText(test);
        //req.setAt(at);
        return req;
    }
}
