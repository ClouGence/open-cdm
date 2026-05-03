package com.clougence.rdp.controller.model.vo;

import com.clougence.rdp.dal.enumeration.AccountBindType;
import com.clougence.rdp.dal.enumeration.AccountType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/8 21:42
 */
@Getter
@Setter
public class LoginUserVO {

    private String          puid;

    private String          pUsername;

    private String          pEmail;

    private String          uid;

    private String          email;

    private String          username;

    private String          userDomain;

    private String          organization;

    private String          phone;

    private String          loginAccount;

    private AccountType     accountType;

    private AccountBindType bindType;

    private String          bindAccount;

    private boolean         isMaintainer;

    private Long            subAccountPwdValidDays;

    private boolean         useMfa;
}
