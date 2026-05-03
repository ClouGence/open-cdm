package com.clougence.rdp.controller.model.fo.mfa;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginMfaValidFO {

    @Min(value = 1, message = "min.mfaCode")
    @NotNull(message = "notnull.mfaCode")
    private Integer mfaCode;

    @NotBlank(message = "notnull.mfaPreActionToken")
    private String  mfaPreActionToken;
}
