package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/3/4 12:36
 */
@Data
public class DeleteSubAccountFO {

    @NotBlank(message = "{notblank.subaccount}")
    private String subAccount;
}
