package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/13 10:18
 */
@Getter
@Setter
public class ListUserAuthResFO {

    @NotNull(message = "{notnull.authkind}")
    private AuthKind authKind;

    @NotBlank(message = "{notnull.targetUid}")
    private String   targetUid;
}
