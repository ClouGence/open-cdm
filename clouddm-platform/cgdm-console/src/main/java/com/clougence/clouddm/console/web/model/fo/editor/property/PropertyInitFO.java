package com.clougence.clouddm.console.web.model.fo.editor.property;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyInitFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotNull(message = "leafName must not null")
    private String       leafName;

    @NotNull(message = "type must not null")
    private String       type;
}
