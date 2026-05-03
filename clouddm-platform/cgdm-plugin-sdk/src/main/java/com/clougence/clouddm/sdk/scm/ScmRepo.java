package com.clougence.clouddm.sdk.scm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmRepo {

    private String repoSpace;
    private String repoName;
    private String repoUrl;
    private String repoHome;
    private String branchName;
}
