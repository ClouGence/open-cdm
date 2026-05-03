package com.clougence.clouddm.console.web.model.fo.project;

import java.util.List;

import com.clougence.clouddm.sdk.scm.ScmEventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuidePipelineFO {

    private long            repoScmId;
    private String          repoScmUrl;
    private String          repoSpace;
    private String          repoName;
    private String          repoBranch;
    private String          repoScriptPath;
    private List<String>    dsLevels;
    private ScmEventType eventType;
}
