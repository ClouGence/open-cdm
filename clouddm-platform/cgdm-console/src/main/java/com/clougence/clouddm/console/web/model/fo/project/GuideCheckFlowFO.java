package com.clougence.clouddm.console.web.model.fo.project;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.ScmType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuideCheckFlowFO {

    private Long         projectID;
    private long         repoScmId;
    private ScmType      repoScmType;
    private String       repoScmUrl;
    private String       repoName;
    private String       repoBranch;
    private String       repoScriptPath;
    private String       dsId;
    private List<String> dsLevels;
}
