package com.clougence.clouddm.console.web.component.autoexec;

import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;

public interface AutoExecHelper {

    SQLJobBizType getHandleType();

    void execStart(SQLJobBizType bizType, String bizId);

    void execCompleted(SQLJobBizType bizType, String bizId);

    void execAbort(SQLJobBizType bizType, String bizId);

    void execFailed(SQLJobBizType bizType, String bizId);
}
