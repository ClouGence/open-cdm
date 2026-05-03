package com.clougence.clouddm.console.web.model.fo.auth;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListElementsOfLevelFO {

    @NotNull(message = "authKind can not be null.")
    private AuthKind     authKind;

    @NotNull(message = "resPaths can not be null.")
    private List<String> resPaths;

    private String       uid;
}
