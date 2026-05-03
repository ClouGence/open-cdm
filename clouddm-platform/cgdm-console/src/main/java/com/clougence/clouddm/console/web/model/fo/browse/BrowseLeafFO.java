package com.clougence.clouddm.console.web.model.fo.browse;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2020/4/13
 **/
@Getter
@Setter
public class BrowseLeafFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotBlank(message = "leafType must not blank")
    private String       leafType;

    private String       pattern;

    private boolean      refreshCache;
}
