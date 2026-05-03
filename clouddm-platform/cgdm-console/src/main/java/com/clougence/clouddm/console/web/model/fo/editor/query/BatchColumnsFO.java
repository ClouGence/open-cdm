package com.clougence.clouddm.console.web.model.fo.editor.query;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/16 16:36
 */
@Getter
@Setter
public class BatchColumnsFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotBlank(message = "targetType must not blank")
    private String       targetType;

    @NotBlank(message = "targetNames must not blank")
    private List<String> targetNames;
}
