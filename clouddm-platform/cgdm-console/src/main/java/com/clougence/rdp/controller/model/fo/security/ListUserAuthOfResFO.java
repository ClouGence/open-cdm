package com.clougence.rdp.controller.model.fo.security;

import java.util.List;

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
public class ListUserAuthOfResFO {

    @NotNull(message = "authKind can not be null.")
    private AuthKind                   authKind;

    @NotBlank(message = "targetUid can not be blank.")
    private String                     targetUid;

    @NotNull(message = "groups can not be null.")
    private List<ListAuthOfResGroupFO> groups;
}
