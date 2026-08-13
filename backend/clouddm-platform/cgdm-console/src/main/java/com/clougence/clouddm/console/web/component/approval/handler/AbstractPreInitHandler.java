/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.approval.handler;

import java.io.IOException;
import com.clougence.clouddm.console.web.component.approval.PreInitHandler;
import com.clougence.clouddm.console.web.component.approval.model.PreInitContext;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalBiz;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;

/**
 * Owns the lifecycle of one pre-initialization analysis task.
 */
public abstract class AbstractPreInitHandler implements PreInitHandler {

    @Override
    public boolean supports(DmApprovalDO approval) {
        ApprovalBiz approBiz = approval.getApproBiz();
        return approBiz == ApprovalBiz.DM_QUERY || approBiz == ApprovalBiz.DM_CHANGE;
    }

    @Override
    public final String taskType() {
        return this.analysisType();
    }

    @Override
    public final boolean handle(PreInitContext context) throws IOException {
        try {
            context.start();
            this.doHandle(context);
            context.finish();
        } catch (Exception e) {
            context.fail(e);
            throw e;
        }
        return true;
    }

    protected abstract String analysisType();

    protected abstract void doHandle(PreInitContext context) throws IOException;
}
