package com.clougence.clouddm.console.web.model.fo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDevopsCreateFO {

    private long                  projectId;
    private GuidePipelineFO       pipeline;
    private ProjectDevopsOptionFO option;
}
