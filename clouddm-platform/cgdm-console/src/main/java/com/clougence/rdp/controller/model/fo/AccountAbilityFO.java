package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/11 13:53
 */
@Getter
@Setter
public class AccountAbilityFO {

    @NotNull(message = "{notblank.uid}")
    private String  uid;

    @NotNull(message = "{notnull.disable}")
    private Boolean disable;
}
