package com.clougence.clouddm.console.web.model.fo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDevopsCallBackFO {

    private long    projectId;
    private long    devopsId;
    private boolean enable;
    private String  method;
    private String  url;
}
