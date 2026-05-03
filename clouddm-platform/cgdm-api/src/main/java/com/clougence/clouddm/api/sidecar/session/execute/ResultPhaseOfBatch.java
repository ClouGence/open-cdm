package com.clougence.clouddm.api.sidecar.session.execute;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultPhase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultPhaseOfBatch extends ResultPhase {
}
