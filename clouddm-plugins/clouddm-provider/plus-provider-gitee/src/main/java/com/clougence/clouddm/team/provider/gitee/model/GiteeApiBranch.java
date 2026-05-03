package com.clougence.clouddm.team.provider.gitee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiteeApiBranch {

    private String               name;
    private GiteeApiBranchCommit commit;
}
