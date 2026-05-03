package com.clougence.rdp.component.sso.model.fo;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SsoRegisterAndLoginFO {

    @NotNull(message = "{notnull.state}")
    private String state;

    /**
     * feishu / wechat
     */
    private String code;

    /**
     * wechat
     */
    private String requestId;

    private String phone;

    private String verifyCode;

    private String company;

    /**
     * dingtalk
     */
    private String authCode;

    /**
     * google sso
     */
    private String accessToken;
}
