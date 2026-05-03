package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UpdateUserEmailWithPwdFO {

    @NotNull(message = "{notnull.email}")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "{pattern.email}")
    private String email;

    @NotBlank(message = "{notblank.password}")
    private String password;
}
