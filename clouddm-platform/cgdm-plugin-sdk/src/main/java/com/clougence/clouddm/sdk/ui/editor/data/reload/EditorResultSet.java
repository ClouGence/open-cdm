package com.clougence.clouddm.sdk.ui.editor.data.reload;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorResultSet {

    private long                          fetchTimeMs;
    private String                        resultId;

    private int                           fetchCount;

    private boolean                       success;
    private String                        message;

    private boolean                       updateCountResult;
    private long                          updateCount;

    private String                        sql;
    private List<Map<TargetType, String>> resource;
    private List<String>                  columnList;
    private List<String>                  columnType;
    private List<ResultSetRow>            rowSet;
    private String                        cacheFile;

    private List<Map<String, String>>     generatedKeys;
    private Map<String, String>           outParams;
}
