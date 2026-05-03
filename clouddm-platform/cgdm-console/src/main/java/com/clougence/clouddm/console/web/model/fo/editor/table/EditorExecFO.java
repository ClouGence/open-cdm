package com.clougence.clouddm.console.web.model.fo.editor.table;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-08-09 17:42
 */
@Getter
@Setter
public class EditorExecFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    private String       table;

    @NotNull(message = "sqlList must not null")
    private List<String> sqlList = new ArrayList<>();
}
