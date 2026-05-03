package com.clougence.clouddm.console.web.model.vo.faker;

import java.util.List;

import com.clougence.clouddm.sdk.model.faker.FakerRunStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
public class FakerLogVO {

    private boolean        success;
    private String         message;
    private Long           successTotal;
    private Long           successInsertTotal;
    private Long           successUpdateTotal;
    private Long           successDeleteTotal;
    private Long           failedTotal;
    private Long           failedInsertTotal;
    private Long           failedUpdateTotal;
    private Long           failedDeleteTotal;
    private String         writeAvgTime;
    private List<String>   logArr;
    private FakerRunStatus status;
    private int            endLine;
    private String         logFile;
    private String         logHost;
}
