package com.clougence.clouddm.console.web.model.fo.openapi;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.rdp.component.mcp.model.McpField;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2025/12/9 10:18:45
 */
@Getter
@Setter
public class DmApiDsListFO {

    @McpField(value = DmMcpI18nKey.F_DATA_SOURCE_ID_DESC)
    private Long           dataSourceId;

    @McpField(value = DmMcpI18nKey.F_DATA_SOURCE_TYPE_DESC)
    private DataSourceType dataSourceType;

    @McpField(value = DmMcpI18nKey.F_INSTANCE_ID_LIKE_DESC)
    private String         instanceIdLike;

    @McpField(value = DmMcpI18nKey.F_INSTANCE_DESC_LIKE_DESC)
    private String         instanceDescLike;

    @McpField(value = DmMcpI18nKey.F_HOST_LIKE_DESC)
    private String         hostLike;
}
