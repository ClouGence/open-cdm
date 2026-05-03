package com.clougence.rdp.controller.model.fo.mfa;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmResetMfaSettingsFO {

    @NotBlank(message = "{notblank.mfaCode}")
    @Pattern(regexp = "^\\d{6}$", message = "{pattern.mfaCode}")
    private String mfaCode;
}
