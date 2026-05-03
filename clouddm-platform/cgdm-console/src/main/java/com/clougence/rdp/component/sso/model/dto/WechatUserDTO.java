package com.clougence.rdp.component.sso.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class WechatUserDTO {

    @JsonProperty(value = "nickname")
    private String nickName;

    @JsonProperty(value = "openid")
    private String openId;

    @JsonProperty(value = "unionid")
    private String unionId;
}
