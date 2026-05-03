package com.clougence.clouddm.console.web.model.fo.browse;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseRemarkDsFO {

    @NotNull(message = "levels must not null")
    private List<String> levels;

    private String       remark;
}
