package com.clougence.rdp.service.openapi.model;

import javax.validation.constraints.Min;

import com.clougence.rdp.component.mcp.model.McpField;
import com.clougence.rdp.constant.RdpMcpLabelKeys;

import lombok.Data;

/**
 * @author bucketli 2022/10/25 15:15:58
 */
@Data
public class ApiQueryDsFO {

    @Min(value = 1, message = "{min.datasourceid}")
    @McpField(value = RdpMcpLabelKeys.DATASOURCE_ID, required = true)
    private long dataSourceId;

}
