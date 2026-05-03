package com.clougence.rdp.service.model;

import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.enumeration.AreaCode;

import lombok.Data;

/**
 * @author bucketli 2020/2/28 14:01
 */
@Data
public class CheckVerifyMO {

    private boolean        isSubAccount;

    private String         subAccountName;

    private VerifyType     verifyType;

    private String         email;

    private String         phoneNumber;

    private String         verifyCode;

    private VerifyCodeType verifyCodeType;

    private String         uid;

    private AreaCode       phoneAreaCode;
}
