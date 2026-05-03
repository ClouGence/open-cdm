package com.clougence.clouddm.team.provider.wechat.constants;

import lombok.Getter;

/**
 * @author mode create time is 2025/02/13
 **/
@Getter
public enum WechatConfigKey {

    ApprovalEnable("wechatEnableApprovalService"),
    ApprovalCorpId("wechatApprovalCorpId"),
    ApprovalSecret("wechatApprovalSecret"),
    ApprovalAgentId("wechatApprovalAgentId"),
    ApprovalToken("wechatApprovalToken"),
    ApprovalEncodingAesKey("wechatApprovalEncodingAesKey"),
    ApprovalTemplateList("wechatApprovalTemplateList"),
    ApprovalTemplateLang("wechatApprovalTemplateLang"),

    LoginEnable("subAccountAuthType"),
    LoginCorpId("wechatLoginCorpId"),
    LoginAgentId("wechatLoginAgentId"),
    LoginSecret("wechatLoginSecret"),
    LoginRoleMap("wechatLoginRoleMap"),;

    private final String configKey;

    WechatConfigKey(String configKey){
        this.configKey = configKey;
    }
}
