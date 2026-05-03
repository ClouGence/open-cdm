package com.clougence.clouddm.console.web.service.faker.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakerTableDTO {

    private String                      catalog;
    private String                      schema;
    private String                      table;
    private String                      wherePolitic;
    private String                      updatePolitic;
    private String                      insertPolitic;
    private Integer                     total;
    private List<String>                ignoreColsAll;
    private Map<String, FakerColumnDTO> columns;
}
