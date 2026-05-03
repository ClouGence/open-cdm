package com.clougence.clouddm.sdk.execute.resource;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.utils.timer.Timer;

public interface ToolResourceManager {

    <C extends ToolConfig> Timer getTimer(C dbConfig);

    <C extends ToolConfig> ToolObject requestResource(C dbConfig);

    boolean isBackground();

    boolean isReady();
}
