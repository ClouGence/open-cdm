package com.clougence.clouddm.console.web.model.fo.ticket;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.console.web.dal.enumeration.DmAutoExecType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmAutoExecConfigFO {

    private DmAutoExecType autoExecType;
    private boolean        enableTransactional;
    private ErrorStrategy  errorStrategy;
    private Long           retryWaitTime;
    private Long           retryCount;
    private Long           execTime;
    private boolean        snapshot;
}
