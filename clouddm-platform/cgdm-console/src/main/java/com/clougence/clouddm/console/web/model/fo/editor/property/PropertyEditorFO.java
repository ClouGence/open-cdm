package com.clougence.clouddm.console.web.model.fo.editor.property;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyEditorFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotNull(message = "types must not null")
    private String       types;
}
