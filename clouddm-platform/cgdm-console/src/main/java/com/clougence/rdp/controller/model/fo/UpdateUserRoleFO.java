package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/8 21:55
 */
@Getter
@Setter
public class UpdateUserRoleFO {

    @NotBlank(message = "{notblank.subaccountuid}")
    private String subAccountUid;

    @Min(value = 1, message = "{min.roleid}")
    private long   roleId;
}
