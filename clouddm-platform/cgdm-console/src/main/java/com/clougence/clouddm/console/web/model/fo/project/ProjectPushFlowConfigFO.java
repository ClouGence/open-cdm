package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeApproveStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeCheckStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmChangeExecStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectPushFlowConfigFO {

    private long                    projectId;
    private DmChangeCheckStrategy   checkStrategy;
    private DmChangeApproveStrategy approveStrategy;
    private DmChangeExecStrategy    executeStrategy;
    private boolean                 transactional;
    private ErrorStrategy           errorStrategy;
    //private Long                    retryWaitTime;
    //private Long                    retryCount;
}
