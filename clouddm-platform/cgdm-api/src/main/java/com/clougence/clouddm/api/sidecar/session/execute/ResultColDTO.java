package com.clougence.clouddm.api.sidecar.session.execute;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultColDTO {

    private boolean        success;
    private String         message;

    private ResultSetValue value;
}
