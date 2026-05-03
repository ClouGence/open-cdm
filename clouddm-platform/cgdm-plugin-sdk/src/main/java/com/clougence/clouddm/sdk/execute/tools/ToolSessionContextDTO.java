package com.clougence.clouddm.sdk.execute.tools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/6/1 15:04:45
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ToolSessionContextDTO {

    private String sessionId;
    private long   bindClusterId;
    private String configuration;
}
