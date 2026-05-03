package com.clougence.clouddm.team.provider.gitee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiteeWebHookEventUser {

    private String id;
    private String name;
    private String username;
    private String email;
}
