package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeApproveStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeCheckStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeExecStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmInitScriptStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectOptionFO {

    private DmInitScriptStrategy    initScript;

    // for flow
    private DmChangeCheckStrategy   checkStrategy;
    private DmChangeApproveStrategy approveStrategy;
    private DmChangeExecStrategy    executeStrategy;

    // exec default
    private boolean                 transactional;
    private ErrorStrategy           errorStrategy;
    private Long                    retryWaitTime;
    private Long                    retryCount;
}
