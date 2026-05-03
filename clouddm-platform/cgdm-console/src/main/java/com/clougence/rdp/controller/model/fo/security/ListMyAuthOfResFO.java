package com.clougence.rdp.controller.model.fo.security;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/2/21 09:57:43
 */
@Getter
@Setter
public class ListMyAuthOfResFO {

    @NotNull(message = "authKind can not be null.")
    private AuthKind                   authKind;

    @NotNull(message = "groups can not be null.")
    private List<ListAuthOfResGroupFO> groups;
}
