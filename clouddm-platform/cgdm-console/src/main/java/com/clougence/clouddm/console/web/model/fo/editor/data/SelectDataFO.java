package com.clougence.clouddm.console.web.model.fo.editor.data;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectDataFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotBlank(message = "targetName must not blank")
    private String       targetName;

    @NotBlank(message = "targetType must not blank")
    private String       targetType;

    private Integer      offset   = 0; //to first 0;to last -1

    private Integer      pageSize = 20;

    private String       condition;

    private String       orderBy;
}
