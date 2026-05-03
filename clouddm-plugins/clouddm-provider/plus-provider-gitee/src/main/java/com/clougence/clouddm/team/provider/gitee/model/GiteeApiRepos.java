package com.clougence.clouddm.team.provider.gitee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiteeApiRepos {

    private Long                   id;
    private String                 full_name;
    private String                 url;
    private GiteeApiReposNamespace namespace;
    private String                 path;
    private String                 name;
    private String                 html_url;
    private String                 default_branch;
}
