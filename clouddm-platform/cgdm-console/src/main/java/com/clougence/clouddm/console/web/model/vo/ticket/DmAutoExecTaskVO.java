package com.clougence.clouddm.console.web.model.vo.ticket;

import com.clougence.clouddm.console.web.dal.enumeration.AutoExecTaskStatus;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmAutoExecTaskVO {

    private Long               taskId;
    private Integer            transactionGroup;
    private SecQueryType       sqlType;
    private Long               affectLine;
    private AutoExecTaskStatus status;
    private String             execSql;
    private Integer            execCount;
    private Integer            executeOrder;

    private boolean            canSkip;
    private boolean            canCanceledSkip;

}
