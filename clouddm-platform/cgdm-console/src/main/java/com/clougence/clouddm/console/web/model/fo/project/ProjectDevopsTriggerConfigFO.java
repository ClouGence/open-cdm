package com.clougence.clouddm.console.web.model.fo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDevopsTriggerConfigFO {

    private long    projectId;
    private long    devopsId;
    private boolean updateHook;
    private boolean updateTrigger;

    //
    private boolean hookEnable;

    //
    private boolean triggerEnable;
    private String  triggerToken;
}
