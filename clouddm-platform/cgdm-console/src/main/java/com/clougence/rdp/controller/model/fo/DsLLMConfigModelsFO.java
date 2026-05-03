package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.llm.LLMAction;

import lombok.Data;

/**
 * @author bucketli 2020/12/30 14:24
 */
@Data
public class DsLLMConfigModelsFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long           dataSourceId;

    @NotNull(message = "{notnull.datasourcetype}")
    private DataSourceType dataSourceType;

    @NotNull(message = "{notnull.datasourcetype}")
    private LLMAction      llmAction;
}
