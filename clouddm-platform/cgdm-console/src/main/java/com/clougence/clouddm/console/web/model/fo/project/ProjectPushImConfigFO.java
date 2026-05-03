package com.clougence.clouddm.console.web.model.fo.project;

import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectPushImConfigFO {

    private boolean delete;

    private long    projectId;
    private Long    imId;
    private ImType  imType;
    private String  language;
    private boolean eventProjectStatus;
    private boolean eventProjectConfig;
    private boolean eventChangeLife;
    private boolean eventChangeNotice;
}
