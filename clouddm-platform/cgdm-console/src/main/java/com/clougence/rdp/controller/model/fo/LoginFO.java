package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import com.clougence.rdp.controller.model.enumeration.LoginAuthType;
import com.clougence.rdp.dal.enumeration.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/9 12:09
 */
@Getter
@Setter
public class LoginFO {

    @NotNull(message = "{login.accounttype.notnull}")
    private AccountType         accountType;

    @NotNull(message = "{login.logintype.notnull}")
    private LoginAuthType       loginType;

    private String              account;

    private String              password;

    private String              verifyCode;

    @JsonIgnore
    private String              accessToken;

    private String              token;

    private LoginAutoRegisterFO registerInfo;
}
