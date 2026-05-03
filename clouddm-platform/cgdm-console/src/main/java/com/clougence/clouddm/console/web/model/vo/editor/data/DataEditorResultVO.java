package com.clougence.clouddm.console.web.model.vo.editor.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataEditorResultVO {

    private String                    catalog;

    private String                    schema;

    private String                    table;

    private List<DataEditorColumnVO>  columnList    = new ArrayList<>();

    @JsonInclude()
    private List<Map<String, Object>> resultSet     = new ArrayList<>();

    @JsonInclude()
    private List<Map<String, Long>>   resultSetMore = new ArrayList<>();

    private boolean                   readOnly;

    private Integer                   offset;

    private Integer                   pageSize;

    private Boolean                   hasNext;

    private Boolean                   isFirst;

    @JsonIgnore
    private Boolean                   executeSuccess;

    @JsonIgnore
    private String                    message;
}
