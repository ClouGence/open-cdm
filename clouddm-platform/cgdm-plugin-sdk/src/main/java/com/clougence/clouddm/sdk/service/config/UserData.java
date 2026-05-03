package com.clougence.clouddm.sdk.service.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2021/1/5
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {

    private String internalUID;
    private String externalUID;
    private String userName;
    private String email;
    private String phone;
    private String subAccount;
    private String userDomain;
    private Long   roleId;
    private String bindAccount;
    private String accessToken; // AccessToken or IdToken
}
