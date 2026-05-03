package com.clougence.clouddm.api.sidecar.session.execute;

import java.util.List;

import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultList {

    private String       sessionId;
    private StatusDTO    status;
    private List<Result> resultList;
}
