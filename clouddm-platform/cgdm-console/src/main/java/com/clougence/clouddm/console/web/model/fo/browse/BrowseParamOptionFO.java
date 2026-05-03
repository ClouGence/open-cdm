package com.clougence.clouddm.console.web.model.fo.browse;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseParamOptionFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    // FUNC   or  PROC
    @NotNull(message = "type must not null")
    private String       type;
}
