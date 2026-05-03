package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.rdp.util.RdpPageDO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectChangeListFO {

    private long      projectId;
    private String    searchKeywords;
    private RdpPageDO page;

}
