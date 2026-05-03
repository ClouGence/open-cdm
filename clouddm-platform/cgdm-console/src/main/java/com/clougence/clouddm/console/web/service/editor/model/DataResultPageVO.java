package com.clougence.clouddm.console.web.service.editor.model;

import java.util.List;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResultPageVO {

    private List<ResultSetRow> rowSet;
}
