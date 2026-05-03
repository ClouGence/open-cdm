package com.clougence.clouddm.api.sidecar.autoexec;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoExecTaskDTO {

    private Long    taskId;
    private String  execSql;
    private Integer execOrder;
}
