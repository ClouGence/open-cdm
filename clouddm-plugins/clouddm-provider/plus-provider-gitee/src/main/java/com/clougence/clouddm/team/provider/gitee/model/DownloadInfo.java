package com.clougence.clouddm.team.provider.gitee.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadInfo {

    private String spacePath;
    private String repoPath;
    private String repoName;
    private String repoUrl;
    private String repoHome;
    private String repoBranch;

    public DownloadInfo(String spacePath, String repoPath, String repoName, String repoUrl, String repoHome, String repoBranch){
        this.spacePath = spacePath;
        this.repoPath = repoPath;
        this.repoName = repoName;
        this.repoUrl = repoUrl;
        this.repoHome = repoHome;
        this.repoBranch = repoBranch;
    }
}
