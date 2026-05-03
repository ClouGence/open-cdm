package com.clougence.clouddm.team.provider.dingtalk.constants;

import lombok.Getter;

/**
 * @author mode create time is 2025/02/13
 **/
@Getter
public enum DingConfigKey {

    ApprovalEnable("dingEnableApprovalService"),
    ApprovalClientId("dingApprovalConfigAk"),
    ApprovalClientSecret("dingApprovalConfigSk"),

    LoginEnable("subAccountAuthType"),
    LoginClientId("dingLoginConfigAk"),
    LoginClientSecret("dingLoginConfigSk"),
    LoginRoleMap("dingLoginRoleMap");

    private final String configKey;

    DingConfigKey(String configKey){
        this.configKey = configKey;
    }
}
