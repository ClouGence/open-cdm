package com.clougence.clouddm.sdk.model.faker;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakerTailResponseDTO {

    private Long           successTotal;
    private Long           successInsertTotal;
    private Long           successUpdateTotal;
    private Long           successDeleteTotal;
    private Long           failedTotal;
    private Long           failedInsertTotal;
    private Long           failedUpdateTotal;
    private Long           failedDeleteTotal;

    private double         writeAvgTimeMs;
    private List<String>   logArr;
    private FakerRunStatus status;
    private int            endLine;
    private String         logFile;
    private String         logHost;
}
