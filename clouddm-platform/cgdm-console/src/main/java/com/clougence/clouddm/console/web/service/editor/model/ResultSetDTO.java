package com.clougence.clouddm.console.web.service.editor.model;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Deprecated // use ResultSet or ResultCount
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSetDTO {

    private String                    sessionId;
    private String                    queryId;
    private long                      fetchTimeMs;

    private int                       resultIndex;

    private int                       scanCount;
    private int                       fetchCount;
    private int                       filterCount;

    private boolean                   success;
    private String                    message;

    private boolean                   updateCountResult;
    private long                      updateCount;

    private String                    sql;
    //private String                    resource;
    private List<String>              columnList;
    private List<String>              columnType;
    private List<ResultSetRow>        rowSet;

    private List<Map<String, String>> generatedKeys;
    private Map<String, String>       outParams;
}
