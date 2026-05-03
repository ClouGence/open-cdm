package com.clougence.clouddm.sdk.execute.tools;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;

@FunctionalInterface
public interface ToolFactory {

    Tool create(ToolConfig toolsConfig);
}
