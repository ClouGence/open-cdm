package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.console.web.model.fo.ticket.DmAutoExecConfigFO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectChangeConfirmExecRequestFO {

    private long               changeId;
    private DmAutoExecConfigFO config;

}
