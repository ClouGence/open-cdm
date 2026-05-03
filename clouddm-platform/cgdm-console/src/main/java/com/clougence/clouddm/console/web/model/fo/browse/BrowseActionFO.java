package com.clougence.clouddm.console.web.model.fo.browse;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseActionFO {

    @NotNull(message = "levels must not null")
    private List<String>        levels;

    @NotBlank(message = "actionType must not blank")
    private String              actionType;

    private String              targetType;

    private String              targetName;

    private String              targetNewName;

    // such as column name
    private String              targetExactName;

    private Map<String, Object> options;
}
