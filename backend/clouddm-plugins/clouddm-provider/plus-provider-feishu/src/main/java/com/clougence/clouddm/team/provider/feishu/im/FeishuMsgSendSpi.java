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
package com.clougence.clouddm.team.provider.feishu.im;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.clougence.clouddm.sdk.messenger.*;
import org.apache.commons.codec.binary.Base64;

import com.clougence.clouddm.team.provider.feishu.constants.FeishuI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.lark.oapi.okhttp.*;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class FeishuMsgSendSpi implements MsgSendSpi {

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public String name() {
        return MsgProviderType.Feishu.name();
    }

    @Override
    public String getServiceUrl() { return "https://www.feishu.cn/"; }

    @Override
    public String getHelpUrl() { return "https://www.cdmgr.com/docs/devops/provider/devops_im_feishu"; }

    @Override
    @SneakyThrows
    public MsgSendResult sendMessage(MsgSendConfig config, MsgContent message) {
        if (StringUtils.isBlank(config.getWebhookUrl())) {
            throw ThirdPartyApiException.as().with(FeishuI18nKeys.FEISHU_IM_CONFIG_MISSING_WEBHOOK_URL);
        }

        try (Response response = client.newCall(createRequest(config, message)).execute()) {
            ResponseBody obj = JsonUtils.toObj(response.body().string(), ResponseBody.class);

            if (obj.code.equals("0")) {
                return MsgSendResult.success(message.getMessageId(), "OK.");
            } else {
                return MsgSendResult.failed(message.getMessageId(), obj.getMsg());
            }
        }
    }

    private Request createRequest(MsgSendConfig config, MsgContent msgContent) throws Exception {
        RequestContent content = new RequestContent();

        if (config.getSecret() != null) {
            long l = System.currentTimeMillis() / 1000;
            content.setTimestamp(String.valueOf(l));
            content.setSign(getSign(config.getSecret(), l));
        }
        String body = msgContent.getBody();

        body = StringUtils.replace(body, "<b>", " ");
        body = StringUtils.replace(body, "</b>", " ");

        content.setContent("{\"text\":\"" + body + "\"}");

        if (msgContent.getType() == MsgSendType.Text) {
            content.setMsg_type("text");
        } else {
            throw ThirdPartyApiException.as().with(FeishuI18nKeys.FEISHU_IM_UNSUPPORT_TYPE, msgContent.getType());
        }

        return new Request.Builder().url(config.getWebhookUrl()).method("POST", RequestBody.create(MediaType.get("application/json"), JsonUtils.toJson(content))).build();

    }

    private String getSign(String secret, long timestamp) throws Exception {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(new byte[] {});
        return new String(Base64.encodeBase64(signData));
    }

    @Getter
    @Setter
    private static class RequestContent {

        private String msg_type;
        private String content;
        private String timestamp;
        private String sign;
    }

    @Getter
    @Setter
    private static class ResponseBody {

        private String code;
        private String msg;
    }
}
