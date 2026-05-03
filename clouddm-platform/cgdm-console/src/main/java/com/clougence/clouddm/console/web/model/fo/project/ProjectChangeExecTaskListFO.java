package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.console.web.dal.enumeration.AutoExecTaskStatus;
import com.clougence.rdp.util.RdpPageDO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectChangeExecTaskListFO {

    private long               changeId;
    private AutoExecTaskStatus taskStatus;
    private RdpPageDO          page;
}
