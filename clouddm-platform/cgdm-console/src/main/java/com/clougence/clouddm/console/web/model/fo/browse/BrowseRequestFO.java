package com.clougence.clouddm.console.web.model.fo.browse;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseRequestFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    private String       targetType;

    private String       targetName;
}
