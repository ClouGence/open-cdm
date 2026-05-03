package com.clougence.clouddm.worker.component.tools;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.sdk.execute.resource.ToolObject;
import com.clougence.clouddm.sdk.execute.tools.Tool;

public class DefaultToolSession extends AbstractToolSession {

    private final ToolObject toolsObject;

    public DefaultToolSession(String newSessionId, ToolConfig toolsConfig, ToolObject toolsObject){
        super(newSessionId, toolsConfig);
        this.toolsObject = toolsObject;
    }

    @Override
    protected Tool currentResource() {
        return this.toolsObject.getTarget();
    }

    @Override
    public void close() throws Exception {
        try {
            this.prepareMDC(this.toolsConfig, this.newSessionId);
            this.toolsObject.close();
            log.info("tool close complete.");
        } catch (Exception e) {
            log.error("tool closeSession failed, " + e.getMessage(), e);
            throw e;
        } finally {
            this.clearMDC();
        }
    }
}
