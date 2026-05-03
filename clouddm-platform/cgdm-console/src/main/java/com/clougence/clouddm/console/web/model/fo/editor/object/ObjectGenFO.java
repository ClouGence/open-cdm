package com.clougence.clouddm.console.web.model.fo.editor.object;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectGenFO {

    @NotNull(message = "levels must not null")
    private List<String>        levels;

    private String              targetName;

    private String              actionType;

    private Map<String, Object> data;
}
