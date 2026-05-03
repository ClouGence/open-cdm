package com.clougence.clouddm.console.web.model.fo.auth;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListUserLeafFo {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotBlank(message = "leafType must not blank")
    private String       leafType;

    private String       pattern;

    private String       uid;

}
