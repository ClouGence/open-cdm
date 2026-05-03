package com.clougence.clouddm.console.web.model.fo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDevopsSwitchFO {

    private long    projectId;
    private long    devopsId;
    private boolean enable;
}
