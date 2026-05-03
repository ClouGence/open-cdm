package com.clougence.clouddm.console.web.service.editor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEditorExecuteResultDTO {

    @JsonInclude()
    private List<Map<String, Object>> resultSet     = new ArrayList<>();

    @JsonInclude()
    private List<Map<String, Long>>   resultSetMore = new ArrayList<>();

    private Boolean                   success;

    private String                    message;

    private Integer                   sequence;

    private boolean                   refresh;
}
