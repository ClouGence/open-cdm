package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.clougence.rdp.dal.enumeration.AreaCode;

import lombok.Data;

@Data
public class UpdateUserPhoneFO {

    @NotBlank(message = "{notnull.phone}")
    @Pattern(regexp = "^\\d{1,20}$", message = "{pattern.phone}")
    private String   phone;

    @NotNull(message = "{notnull.phoneareacode}")
    private AreaCode phoneAreaCode;

    @NotNull(message = "{notnull.verifycode}")
    @NotBlank(message = "{notblank.verifycode}")
    private String   verifyCode;
}
