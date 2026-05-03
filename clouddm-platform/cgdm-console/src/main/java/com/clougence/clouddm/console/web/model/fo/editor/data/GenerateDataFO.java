package com.clougence.clouddm.console.web.model.fo.editor.data;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clougence.clouddm.console.web.model.vo.editor.data.DataEditorColumnVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateDataFO {

    @NotNull(message = "levels must not null")
    private List<String>             levels;

    @NotBlank(message = "targetName must not blank")
    private String                   targetName;

    @NotBlank(message = "targetType must not blank")
    private String                   targetType;

    private List<DataEditorColumnVO> columnList;

    private List<ChangeRowFO>        changeRows;
}
