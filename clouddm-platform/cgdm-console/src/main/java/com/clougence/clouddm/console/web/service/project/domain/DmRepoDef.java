package com.clougence.clouddm.console.web.service.project.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmRepoDef {

    private long   scmId;
    private String repoSpace;
    private String repoName;
    private String repoUrl;
    private String repoHome;
    private String branch;
}
