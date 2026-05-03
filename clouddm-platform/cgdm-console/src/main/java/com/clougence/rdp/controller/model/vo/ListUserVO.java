package com.clougence.rdp.controller.model.vo;

import com.clougence.rdp.dal.enumeration.AccountBindType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/8 21:42
 */
@Getter
@Setter
public class ListUserVO {

    private String          uid;

    private String          email;

    private String          username;

    private String          phone;

    private String          subAccount;

    private AccountBindType bindType;

    private String          bindAccount;

    private Long            roleId;

    private String          roleName;

    private boolean         innerRole;

    private String          userDomain;

    private boolean         disable;

    private boolean         resourceManage;

    private String          lastTryLoginTime;

    private boolean         useMfa;
}
