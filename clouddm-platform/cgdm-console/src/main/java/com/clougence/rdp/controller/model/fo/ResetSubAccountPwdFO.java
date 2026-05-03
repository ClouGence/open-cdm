package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/1/8 21:55
 */
@Data
public class ResetSubAccountPwdFO {

    @NotBlank(message = "{notblank.operator_password}")
    private String operatorPwd;

    @NotBlank(message = "{notblank.uid}")
    private String subAccountUid;

    @NotBlank(message = "{notblank.password}")
    private String newPassword;
}
