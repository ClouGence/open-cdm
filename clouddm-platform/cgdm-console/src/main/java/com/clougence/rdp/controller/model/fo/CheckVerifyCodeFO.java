package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clougence.rdp.controller.model.enumeration.VerifyType;

import lombok.Data;

@Data
public class CheckVerifyCodeFO {

    @NotNull(message = "{notnull.verifytype}")
    private VerifyType verifyType;

    @NotBlank(message = "{notblank.verifycode}")
    private String     verifyCode;

}
