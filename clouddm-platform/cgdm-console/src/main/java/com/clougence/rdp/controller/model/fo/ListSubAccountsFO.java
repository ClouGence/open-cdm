package com.clougence.rdp.controller.model.fo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 12:09
 */
@Getter
@Setter
public class ListSubAccountsFO {

    private long   roleId;

    private String userNameOrSubAccountPrefix;
}
