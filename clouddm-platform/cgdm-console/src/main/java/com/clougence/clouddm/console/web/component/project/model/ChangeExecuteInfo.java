package com.clougence.clouddm.console.web.component.project.model;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmAutoExecType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeExecuteInfo {

    private DmAutoExecType execType;
    private boolean        transactional;
    private ErrorStrategy  errorStrategy;
    private Long           retryWaitTime;
    private Long           retryCount;
    private Long           execTime;

    private boolean        snapshot;
}
