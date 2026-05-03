package com.clougence.clouddm.console.web.model.fo.browse;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowseConvertDDLFO {

    @NotNull(message = "levels must not null")
    private List<String>        levels;

    private String              leafType;

    private String              sourceTableName;

    @NotNull(message = "levels must not null")
    private String              targetDsType;

    private Map<String, Object> options;
}
