package com.clougence.clouddm.api.sidecar.session.execute;

import java.util.List;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultPageDTO {

    private boolean            success;
    private String             message;

    private List<ResultSetRow> rowSet;
}
