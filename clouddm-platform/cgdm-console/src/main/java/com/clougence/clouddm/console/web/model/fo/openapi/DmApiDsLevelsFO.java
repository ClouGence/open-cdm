package com.clougence.clouddm.console.web.model.fo.openapi;

import java.util.List;

import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.rdp.component.mcp.model.McpField;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2025/12/9 10:18:45
 */
@Getter
@Setter
public class DmApiDsLevelsFO {

    @McpField(value = DmMcpI18nKey.F_LEVELS_DESC, required = true)
    private List<String> levels;

    @McpField(value = DmMcpI18nKey.F_REFRESH_CACHE_DESC)
    private boolean      refreshCache;
}
