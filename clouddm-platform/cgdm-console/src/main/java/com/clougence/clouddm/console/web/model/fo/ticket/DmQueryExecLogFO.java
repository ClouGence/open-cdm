package com.clougence.clouddm.console.web.model.fo.ticket;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.console.web.dal.enumeration.DmLogDependBizType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmQueryExecLogFO {

    private Long               taskId;
    @NotNull(message = "jobId must not null")
    private Long               jobId;
    @NotNull(message = "dependBizType must not null")
    private DmLogDependBizType dependBizType;
}
