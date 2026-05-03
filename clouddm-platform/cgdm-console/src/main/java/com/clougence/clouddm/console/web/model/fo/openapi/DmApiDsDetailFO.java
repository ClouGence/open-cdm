package com.clougence.clouddm.console.web.model.fo.openapi;

import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.rdp.component.mcp.model.McpField;

import lombok.Data;

/**
 * @author bucketli 2022/10/25 15:15:58
 */
@Data
public class DmApiDsDetailFO {

    @McpField(value = DmMcpI18nKey.F_DATA_SOURCE_ID_DESC, required = true)
    private long dataSourceId;

}
