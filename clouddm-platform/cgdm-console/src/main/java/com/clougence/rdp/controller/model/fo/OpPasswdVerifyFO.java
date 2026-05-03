package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/1/11 11:17
 */
@Data
public class OpPasswdVerifyFO {

    @NotBlank(message = "{notblank.oppassword}")
    private String opPassword;
}
