/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package com.clougence.clouddm.worker.component.tools;

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
