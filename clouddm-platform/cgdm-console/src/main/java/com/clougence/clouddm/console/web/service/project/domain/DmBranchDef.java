package com.clougence.clouddm.console.web.service.project.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmBranchDef {

    private long   scmId;
    private String repoName;
    private String branch;
    private String branchCommitId;
}
