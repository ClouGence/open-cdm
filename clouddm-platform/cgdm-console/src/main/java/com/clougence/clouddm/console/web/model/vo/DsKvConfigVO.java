package com.clougence.clouddm.console.web.model.vo;

import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/11/9 13:23
 */
@Getter
@Setter
public class DsKvConfigVO {

    private Long          id;

    private String        configName;

    private DsConfigGroup configGroup;

    private String        description;

    private boolean       valueRequire;

    private String        valueValidRegex;

    private String        configValue;

    private String        defaultValue;

    private String        valueAdvance;

    private boolean       readOnly;

    private boolean       isSecret;

    private boolean       needCreated;

}
