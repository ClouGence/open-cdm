package com.clougence.clouddm.console.web.model.fo.editor.table;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorReferencedFO {

    @NotNull(message = "levels must not null")
    private List<String>          levels;

    @NotNull
    private String                table;

    private GeneralConstraintType type;
}
