package com.clougence.clouddm.team.provider.wechat.approval;

import java.util.Map;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;

import com.clougence.clouddm.team.provider.wechat.client.WechatApi;
import com.clougence.clouddm.team.provider.wechat.constants.approval.WechatConstant;
import com.clougence.clouddm.team.provider.wechat.utils.aes.WXBizJsonMsgCrypt;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.approval.ApprovalCallbackSpi;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalConsoleService;
import com.clougence.clouddm.sdk.service.approval.RdpApprovalTicketInfo;
import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.utils.StringUtils;

public class WechatApprovalCallbackSpi implements ApprovalCallbackSpi {

    private final static Logger             logger = LoggerUtil.getLoggerAppender();
    private final WechatApprovalProviderSpi sdkService;
    private final RdpApprovalConsoleService approvalService;

    public WechatApprovalCallbackSpi(WechatApprovalProviderSpi sdkService, RdpApprovalConsoleService approvalService){
        this.sdkService = sdkService;
        this.approvalService = approvalService;
    }

    @Override
    public Object handle(String ownerUid, Map<String, String> params) {
        WechatApi wechatApi = this.sdkService.wechatApi(ownerUid);
        String token = wechatApi.getToken();
        String encodingAesKey = wechatApi.getEncodingAesKey();
        String corpId = wechatApi.getClient().getAgentDetails().getCorpId();

        // check
        String encryptMsg = params.get(WechatConstant.CALLBACK_ECHOSTR);
        if (encryptMsg != null) {
            logger.info("wechat receive check msg");
            return new WXBizJsonMsgCrypt(token, encodingAesKey, corpId).VerifyURL(params.get(WechatConstant.CALLBACK_MSG_SIGNATURE),   //
                    params.get(WechatConstant.CALLBACK_TIMESTAMP),          //
                    params.get(WechatConstant.CALLBACK_NONCE), encryptMsg);
        }
        logger.info("wechat receive event msg,origin msg:" + params.get(WechatConstant.CALLBACK_REQUEST_BODY));

        //handle
        JSONObject requestBody = XML.toJSONObject(params.get(WechatConstant.CALLBACK_REQUEST_BODY));
        encryptMsg = requestBody.getJSONObject("xml").getString("Encrypt");

        String s = new WXBizJsonMsgCrypt(token, encodingAesKey, corpId).VerifyURL(params.get(WechatConstant.CALLBACK_MSG_SIGNATURE),   //
                params.get(WechatConstant.CALLBACK_TIMESTAMP),          //
                params.get(WechatConstant.CALLBACK_NONCE), encryptMsg);

        logger.info("wechat receive event msg, decrypt message:" + s);
        String spNo = readSpNumber(s);
        if (StringUtils.isBlank(spNo)) {
            logger.error("wechat receive event msg, spNo is null, data is " + s);
        }

        RdpApprovalTicketInfo approvalInstanceCallback = new RdpApprovalTicketInfo();
        approvalInstanceCallback.setProviderType(ApprovalProvider.Wechat.name());
        approvalInstanceCallback.setApproIdentity(spNo);
        approvalInstanceCallback.setOwnerUid(params.get(WechatConstant.PUID));
        approvalService.refreshTicket(approvalInstanceCallback);
        return true;
    }

    private static String readSpNumber(String s) {
        JSONObject jsonObject = XML.toJSONObject(s);
        jsonObject = jsonObject.getJSONObject("xml");
        JSONObject approvalJson = jsonObject.getJSONObject("ApprovalInfo");
        if (approvalJson != null) {
            return String.valueOf(approvalJson.getNumber("SpNo"));
        } else {
            return null;
        }
    }
}
