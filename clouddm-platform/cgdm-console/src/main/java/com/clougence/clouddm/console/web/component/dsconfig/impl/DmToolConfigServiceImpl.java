package com.clougence.clouddm.console.web.component.dsconfig.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.base.metadata.ds.tools.FakerPluginConfig;
import com.clougence.clouddm.console.web.component.dsconfig.DmToolConfigService;
import com.clougence.clouddm.console.web.dal.model.DmToolKvBaseConfigDO;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020/11/7 14:27
 */
@Slf4j
@Service
public class DmToolConfigServiceImpl implements DmToolConfigService {

    @Override
    public ToolConfig fetchToolConfig(String toolName) {
        if (StringUtils.equals(toolName, FakerPluginConfig.TOOL_NAME)) {
            FakerPluginConfig config = new FakerPluginConfig();
            config.deserialize();

            config.setExportMaxConcurrent(50);
            config.setOnlineMaxConcurrent(50);
            return config;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<DmToolKvBaseConfigDO> fetchDefaultConfig(String toolName) {
        return Collections.emptyList();
    }

    @Override
    public void cleanToolConfig(String toolName) {

    }
}
