package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.clougence.rdp.controller.model.enumeration.VerifyType;

import lombok.Data;

@Data
public class UpdateUserEmailFO {

    @NotNull(message = "{notnull.email}")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "{pattern.email}")
    private String     email;

    @NotBlank(message = "{notblank.verifycode}")
    private String     verifyCode;

    @NotNull(message = "{notnull.verifycodetype}")
    private VerifyType verifyType;
}
