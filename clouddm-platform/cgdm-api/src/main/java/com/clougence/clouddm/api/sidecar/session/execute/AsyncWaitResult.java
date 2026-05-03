package com.clougence.clouddm.api.sidecar.session.execute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncWaitResult {

    private String  sessionId;
    private boolean success;
    private String  message;
}
