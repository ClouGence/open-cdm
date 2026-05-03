package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.rdp.util.RdpPageDO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectListFO {

    private String    searchKeywords;
    private String    mark;
    private String    status;
    private RdpPageDO page;

}
