package com.clougence.rdp.controller.model.fo;

import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.enumeration.AreaCode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/2/28 14:01
 */
@Getter
@Setter
public class VerifyMO {

    private VerifyType     verifyType;

    private boolean        sub;

    private String         account;

    private String         email;

    private String         phoneNumber;

    private AreaCode       phoneAreaCode;

    private VerifyCodeType verifyCodeType;
}
