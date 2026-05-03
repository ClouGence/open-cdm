package com.clougence.clouddm.console.web.service.project.domain;

import com.clougence.clouddm.console.web.dal.model.DmProjectChangeDO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSuggest {

    private CreateSuggestType suggestType;
    private DmProjectChangeDO change;
}
