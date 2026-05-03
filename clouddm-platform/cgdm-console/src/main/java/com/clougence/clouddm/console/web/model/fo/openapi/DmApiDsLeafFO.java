package com.clougence.clouddm.console.web.model.fo.openapi;

import java.util.List;

import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.rdp.component.mcp.model.McpField;

import lombok.Data;

/**
 * @author bucketli 2022/10/25 15:15:58
 */
@Data
public class DmApiDsLeafFO {

    @McpField(value = DmMcpI18nKey.F_LEVELS_DESC, required = true)
    private List<String> levels;

    @McpField(value = DmMcpI18nKey.F_LEAF_TYPE_DESC, required = true)
    private String       leafType;

    @McpField(value = DmMcpI18nKey.F_REFRESH_CACHE_DESC)
    private boolean      refreshCache;
}
