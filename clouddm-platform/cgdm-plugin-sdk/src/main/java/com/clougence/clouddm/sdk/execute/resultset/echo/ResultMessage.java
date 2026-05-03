package com.clougence.clouddm.sdk.execute.resultset.echo;

import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultMessage extends Result {

    private MessageLevel level;
    private boolean      notify = true;
}
