package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/1/11 10:34
 */
@Data
public class UpdateSubAccountFO {

    @NotBlank(message = "{notblank.targetuid}")
    private String targetUid;

    // @NotBlank(message = "{notblank.username}")
    private String userName;

    // @NotBlank(message = "{notblank.subaccount}")
    private String subAccount;
}
