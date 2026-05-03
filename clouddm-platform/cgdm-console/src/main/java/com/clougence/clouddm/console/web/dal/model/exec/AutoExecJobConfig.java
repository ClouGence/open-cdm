package com.clougence.clouddm.console.web.dal.model.exec;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoExecJobConfig {

    private boolean       enableTransactional;
    private ErrorStrategy errorStrategy;

    private Long          retryWaitTime;
    private Long          retryCount;
}
