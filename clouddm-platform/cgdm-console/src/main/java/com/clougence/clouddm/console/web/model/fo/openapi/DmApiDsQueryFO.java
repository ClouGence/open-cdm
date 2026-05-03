package com.clougence.clouddm.console.web.model.fo.openapi;

import java.util.List;

import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryType;
import com.clougence.rdp.component.mcp.model.McpField;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2025/12/9 10:18:45
 */
@Getter
@Setter
public class DmApiDsQueryFO {

    @McpField(value = DmMcpI18nKey.F_LEVELS_DESC, required = true)
    private List<String> levels;

    @McpField(value = DmMcpI18nKey.F_QUERY_TYPE_DESC, required = true)
    private WsQueryType  queryType;

    @McpField(value = DmMcpI18nKey.F_QUERY_STRING_DESC, required = true)
    private String       queryString;

    @McpField(value = DmMcpI18nKey.F_QUERY_FORCE_DESC)
    private boolean      queryForce;

}
