package com.clougence.rdp.controller.model.fo.role;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 15:13
 */
@Getter
@Setter
public class DeleteRoleFO {

    @Min(value = 1, message = "{min.roleid}")
    private long roleId;
}
