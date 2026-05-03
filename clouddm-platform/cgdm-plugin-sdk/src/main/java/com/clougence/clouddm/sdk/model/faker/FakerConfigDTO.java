package com.clougence.clouddm.sdk.model.faker;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakerConfigDTO {

    private DataSourceType       dsType;
    private long                 dsId;
    private String               catalog;
    private String               schema;
    private boolean              transaction;
    private boolean              ignoreErrors;
    private Integer              pThreadCnt;
    private Integer              wThreadCnt;
    private String               yaml;
    private FakerRunModel        runModel;

    // for full
    private Map<String, Integer> writerTotal;

    // for increment
    private Integer              workingTime;
    private Integer              insertRatio;
    private Integer              updateRatio;
    private Integer              deleteRatio;
}
