package com.clougence.clouddm.console.web.model.fo.object;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.console.web.model.fo.editor.table.EditorViewModeEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectEditorDefFO {

    @NotNull(message = "levels must not null")
    private List<String>       levels;

    @NotNull(message = "targetType must not null")
    String                     targetType;

    // help trigger fetch columns
    String                     targetName;

    @NotNull(message = "viewMode must not null")
    private EditorViewModeEnum viewMode;
}
