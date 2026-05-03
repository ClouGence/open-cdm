package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UpdateUserPhoneWithPwdFO {

    @NotBlank(message = "{notnull.phone}")
    @Pattern(regexp = "^\\d{1,20}$", message = "{pattern.phone}")
    private String phone;

    @NotBlank(message = "{notblank.password}")
    private String password;
}
