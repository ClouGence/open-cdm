package com.clougence.clouddm.console.web.model.fo.editor.table;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorDefFO {

    @NotNull(message = "levels must not null")
    private List<String>       levels;

    @NotNull(message = "viewMode must not null")
    private EditorViewModeEnum viewMode;
}
