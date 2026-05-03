package com.clougence.clouddm.console.web.model.fo.editor.table;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorInitFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotNull(message = "table must not null")
    private String       table;

    private boolean      refreshCache;
}
