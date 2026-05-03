package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import com.clougence.rdp.controller.model.enumeration.VerifyCodeType;
import com.clougence.rdp.controller.model.enumeration.VerifyType;

import lombok.Data;

/**
 * @author bucketli 2020/2/28 14:01
 */
@Data
public class SendCodeAfterLoginFO {

    @NotNull(message = "{notnull.verifytype}")
    private VerifyType     verifyType;

    @NotNull(message = "{notnull.verifycodetype}")
    private VerifyCodeType verifyCodeType;
}
