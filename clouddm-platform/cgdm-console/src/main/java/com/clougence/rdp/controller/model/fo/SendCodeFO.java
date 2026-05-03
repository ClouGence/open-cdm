package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;
import com.clougence.rdp.dal.enumeration.AreaCode;

import lombok.Data;

/**
 * @author bucketli 2020/2/28 14:01
 */
@Data
public class SendCodeFO {

    @NotNull(message = "{notnull.verifytype}")
    private VerifyType     verifyType;

    @NotNull(message = "{notnull.sub}")
    private boolean        sub;

    private String         account;

    private String         email;

    @Pattern(regexp = "^\\d{1,20}$", message = "{pattern.phone}")
    private String         phoneNumber;

    private AreaCode       phoneAreaCode;

    @NotNull(message = "{notnull.verifycodetype}")
    private VerifyCodeType verifyCodeType;
}
