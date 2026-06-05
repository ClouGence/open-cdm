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
package com.clougence.clouddm.team.provider.wechat.approval;

import java.util.Map;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;

import com.clougence.clouddm.sdk.LoggerUtil;
import com.clougence.clouddm.sdk.approval.ApprovalCallbackSpi;
import com.clougence.clouddm.sdk.approval.ApprovalProvider;
import com.clougence.clouddm.sdk.service.approval.ApprovalIdentity;
import com.clougence.clouddm.sdk.service.approval.ApprovalRefreshService;
import com.clougence.clouddm.team.provider.wechat.client.WechatApi;
import com.clougence.clouddm.team.provider.wechat.constants.approval.WechatConstant;
import com.clougence.clouddm.team.provider.wechat.utils.aes.WXBizJsonMsgCrypt;
import com.clougence.utils.StringUtils;

public class WechatApprovalCallbackSpi implements ApprovalCallbackSpi {

    private final static Logger             logger = LoggerUtil.getLoggerAppender();
    private final WechatApprovalProviderSpi sdkService;
    private final ApprovalRefreshService    approvalService;

    public WechatApprovalCallbackSpi(WechatApprovalProviderSpi sdkService, ApprovalRefreshService approvalService){
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

        ApprovalIdentity approvalInstanceCallback = new ApprovalIdentity();
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
