package com.clougence.clouddm.team.provider.feishu.constants;

import lombok.Getter;

/**
 * @author mode create time is 2025/02/13
 **/
@Getter
public enum FeishuConfigKey {

    ApprovalEnable("feishuEnableApprovalService"),
    ApprovalAppID("feishuApprovalAppID"),
    ApprovalAppSecret("feishuApprovalAppSecret"),
    ApprovalApiTimeoutSec("feishuApprovalApiTimeoutSec"),
    ApprovalTemplateList("feishuApprovalTemplateList"),

    LoginEnable("subAccountAuthType"),
    LoginAppID("feishuLoginAppID"),
    LoginAppSecret("feishuLoginAppSecret"),
    LoginApiTimeoutSec("feishuLoginApiTimeoutSec"),
    LoginRoleMap("feishuLoginRoleMap");

    private final String configKey;

    FeishuConfigKey(String configKey){
        this.configKey = configKey;
    }
}
