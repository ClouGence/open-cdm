package com.clougence.rdp.controller.model.fo.security;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/21 15:31:41
 */
@Getter
@Setter
public class ListMyAuthResFO {

    @NotNull(message = "{notnull.authkind}")
    private AuthKind authKind;
}
