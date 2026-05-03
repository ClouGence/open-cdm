package com.clougence.clouddm.team.provider.wechat.im;

import com.clougence.clouddm.sdk.messenger.*;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.team.provider.wechat.constants.WechatI18nKeys;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import okhttp3.*;

public class WechatMsgSendSpi implements MsgSendSpi {

    private final OkHttpClient client   = new OkHttpClient();
    private final String       TEMPLATE = "{\"msgtype\": \"{{TYPE}}\",\"text\": {\"content\": \"{{CONTENT}}\" } }";

    @Override
    public String name() {
        return MsgProviderType.Wechat.name();
    }

    @Override
    public String getServiceUrl() { return "https://work.weixin.qq.com/"; }

    @Override
    public String getHelpUrl() { return "https://www.cdmgr.com/docs/devops/provider/devops_im_wechat"; }

    @Override
    @SneakyThrows
    public MsgSendResult sendMessage(MsgSendConfig config, MsgContent message) {
        if (StringUtils.isBlank(config.getWebhookUrl())) {
            throw ThirdPartyApiException.asRDP().with(WechatI18nKeys.WECHAT_IM_CONFIG_MISSING_WEBHOOK_URL);
        }
        try (Response response = client.newCall(createRequest(config, message)).execute()) {
            ResponseBody obj = JsonUtils.toObj(response.body().string(), ResponseBody.class);

            if (obj.errcode.equals("0")) {
                return MsgSendResult.success(message.getMessageId(), "OK.");
            } else {
                return MsgSendResult.failed(message.getMessageId(), obj.getErrmsg());
            }
        }
    }

    private Request createRequest(MsgSendConfig config, MsgContent msgContent) {
        String body = msgContent.getBody();

        body = StringUtils.replace(body, "<b>", " ");
        body = StringUtils.replace(body, "</b>", " ");

        String replace = TEMPLATE.replace("{{CONTENT}}", body);

        if (msgContent.getType() == MsgSendType.Text) {
            replace = replace.replace("{{TYPE}}", "text");
        } else {
            throw ThirdPartyApiException.asRDP().with(WechatI18nKeys.WECHAT_IM_UNSUPPORT_TYPE, msgContent.getType());
        }

        return new Request.Builder().url(config.getWebhookUrl()).method("POST", RequestBody.create(MediaType.get("application/json"), replace)).build();
    }

    @Getter
    @Setter
    private static class ResponseBody {

        private String errcode;
        private String errmsg;
    }
}
