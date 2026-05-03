package com.clougence.rdp.controller.model.fo;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class BrowseLevelsFO {

    @NotNull(message = "kind must not null")
    private AuthKind     kind;

    @NotNull(message = "levels must not null")
    private List<String> levels;

    //private String       keyWords;
}
