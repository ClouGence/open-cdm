package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.console.web.dal.enumeration.DmInitScriptStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDevopsOptionFO {

    private DmInitScriptStrategy initScript;
}
