package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.console.web.dal.enumeration.DmLogDependBizType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectChangeExecLogFO {

    private long               changeId;
    private Long               taskId;
    private Long               jobId;
    private DmLogDependBizType bizType;
}
