package com.clougence.clouddm.sdk.service.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/11/9 13:23
 */
@Getter
@Setter
public class ConfigData {

    private String configName;
    private String configValue;
    private String defaultValue;
}
