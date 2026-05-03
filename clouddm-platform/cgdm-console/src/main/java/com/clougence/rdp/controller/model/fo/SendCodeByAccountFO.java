package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.enumeration.AreaCode;

import lombok.Data;

/**
 * @author bucketli 2020/2/28 14:01
 */
@Data
public class SendCodeByAccountFO {

    @NotNull(message = "{notnull.verifycodetype}")
    private VerifyCodeType verifyCodeType;

    @NotNull(message = "{notnull.verifytype}")
    private VerifyType     verifyType;

    private String         email;

    private String         phoneNumber;

    private AreaCode       phoneAreaCode;
}
