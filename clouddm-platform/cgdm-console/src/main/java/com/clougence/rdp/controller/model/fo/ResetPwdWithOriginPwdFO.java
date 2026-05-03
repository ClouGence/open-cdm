package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/1/8 21:55
 */
@Data
public class ResetPwdWithOriginPwdFO {

    @NotBlank(message = "{notblank.oripassword}")
    private String originPassword;

    @NotBlank(message = "{notblank.password}")
    private String newPassword;
}
