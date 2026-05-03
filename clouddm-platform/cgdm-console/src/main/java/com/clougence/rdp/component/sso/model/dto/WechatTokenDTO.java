package com.clougence.rdp.component.sso.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class WechatTokenDTO {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "openid")
    private String openId;
}
