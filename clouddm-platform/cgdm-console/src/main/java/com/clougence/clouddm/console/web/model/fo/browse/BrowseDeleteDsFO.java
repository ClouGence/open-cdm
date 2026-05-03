package com.clougence.clouddm.console.web.model.fo.browse;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseDeleteDsFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;
}
