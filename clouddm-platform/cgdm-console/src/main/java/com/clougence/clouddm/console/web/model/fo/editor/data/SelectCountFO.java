package com.clougence.clouddm.console.web.model.fo.editor.data;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectCountFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotBlank(message = "targetName must not blank")
    private String       targetName;

    @NotBlank(message = "targetType must not blank")
    private String       targetType;

    private String       condition;
}
