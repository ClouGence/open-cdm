package com.clougence.clouddm.console.web.model.fo.editor.table;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-07-17 16:30
 */
@Getter
@Setter
public class EditorGenFO {

    @NotNull(message = "levels must not null")
    private List<String>      levels;

    private String            table;

    @NotNull(message = "schema Data must not null")
    private TableEditorUiData tableSchema;
}
