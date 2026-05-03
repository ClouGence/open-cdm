package com.clougence.clouddm.console.web.model.fo.editor.query;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/1/16 16:36
 */
@Getter
@Setter
public class CreateSessionFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotNull(message = "initAutoCommit can not be null.")
    private Boolean      initAutoCommit;

    @NotNull(message = "initIsolation can not be null.")
    private String       initIsolation;
}
