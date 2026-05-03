package com.clougence.clouddm.api.sidecar.autoexec;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.api.console.autoexec.ErrorStrategy;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoExecJobDTO {

    private Long                  jobId;
    private Requester             requester;
    private String                uid;
    private List<String>          levels;

    private boolean               jobIsExecByAnother;
    private boolean               jobNotExists;

    private boolean               enableTransactional;
    private ErrorStrategy         errorStrategy;

    private Long                  retryWaitTime;
    private Long                  retryCount;

    private List<AutoExecTaskDTO> taskList = new ArrayList<>();

    private SessionContextDTO     contextDTO;

    private Long                  dsId;
    private DataSourceType        dsType;

}
