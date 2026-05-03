package com.clougence.clouddm.team.provider.gitee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiteeApiWebHooks {

    private String  id;
    private String  url;
    private String  password;
    private String  result;
    private String  result_code;
    private String  created_at;
    private boolean push_events;
    private boolean tag_push_events;
    private boolean issues_events;
    private boolean note_events;
    private boolean merge_requests_events;
}
