package com.clougence.clouddm.console.web.model.vo.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevopsScmRepoVO {

    private long   scmId;
    private String repoSpace;
    private String repoName;
    private String repoUrl;
    private String repoHome;
    private String repoBranch;
}
