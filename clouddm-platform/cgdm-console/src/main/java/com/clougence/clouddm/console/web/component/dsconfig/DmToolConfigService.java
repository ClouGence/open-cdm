package com.clougence.clouddm.console.web.component.dsconfig;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.console.web.dal.model.DmToolKvBaseConfigDO;

/**
 * @author bucketli 2020/11/7 14:25
 */
public interface DmToolConfigService {

    ToolConfig fetchToolConfig(String toolName);

    List<DmToolKvBaseConfigDO> fetchDefaultConfig(String toolName);

    void cleanToolConfig(String toolName);

}
