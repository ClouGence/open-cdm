package com.clougence.clouddm.console.web.service.editor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataEditorResultDTO {

    private String                    catalog;

    private String                    schema;

    private String                    table;

    private List<DataEditorColumn>    columnList    = new ArrayList<>();

    @JsonInclude()
    private List<Map<String, Object>> resultSet     = new ArrayList<>();

    @JsonInclude()
    private List<Map<String, Long>>   resultSetMore = new ArrayList<>();

    private Integer                   offset;

    private Integer                   pageSize;

    private Boolean                   hasNext;

    private Boolean                   isFirst;

    private Boolean                   executeSuccess;

    private String                    message;

    private String                    rowId;

    private List<Map<String, String>> generatedKeys;

    private long                      influenceCount;
}
