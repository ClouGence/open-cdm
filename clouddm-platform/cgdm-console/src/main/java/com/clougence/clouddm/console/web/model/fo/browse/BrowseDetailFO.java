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
public class BrowseDetailFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    @NotBlank(message = "targetType must not blank")
    private String       targetType;

    @NotBlank(message = "targetName must not blank")
    private String       targetName;

    private boolean      refreshCache;
}
