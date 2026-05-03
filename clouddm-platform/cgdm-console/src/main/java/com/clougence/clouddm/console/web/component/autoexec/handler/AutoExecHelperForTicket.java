package com.clougence.clouddm.console.web.component.autoexec.handler;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.autoexec.AutoExecHelper;
import com.clougence.clouddm.console.web.dal.enumeration.SQLJobBizType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecHelperForTicket implements AutoExecHelper {

    @Override
    public SQLJobBizType getHandleType() { return SQLJobBizType.TICKET; }

    @Override
    public void execStart(SQLJobBizType bizType, String bizId) {

    }

    @Override
    public void execCompleted(SQLJobBizType bizType, String bizId) {

    }

    @Override
    public void execAbort(SQLJobBizType bizType, String bizId) {

    }

    @Override
    public void execFailed(SQLJobBizType bizType, String bizId) {

    }
}
